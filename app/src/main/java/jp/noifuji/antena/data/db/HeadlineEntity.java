package jp.noifuji.antena.data.db;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ryoma on 2015/12/22.
 */
public class HeadlineEntity {
    private static final String SYS_ID = "_id";
    private static final String TITLE = "title";
    private static final String PUBLICATIONDATE = "publicationDate";
    private static final String SITETITLE = "sitetitle";
    private static final String SUMMARY = "summary";
    private static final String URL = "url";
    private static final String THUMBNAIL = "thumbnail";
    private static final String CATEGORY = "category";

    private String mSysId = "";
    private String mTitle = "";
    private String mUrl = "";
    private long mPublicationDate = 0;
    private String mSiteTitle = "";
    private String mSummary = "";
    private String mThumbnailUrl = "";
    private String mCategory = "";
    private boolean isRead = false;
    private boolean isNew = true;

    public HeadlineEntity(){}

    /**
     *
     * @param json
     * @throws JSONException title, publicationDate, sitetitle, summary, url, thumbnailのいずれかががない場合
     */
    public HeadlineEntity(JSONObject json) throws JSONException {
        setmSysId(json.getString(SYS_ID));
        setmTitle(json.getString(TITLE));
        setmPublicationDate(Long.valueOf(json.getString(PUBLICATIONDATE)));
        setmSiteTitle(json.getString(SITETITLE));
        setmSummary(json.getString(SUMMARY));
        setmUrl(json.getString(URL));
        setmThumbnailUrl(json.getString(THUMBNAIL));
        setmCategory(json.getString(CATEGORY));
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

    public long getmPublicationDate() {
        return mPublicationDate;
    }

    public void setmPublicationDate(long mPublicationDate) {
        this.mPublicationDate = mPublicationDate;
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

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
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
}
