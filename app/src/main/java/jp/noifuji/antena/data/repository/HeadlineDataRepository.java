package jp.noifuji.antena.data.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import jp.noifuji.antena.data.db.HeadlineEntity;
import jp.noifuji.antena.data.entity.Headline;
import jp.noifuji.antena.data.repository.datastore.HeadlineDataStore;
import jp.noifuji.antena.data.repository.datastore.HeadlineStorageDataStore;
import jp.noifuji.antena.data.repository.datastore.HeadlineWebDataStore;

/**
 * Created by ryoma on 2016/02/25.
 */
public class HeadlineDataRepository implements jp.noifuji.antena.domain.repository.HeadlineRepository {

    private Context mContext;

    public HeadlineDataRepository(Context context) {
        mContext = context;
    }
    @Override
    public List<Headline> getNewerHeadlines(Headline headline) {
        HeadlineDataStore webDataStore = new HeadlineWebDataStore();
        List<Headline> results = new ArrayList<>();

        List<HeadlineEntity> headlineEntities = webDataStore.getNewerHeadlineList(headline);
        for (HeadlineEntity entity : headlineEntities) {
            Headline temp = new Headline();
            temp.setmSysId(entity.getmSysId());
            temp.setmTitle(entity.getmTitle());
            temp.setmUrl(entity.getmUrl());
            temp.setmPublicationDate(String.valueOf(entity.getmPublicationDate()));
            temp.setmSummary(entity.getmSummary());
            temp.setmSiteTitle(entity.getmSiteTitle());
            temp.setmThumbnailUrl(entity.getmThumbnailUrl());
            temp.setmCategory(entity.getmCategory());
            temp.setIsRead(entity.isRead());
            temp.setIsNew(entity.isNew());

            results.add(temp);
        }
        //TODO:キャッシュへの登録を行う
        return results;
    }

    @Override
    public List<Headline> getAllHeadlines() {
        HeadlineDataStore webDataStore = new HeadlineWebDataStore();
        HeadlineDataStore storageDataStore = new HeadlineStorageDataStore(mContext);
        List<HeadlineEntity> entitiesInStorage = storageDataStore.getAllHeadlineList();
        //TODO:マッパーを用意する
        Headline temp = new Headline();
        temp.setmSysId(entitiesInStorage.get(0).getmSysId());
        temp.setmTitle(entitiesInStorage.get(0).getmTitle());
        temp.setmUrl(entitiesInStorage.get(0).getmUrl());
        temp.setmPublicationDate(String.valueOf(entitiesInStorage.get(0).getmPublicationDate()));
        temp.setmSummary(entitiesInStorage.get(0).getmSummary());
        temp.setmSiteTitle(entitiesInStorage.get(0).getmSiteTitle());
        temp.setmThumbnailUrl(entitiesInStorage.get(0).getmThumbnailUrl());
        temp.setmCategory(entitiesInStorage.get(0).getmCategory());
        temp.setIsRead(entitiesInStorage.get(0).isRead());
        temp.setIsNew(entitiesInStorage.get(0).isNew());

        List<Headline> results = getNewerHeadlines(temp);

        for (HeadlineEntity entity : entitiesInStorage) {
            Headline temp2 = new Headline();
            temp2.setmSysId(entity.getmSysId());
            temp2.setmTitle(entity.getmTitle());
            temp2.setmUrl(entity.getmUrl());
            temp2.setmPublicationDate(String.valueOf(entity.getmPublicationDate()));
            temp2.setmSummary(entity.getmSummary());
            temp2.setmSiteTitle(entity.getmSiteTitle());
            temp2.setmThumbnailUrl(entity.getmThumbnailUrl());
            temp2.setmCategory(entity.getmCategory());
            temp2.setIsRead(entity.isRead());
            temp2.setIsNew(entity.isNew());

            results.add(temp2);
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
        return headline;
    }
}
