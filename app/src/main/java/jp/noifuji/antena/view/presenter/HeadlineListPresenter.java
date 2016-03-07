package jp.noifuji.antena.view.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Collections;
import java.util.List;

import jp.noifuji.antena.R;
import jp.noifuji.antena.data.entity.Headline;
import jp.noifuji.antena.data.entity.HeadlineComparator;
import jp.noifuji.antena.data.repository.HeadlineDataRepository;
import jp.noifuji.antena.domain.usecase.GetHeadlineListUseCase;
import jp.noifuji.antena.domain.usecase.GetHeadlineThumbnailUseCase;
import jp.noifuji.antena.view.fragment.HeadLineListFragment;

/**
 * Created by ryoma on 2015/11/19.
 */
public class HeadlineListPresenter implements Presenter, GetHeadlineListUseCase.GetHeadlineListUseCaseListener, GetHeadlineThumbnailUseCase.GetHeadlineThumbnailUseCaseListener {

    private static final String TAG = "HeadlineListPresenter";
    private static HeadlineListPresenter instance = new HeadlineListPresenter();
    private HeadLineListFragment mHeadlineListFragment;//@フラグメントが死んだときやばくね??
    private GetHeadlineListUseCase getHeadlineListUseCase;//TODO 名前変えろ

    private HeadlineListPresenter() {
    }

    public static HeadlineListPresenter getInstance() {
        return instance;
    }

    /*Fragmentからコールされるメソッド*/
    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
    }

    public void setFragment(HeadLineListFragment fragment) {
        this.mHeadlineListFragment = fragment;
    }

    public void loadHeadlineList(String category) {
        this.showViewLoading();
        this.getHeadlineList(category);
    }

    public void refreshHeadlineList(String category) {
        this.getHeadlineList(category);
    }

    public void onHeadlineClicked(Headline headline, int position) {
        //headline.setIsRead(true);
        //this.mHeadlineListFragment.readHeadline(position);//既読機能
        this.mHeadlineListFragment.viewEntry(headline);
    }

    public void getThumbnailImage(Headline headline, ImageView imageView) {
        if (headline.getmThumbnail() != null) {
            Log.d(TAG, "Thumbnail has been loaded or doesn't exist.");
            byte[] bytes = headline.getmThumbnail();
            if(bytes.length == 1) {
                imageView.setVisibility(View.INVISIBLE);
            } else {
                Log.d(TAG, "thumbnail size : " + bytes.length);
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setVisibility(View.VISIBLE);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setImageBitmap(bmp);
                imageView.setColorFilter(mHeadlineListFragment.getActivity().getApplicationContext().getResources().getColor(R.color.transparent));
            }
            return;
        }
        Log.d(TAG, "image view tag is " + imageView.getTag() + "[" + headline.getmTitle() + "]");
        GetHeadlineThumbnailUseCase getHeadlineThumbnailUseCase = new GetHeadlineThumbnailUseCase(this.mHeadlineListFragment.getActivity(), new HeadlineDataRepository(mHeadlineListFragment.getActivity()), headline, (Integer) imageView.getTag());
        getHeadlineThumbnailUseCase.addListener(this);
        getHeadlineThumbnailUseCase.execute(this.mHeadlineListFragment.getLoaderManager());
    }


    /*Fragmentをコールするメソッド*/

    private void showViewLoading() {
        this.mHeadlineListFragment.showLoading();
    }

    private void hideViewLoading() {
        this.mHeadlineListFragment.hideLoading();
    }

    private void hideViewRefreshing() {
        this.mHeadlineListFragment.hideRefreshing();
    }

    private void getHeadlineList(String category) {
        Log.e(TAG, "StartRefresh:" + System.currentTimeMillis());
        //ヘッドラインのリストを取得する。
        //getHeadlineListUseCase = new GetHeadlineListUseCase(this.mHeadlineListFragment.getActivity(), new HeadlineRepositoryImpl(), category);
        getHeadlineListUseCase = new GetHeadlineListUseCase(this.mHeadlineListFragment.getActivity(), new HeadlineDataRepository(mHeadlineListFragment.getActivity()), category);
        getHeadlineListUseCase.addListener(this);
        getHeadlineListUseCase.execute(this.mHeadlineListFragment.getLoaderManager());
    }

    @Override
    public void onGetHeadlineListUseCaseError(String errorMessage) {
        this.hideViewLoading();
        //リスト取得時のエラーを通知
        mHeadlineListFragment.showError(errorMessage);
    }

    @Override
    public void onGetHeadlineListUseCaseCompleted(List<Headline> headlineList) {
        if (headlineList == null) {
            return;
        }

        this.hideViewLoading();
        this.hideViewRefreshing();
        mHeadlineListFragment.renderHeadlineList(headlineList);
        if (headlineList.size() > 0) {
            this.mHeadlineListFragment.setNewestHeadline(Collections.max(headlineList, new HeadlineComparator()));
        }
    }

    @Override
    public void onGetHeadlineThumbnailUseCaseError(String errorMessage) {
        //サムネ取得時のエラーを通知
        mHeadlineListFragment.showError(errorMessage);
    }

    @Override
    public void onGetHeadlineThumbnailUseCaseCompleted(Headline headline, int position) {
        if (headline == null) {
            return;
        }

        this.mHeadlineListFragment.onReceivedThumbnail(headline, position);
    }
}
