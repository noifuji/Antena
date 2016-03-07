package jp.noifuji.antena.data.repository.datastore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;

import jp.noifuji.antena.data.db.HeadlineDao;
import jp.noifuji.antena.data.db.HeadlineEntity;
import jp.noifuji.antena.data.db.HeadlineOpenHelper;
import jp.noifuji.antena.data.db.ThumbnailDao;
import jp.noifuji.antena.data.db.ThumbnailEntity;
import jp.noifuji.antena.data.db.ThumbnailOpenHelper;
import jp.noifuji.antena.data.entity.Headline;
import jp.noifuji.antena.util.Utils;

/**
 * Created by ryoma on 2016/02/25.
 */
public class HeadlineStorageDataStore implements HeadlineDataStore {
    private static final String TAG = "HeadlineStorageDataStore";
    private Context mContext;

    public HeadlineStorageDataStore(Context context) {
        mContext = context;
    }
    @Override
    public List<HeadlineEntity> getNewerHeadlineList(Headline headline) {
        throw new UnsupportedOperationException("getNewerHeadlineList(Headline headline) is not available.");
    }

    @Override
    public List<HeadlineEntity> getAllHeadlineList() {
        HeadlineOpenHelper helper = new HeadlineOpenHelper(mContext);
        SQLiteDatabase db = helper.getReadableDatabase();
        HeadlineDao headlineDao = new HeadlineDao(db);

        //データベースからデータを取得
        Date today = Utils.getDayInMonth(new Date());
        List<HeadlineEntity> entities = headlineDao.findLaterThanTime(today.getTime());
        //TODO:その日のうちの記事を取得→20件ずつなどにかえる
        //TODO:これらの処理はキャッシュクラスに移す キャッシュの定期削除もそこでおこなう
        db.close();

        return entities;
    }

    @Override
    public byte[] getThumbnailByHeadline(Headline headline) {
        ThumbnailOpenHelper helper = new ThumbnailOpenHelper(mContext);
        SQLiteDatabase db = helper.getReadableDatabase();
        ThumbnailDao thumbnailDao = new ThumbnailDao(db);
        ThumbnailEntity entity = thumbnailDao.findById(headline.getmSysId());
        byte[] bytes = null;
        if (entity != null) {
            bytes = entity.getmThumbnail();
        }
        return bytes;
    }

    @Override
    public void insertHeadline(HeadlineEntity entity) {
        HeadlineOpenHelper helper = new HeadlineOpenHelper(mContext);
        SQLiteDatabase db = helper.getReadableDatabase();
        HeadlineDao headlineDao = new HeadlineDao(db);
        headlineDao.insert(entity);
    }

    @Override
    public void insertThumbnail(ThumbnailEntity entity) {
        ThumbnailOpenHelper helper = new ThumbnailOpenHelper(mContext);
        SQLiteDatabase db = helper.getReadableDatabase();
        ThumbnailDao thumbnailDao = new ThumbnailDao(db);
        thumbnailDao.insert(entity);
    }
}
