package jp.noifuji.antena.data.mapper;

import java.util.ArrayList;
import java.util.List;

import jp.noifuji.antena.data.db.HeadlineEntity;
import jp.noifuji.antena.data.entity.Headline;

/**
 * Created by ryoma on 2016/03/07.
 */
public class HeadlineEntityMapper {

    public HeadlineEntityMapper(){}
    
    public Headline transform(HeadlineEntity headlineEntity) {
        Headline headline = new Headline();
        headline.setmSysId(headlineEntity.getmSysId());
        headline.setmTitle(headlineEntity.getmTitle());
        headline.setmUrl(headlineEntity.getmUrl());
        headline.setmPublicationDate(String.valueOf(headlineEntity.getmPublicationDate()));
        headline.setmSummary(headlineEntity.getmSummary());
        headline.setmSiteTitle(headlineEntity.getmSiteTitle());
        headline.setmThumbnailUrl(headlineEntity.getmThumbnailUrl());
        headline.setmCategory(headlineEntity.getmCategory());
        headline.setIsRead(headlineEntity.isRead());
        headline.setIsNew(headlineEntity.isNew());
        return headline;
    }

    public List<Headline> transform(List<HeadlineEntity> headlineEntityList) {
        List<Headline> headlineList = new ArrayList<>();

        for(HeadlineEntity he : headlineEntityList) {
            headlineList.add(transform(he));
        }
        return headlineList;
    }
}
