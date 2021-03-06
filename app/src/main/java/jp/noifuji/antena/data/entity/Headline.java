package jp.noifuji.antena.data.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ryoma on 2015/10/24.
 */
public class Headline {
    private static final String TAG = "Headline";
    private String mSysId = "";
    private String mTitle = "";
    private String mUrl = "";
    private String mPublicationDate = "";
    private String mSiteTitle = "";
    private String mSummary = "";
    private String mThumbnailUrl = "";
    private byte[] mThumbnail;
    private String mCategory = "";
    private boolean isRead = false;
    private boolean isNew = true;

    public Headline() {
    }


    public String getmSysId() {
        return mSysId;
    }

    public void setmSysId(String mSysId) {
        this.mSysId = mSysId;
    }
    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmPublicationDate() {
        return mPublicationDate;
    }

    public String getFormedPublicationDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(mPublicationDate)));
    }

    public void setmPublicationDate(String mPublicationDate) {
        this.mPublicationDate = mPublicationDate;
    }

    public String getTwitterLikeDate() {
        Date now = new Date();
        Date publication = new Date(Long.valueOf(mPublicationDate));
        long diff = now.getTime() - publication.getTime();
        String result = "";

        if(diff < 1000*60*60) {
            result = diff/(1000*60) + "分前";
        } else if(diff >= 1000*60 && diff < 1000*60*60*24) {
            result = diff/(1000*60*60) + "時間前";
        } else {
            result = getFormedPublicationDate("MM/dd HH:mm");
        }

        return result;
    }

    public String getmSiteTitle() {
        return mSiteTitle;
    }

    public void setmSiteTitle(String mSiteTitle) {
        this.mSiteTitle = mSiteTitle;
    }

    public String getmSummary() {
        return mSummary;
    }

    public void setmSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public String getmThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setmThumbnailUrl(String mThumbnailUrl) {
        this.mThumbnailUrl = mThumbnailUrl;
    }


    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public byte[] getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(byte[] mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getmThumbnailFileName() {
        String filename;
        String[] url = mThumbnailUrl.split("/");
        filename = url[url.length-1];
        String[] temp = filename.split("\\.");
        return temp[0];
    }
}
