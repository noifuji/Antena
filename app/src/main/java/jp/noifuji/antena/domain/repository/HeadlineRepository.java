package jp.noifuji.antena.domain.repository;

import java.util.List;

import jp.noifuji.antena.data.entity.Headline;

/**
 * Created by ryoma on 2016/02/25.
 */
public interface HeadlineRepository {
    List<Headline> getNewerHeadlines(Headline headline);
    List<Headline> getAllHeadlines();
    Headline getThumbnailByHeadline(Headline headline);
}
