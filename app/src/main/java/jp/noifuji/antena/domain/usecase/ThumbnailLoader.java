package jp.noifuji.antena.domain.usecase;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.IOException;

import jp.noifuji.antena.R;
import jp.noifuji.antena.data.entity.Headline;
import jp.noifuji.antena.data.repository.HeadlineRepository;
import jp.noifuji.antena.data.repository.HeadlineRepositoryImpl;

/**
 * Created by ryoma on 2015/12/10.
 */
public class ThumbnailLoader extends AsyncTaskLoader<AsyncResult<Headline>> implements LoaderManager.LoaderCallbacks<AsyncResult<Headline>> {
    private static final String TAG = "ThumbnailLoader";
    private static final int LOADER_ID = 2;
    private Loader mLoader;
    private Context mContext;
    private Headline mHeadline;
    private ImageView mImageView;

    public ThumbnailLoader(Context context, Headline headline, ImageView imageView) {
        super(context);
        mContext = context;
        mHeadline = headline;
        mImageView = imageView;
    }

    @Override
    public AsyncResult<Headline> loadInBackground() {
        AsyncResult<Headline> result = new AsyncResult();
        Headline headline = null;
        try {
            HeadlineRepository repo = new HeadlineRepositoryImpl();
            headline = repo.headlines().thumbnail(mContext, mHeadline);
        } catch (IOException e) {
            e.printStackTrace();
            //TODO メッセージを設定する
            result.setException(e, "");
        } catch (JSONException e) {
            e.printStackTrace();
            result.setException(e, "");
        }
        result.setData(headline);
        return result;
    }

    @Override
    public Loader<AsyncResult<Headline>> onCreateLoader(int i, Bundle bundle) {
        return this;
    }

    @Override
    public void onLoadFinished(Loader<AsyncResult<Headline>> loader, AsyncResult<Headline> data) {
        Exception exception = data.getException();
        if (exception != null) {
            Log.e(TAG, data.getErrorMessage());
            return;
        }

        Bitmap bmp = BitmapFactory.decodeByteArray(data.getData().getmThumbnail(), 0, data.getData().getmThumbnail().length);
        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mImageView.setImageBitmap(bmp);
        mImageView.setColorFilter(mContext.getResources().getColor(R.color.transparent));
    }

    @Override
    public void onLoaderReset(Loader<AsyncResult<Headline>> loader) {

    }

    public void execute(LoaderManager lm) {
        Bundle data = new Bundle();
        mLoader = lm.restartLoader(LOADER_ID, data, this);
        mLoader.forceLoad();
    }

    /**
     * このクラスの通知を受け取るクラスはこのインターフェースを実装する。
     */
    public interface GetHeadlineThumbnailUseCaseListener {
        /**
         * 記事のサムネイル取得に失敗した場合に呼び出されます。
         * @param errorMessage
         */
        void onGetHeadlineThumbnailUseCaseError(String errorMessage);

        /**
         * 記事のサムネイル取得が完了した場合に呼び出されます。
         * @param headline モデルが保持しているヘッドライン情報
         *
         */
        void onGetHeadlineThumbnailUseCaseCompleted(Headline headline);
    }
}
