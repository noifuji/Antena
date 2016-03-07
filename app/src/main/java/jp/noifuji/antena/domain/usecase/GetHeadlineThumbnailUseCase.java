package jp.noifuji.antena.domain.usecase;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

import jp.noifuji.antena.data.entity.Headline;
import jp.noifuji.antena.domain.repository.HeadlineRepository;

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
        headline = mHeadlineRepository.getThumbnailByHeadline(mHeadline);
        Log.d(TAG, "loadInBackground() : " + headline.getmTitle());
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
