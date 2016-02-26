package jp.noifuji.antena.data.repository.datastore;

import java.util.List;

import jp.noifuji.antena.data.db.HeadlineEntity;
import jp.noifuji.antena.data.entity.Headline;

/**
 * Created by ryoma on 2016/02/25.
 */
public interface HeadlineDataStore {
    List<HeadlineEntity> getNewerHeadlineList(Headline headline);
    List<HeadlineEntity> getAllHeadlineList();
    byte[] getThumbnailByHeadline(Headline headline);
}
