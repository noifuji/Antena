package jp.noifuji.antena.data.repository.datastore;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.noifuji.antena.constants.Category;
import jp.noifuji.antena.data.db.HeadlineEntity;
import jp.noifuji.antena.data.entity.Headline;
import jp.noifuji.antena.data.net.WebAPI;

/**
 * Created by ryoma on 2016/02/25.
 */
public class HeadlineWebDataStore implements HeadlineDataStore {
    private static final String TAG = "HeadlineWebDataStore";

    @Override
    public List<HeadlineEntity> getNewerHeadlineList(Headline headline) {
        WebAPI webAPI = new WebAPI();
        ArrayList<HeadlineEntity> newHeadlines = null;
        try {
            newHeadlines = (ArrayList<HeadlineEntity>) webAPI.getHeadlinesFromAPI(String.valueOf(headline.getmPublicationDate()), Category.ALL);//TODO:カテゴリをセットする
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newHeadlines;
    }

    @Override
    public List<HeadlineEntity> getAllHeadlineList() {
        throw new UnsupportedOperationException("getAllHeadlineList() is not available.");
    }

    @Override
    public byte[] getThumbnailByHeadline(Headline headline) {
        WebAPI webAPI = new WebAPI();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bmp = null;
        //TODO nullじゃないなんか適当なデータをつっこんでるけどどうにかする
        byte[] bytes = {0};
        try {
            bmp = webAPI.getThumbnail(headline.getmThumbnailFileName());
            Log.d(TAG, "thumbnail downloaded:" + headline.getmThumbnailFileName());
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
