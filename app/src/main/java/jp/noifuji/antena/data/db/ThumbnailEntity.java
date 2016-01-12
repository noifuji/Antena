package jp.noifuji.antena.data.db;

/**
 * Created by Ryoma on 2015/12/23.
 */
public class ThumbnailEntity {
    private String mSysId = "";
    private byte[] mThumbnail = null;

    public String getmSysId() {
        return mSysId;
    }

    public void setmSysId(String mSysId) {
        this.mSysId = mSysId;
    }

    public byte[] getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(byte[] mThumbnail) {
        this.mThumbnail = mThumbnail;
    }
}
