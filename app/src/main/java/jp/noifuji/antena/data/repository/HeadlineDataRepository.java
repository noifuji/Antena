package jp.noifuji.antena.data.repository;

import android.content.Context;

import java.util.List;

import jp.noifuji.antena.data.db.HeadlineEntity;
import jp.noifuji.antena.data.db.ThumbnailEntity;
import jp.noifuji.antena.data.entity.Headline;
import jp.noifuji.antena.data.mapper.HeadlineEntityMapper;
import jp.noifuji.antena.data.repository.datastore.HeadlineDataStore;
import jp.noifuji.antena.data.repository.datastore.HeadlineStorageDataStore;
import jp.noifuji.antena.data.repository.datastore.HeadlineWebDataStore;
import jp.noifuji.antena.domain.repository.HeadlineRepository;

/**
 * データの取得方法を選択する。
 * Created by ryoma on 2016/02/25.
 */
public class HeadlineDataRepository implements HeadlineRepository {

    private Context mContext;

    public HeadlineDataRepository(Context context) {
        mContext = context;
    }

    @Override
    public List<Headline> getNewerHeadlines(Headline headline) {
        HeadlineDataStore webDataStore = new HeadlineWebDataStore();
        HeadlineEntityMapper mapper = new HeadlineEntityMapper();
        List<Headline> results;

        List<HeadlineEntity> headlineEntities = webDataStore.getNewerHeadlineList(headline);
        results = mapper.transform(headlineEntities);

        //TODO:キャッシュへの登録を行う WebDataStoreで処理する、
        HeadlineDataStore storageDataStore = new HeadlineStorageDataStore(mContext);
        for(HeadlineEntity e : headlineEntities) {
            storageDataStore.insertHeadline(e);
        }

        return results;
    }

    @Override
    public List<Headline> getAllHeadlines() {
        HeadlineDataStore storageDataStore = new HeadlineStorageDataStore(mContext);
        List<HeadlineEntity> entitiesInStorage = storageDataStore.getAllHeadlineList();
        HeadlineEntityMapper mapper = new HeadlineEntityMapper();
        Headline temp = null;

        if(entitiesInStorage.size() > 0) {
            //ローカルストレージ内で最新の記事情報を取得する。
            temp = mapper.transform(entitiesInStorage.get(0));
        }
        //より新しい記事をWEBから取得する。
        List<Headline> results = getNewerHeadlines(temp);

        for (HeadlineEntity entity : entitiesInStorage) {
            results.add(mapper.transform(entity));
        }
        return results;
    }

    @Override
    public Headline getThumbnailByHeadline(Headline headline) {
        HeadlineDataStore webDataStore = new HeadlineWebDataStore();
        HeadlineDataStore storageDataStore = new HeadlineStorageDataStore(mContext);
        byte[] bytes = storageDataStore.getThumbnailByHeadline(headline);
        if(bytes != null) {
            headline.setmThumbnail(bytes);
            return headline;
        }

        bytes = webDataStore.getThumbnailByHeadline(headline);
        headline.setmThumbnail(bytes);

        //ローカルストレージへ保存する。
        ThumbnailEntity thumbnailEntity = new ThumbnailEntity();
        thumbnailEntity.setmSysId(headline.getmSysId());
        thumbnailEntity.setmThumbnail(bytes);
        storageDataStore.insertThumbnail(thumbnailEntity);

        return headline;
    }
}
