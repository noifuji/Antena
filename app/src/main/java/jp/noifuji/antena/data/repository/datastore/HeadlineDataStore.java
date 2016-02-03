package jp.noifuji.antena.data.repository.datastore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.noifuji.antena.data.db.HeadlineDao;
import jp.noifuji.antena.data.db.HeadlineEntity;
import jp.noifuji.antena.data.db.HeadlineOpenHelper;
import jp.noifuji.antena.data.db.ThumbnailDao;
import jp.noifuji.antena.data.db.ThumbnailEntity;
import jp.noifuji.antena.data.db.ThumbnailOpenHelper;
import jp.noifuji.antena.data.entity.Headline;
import jp.noifuji.antena.data.net.WebAPI;
import jp.noifuji.antena.util.Utils;

/**
 * 記事タイトルリストをWebAPIに問い合わせて取得する。
 * キャッシュが存在する場合は、差分をWebAPIから取得する。
 */
public class HeadlineDataStore implements DataStore {
    private static final String TAG = "HeadlineDataStore";

    @Override
    public List<Headline> headlineList(Context context, String category) throws IOException, JSONException {
        Log.e(TAG, "headlineList:" + System.currentTimeMillis());
        Log.d(TAG, "headlineList called");
        WebAPI webAPI = new WebAPI();
        ArrayList<Headline> results = new ArrayList<>();

        HeadlineOpenHelper helper = new HeadlineOpenHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        ThumbnailOpenHelper helperThumb = new ThumbnailOpenHelper(context);
        SQLiteDatabase dbThumb = helperThumb.getReadableDatabase();
        HeadlineDao headlineDao = new HeadlineDao(db);
        ThumbnailDao thumbnailDao = new ThumbnailDao(dbThumb);

        try {
            HeadlineEntity entity = headlineDao.findLatestPublicationDate();
            ArrayList<HeadlineEntity> newHeadlines;

            Date today = Utils.getDayInMonth(new Date());
            if(entity == null) {
                Log.i(TAG, "There are no entries in Headline Table.");
                newHeadlines = (ArrayList<HeadlineEntity>) webAPI.getHeadlinesFromAPI(String.valueOf(today.getTime()), category);
            }
            else if (today.getTime() < entity.getmPublicationDate()) {
                Log.i(TAG, "The latest entry is " + entity.getmTitle() + ". time = " + (new Date(entity.getmPublicationDate())).toString());
                newHeadlines = (ArrayList<HeadlineEntity>) webAPI.getHeadlinesFromAPI(String.valueOf(entity.getmPublicationDate()), category);
            } else {
                Log.i(TAG, "There are no today's entries in Headline Table.");
                //データベースを全消し
                headlineDao.allClear();
                thumbnailDao.allClear();
                newHeadlines = (ArrayList<HeadlineEntity>) webAPI.getHeadlinesFromAPI(String.valueOf(today.getTime()), category);
            }

            //新規データをデータベースへ保存
            for (HeadlineEntity temp : newHeadlines) {
                headlineDao.insert(temp);
            }

            //データベースからデータを取得
            List<HeadlineEntity> entities = headlineDao.findLaterThanTime(today.getTime());
            for (HeadlineEntity entity1 : entities) {
                Headline temp = new Headline();
                temp.setmSysId(entity1.getmSysId());
                temp.setmTitle(entity1.getmTitle());
                temp.setmUrl(entity1.getmUrl());
                temp.setmPublicationDate(String.valueOf(entity1.getmPublicationDate()));
                temp.setmSummary(entity1.getmSummary());
                temp.setmSiteTitle(entity1.getmSiteTitle());
                temp.setmThumbnailUrl(entity1.getmThumbnailUrl());
                temp.setmCategory(entity1.getmCategory());
                temp.setIsRead(entity1.isRead());
                temp.setIsNew(entity1.isNew());

                //サムネがあれば格納する
                ThumbnailEntity thumb = thumbnailDao.findById(temp.getmSysId());
                if (thumb != null) {
                    temp.setmThumbnail(thumb.getmThumbnail());
                }

                results.add(temp);
            }
        } finally {
            db.close();
            dbThumb.close();
        }

        return results;
    }

    @Override
    public Headline thumbnail(Context context, Headline headline) throws IOException, JSONException {
        ThumbnailOpenHelper helper = new ThumbnailOpenHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            ThumbnailDao thumbnailDao = new ThumbnailDao(db);
            ThumbnailEntity entity = thumbnailDao.findById(headline.getmSysId());

            if (entity != null) {
                headline.setmThumbnail(entity.getmThumbnail());
            } else {
                WebAPI webAPI = new WebAPI();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bmp = null;
                bmp = webAPI.getThumbnail(headline.getmThumbnailFileName());

                //TODO nullじゃないなんか適当なデータをつっこんでるけどどうにかする
                byte[] bytes = {0};
                if (bmp == null) {
                    Log.e(TAG, "getThumbnail No:" + System.currentTimeMillis());
                    headline.setmThumbnail(bytes);
                } else {
                    Log.e(TAG, "getThumbnail End:" + System.currentTimeMillis());
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    bytes = baos.toByteArray();
                    headline.setmThumbnail(bytes);
                }

                ThumbnailEntity temp = new ThumbnailEntity();
                temp.setmSysId(headline.getmSysId());
                temp.setmThumbnail(bytes);
                thumbnailDao.insert(temp);
            }
        } finally {
            db.close();
        }

        return headline;
    }
}
