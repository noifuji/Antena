package jp.noifuji.antena.domain.usecase;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;

import jp.noifuji.antena.data.entity.Headline;
import jp.noifuji.antena.data.repository.HeadlineRepository;

/**
 * Created by ryoma on 2015/12/10.
 */
public class GetHeadlineThumbnailUseCase  extends AsyncTaskLoader<AsyncResult<Headline>> implements LoaderManager.LoaderCallbacks<AsyncResult<Headline>> {
    private static final String TAG = "GetHeadlineThumbnail";
    private Loader mLoader;
    private HeadlineRepository mHeadlineRepository;
    private Context mContext;
    private Headline mHeadline;
    private GetHeadlineThumbnailUseCaseListener mUseCaseListener;
    private int mViewTag;

    public GetHeadlineThumbnailUseCase(Context context, HeadlineRepository headlineRepository, Headline headline, int viewTag) {
        super(context);
        this.mHeadlineRepository = headlineRepository;
        mContext = context;
        mHeadline = headline;
        mViewTag = viewTag;
    }

    @Override
    public AsyncResult<Headline> loadInBackground() {
        AsyncResult<Headline> result = new AsyncResult();
        Headline headline = null;
        try {
            headline = mHeadlineRepository.headlines().thumbnail(mContext, mHeadline);
            Log.d(TAG, "loadInBackground() : " + headline.getmTitle());
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
        if (data.getException() != null) {
            //Presenterへのエラー通知を行う
            if(mUseCaseListener != null) {
                mUseCaseListener.onGetHeadlineThumbnailUseCaseError(data.getErrorMessage());
            }
            return;
        }
        Log.d(TAG, "onLoadFinished() : " + data.getData().getmTitle());
        Bitmap bmp = null;
        /*byte[] bytes = data.getData().getmThumbnail(); //ここに画像データが入っているものとする
        if (bytes != null) {
            if(bytes.length == 1) {
                mImageView.setVisibility(View.INVISIBLE);
            } else {
                Log.d(TAG, "thumbnail size : " + bytes.length);
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                mImageView.setVisibility(View.VISIBLE);
                mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                mImageView.setImageBitmap(bmp);
                mImageView.setColorFilter(mContext.getResources().getColor(R.color.transparent));
            }
        } else {
            mImageView.setVisibility(View.VISIBLE);
            mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_thumbnail));
            mImageView.setColorFilter(mContext.getResources().getColor(R.color.ripple));
        }*/
        mUseCaseListener.onGetHeadlineThumbnailUseCaseCompleted(data.getData(), mViewTag);
    }

    @Override
    public void onLoaderReset(Loader<AsyncResult<Headline>> loader) {

    }

    public void execute(LoaderManager lm) {
        Bundle data = new Bundle();
        mLoader = lm.restartLoader(hashCode(), data, this);
        mLoader.forceLoad();
    }

    /**
     * このクラスから通知を受け取るクラスを登録します。
     * @param listener リスナとして登録するクラスのインスタンス
     */
    public void addListener(GetHeadlineThumbnailUseCaseListener listener) {
        this.mUseCaseListener = listener;
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
        void onGetHeadlineThumbnailUseCaseCompleted(Headline headline, int position);
    }
}
