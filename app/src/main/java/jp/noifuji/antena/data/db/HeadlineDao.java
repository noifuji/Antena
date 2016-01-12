package jp.noifuji.antena.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryoma on 2015/12/18.
 */
public class HeadlineDao {
    // テーブルの定数
    private static final String TABLE_NAME = "headline";
    private static final String COLUMN_SYS_ID = "sys_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_PUBLICATION_DATE = "pub_date";
    private static final String COLUMN_SITE_TITLE = "site_title";
    private static final String COLUMN_SUMMARY = "summary";
    private static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_IS_READ = "is_read";
    private static final String COLUMN_IS_NEW = "is_new";
    private static final String[] COLUMNS = {COLUMN_SYS_ID, COLUMN_TITLE, COLUMN_URL
            , COLUMN_PUBLICATION_DATE, COLUMN_SITE_TITLE, COLUMN_SUMMARY
            , COLUMN_THUMBNAIL_URL, COLUMN_CATEGORY, COLUMN_IS_READ, COLUMN_IS_NEW};
    private static final String[] COLUMNS_LATEST_PUB = {COLUMN_SYS_ID, COLUMN_TITLE, COLUMN_URL
            , "MAX(" + COLUMN_PUBLICATION_DATE + ")", COLUMN_SITE_TITLE, COLUMN_SUMMARY
            , COLUMN_THUMBNAIL_URL, COLUMN_CATEGORY, COLUMN_IS_READ, COLUMN_IS_NEW};

    // SQLiteDatabase
    private SQLiteDatabase db;

    /**
     * コンストラクタ
     *
     * @param db
     */
    public HeadlineDao(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 全データの取得   ----------------①
     *
     * @return
     */
    public List<HeadlineEntity> findAll() {
        List<HeadlineEntity> entityList = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMNS,
                null,
                null,
                null,
                null,
                COLUMN_PUBLICATION_DATE + " desc");

        while (cursor.moveToNext()) {
            HeadlineEntity entity = new HeadlineEntity();
            entity.setmSysId(cursor.getString(0));
            entity.setmTitle(cursor.getString(1));
            entity.setmUrl(cursor.getString(2));
            entity.setmPublicationDate(cursor.getLong(3));
            entity.setmSiteTitle(cursor.getString(4));
            entity.setmSummary(cursor.getString(5));
            entity.setmThumbnailUrl(cursor.getString(6));
            entity.setmCategory(cursor.getString(7));
            entity.setIsRead(cursor.getInt(8) == 0 ? true : false);
            entity.setIsNew(cursor.getInt(9) == 0 ? true : false);
            entityList.add(entity);
        }

        return entityList;
    }

    /**
     * 指定した時刻以降の記事を取得する
     *
     * @return
     */
    public List<HeadlineEntity> findLaterThanTime(long time) {
        String selection = COLUMN_PUBLICATION_DATE + ">" + time;
        List<HeadlineEntity> entityList = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMNS,
                selection,
                null,
                null,
                null,
                COLUMN_PUBLICATION_DATE + " desc");

        while (cursor.moveToNext()) {
            HeadlineEntity entity = new HeadlineEntity();
            entity.setmSysId(cursor.getString(0));
            entity.setmTitle(cursor.getString(1));
            entity.setmUrl(cursor.getString(2));
            entity.setmPublicationDate(cursor.getLong(3));
            entity.setmSiteTitle(cursor.getString(4));
            entity.setmSummary(cursor.getString(5));
            entity.setmThumbnailUrl(cursor.getString(6));
            entity.setmCategory(cursor.getString(7));
            entity.setIsRead(cursor.getInt(8) == 0 ? true : false);
            entity.setIsNew(cursor.getInt(9) == 0 ? true : false);
            entityList.add(entity);
        }

        return entityList;
    }

    /**
     * 指定した時刻以降の記事を取得する
     * 結果がなければnullを返す。
     * @return
     */
    public HeadlineEntity findLatestPublicationDate() {
        HeadlineEntity entity = new HeadlineEntity();
        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMNS,
                null,
                null,
                null,
                null,
                COLUMN_PUBLICATION_DATE + " desc");

        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            entity.setmSysId(cursor.getString(0));
            entity.setmTitle(cursor.getString(1));
            entity.setmUrl(cursor.getString(2));
            entity.setmPublicationDate(cursor.getLong(3));
            entity.setmSiteTitle(cursor.getString(4));
            entity.setmSummary(cursor.getString(5));
            entity.setmThumbnailUrl(cursor.getString(6));
            entity.setmCategory(cursor.getString(7));
            entity.setIsRead(cursor.getInt(8) == 0 ? true : false);
            entity.setIsNew(cursor.getInt(9) == 0 ? true : false);
        } else {
            return null;
        }

        return entity;
    }

    /**
     * 特定IDのデータを取得   ----------------②
     *
     * @param sysId
     * @return
     */
    public HeadlineEntity findById(String sysId) {
        String selection = COLUMN_SYS_ID + "='" + sysId + "'";
        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMNS,
                selection,
                null,
                null,
                null,
                null);

        HeadlineEntity entity = new HeadlineEntity();
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            entity.setmSysId(cursor.getString(0));
            entity.setmTitle(cursor.getString(1));
            entity.setmUrl(cursor.getString(2));
            entity.setmPublicationDate(cursor.getLong(3));
            entity.setmSiteTitle(cursor.getString(4));
            entity.setmSummary(cursor.getString(5));
            entity.setmThumbnailUrl(cursor.getString(6));
            entity.setmCategory(cursor.getString(7));
            entity.setIsRead(cursor.getInt(8) == 0 ? true : false);
            entity.setIsNew(cursor.getInt(9) == 0 ? true : false);
        } else {
            return null;
        }

        return entity;
    }

    /**
     * データの登録   ----------------③
     *
     * @param entity
     * @return
     */
    public long insert(HeadlineEntity entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMNS[0], entity.getmSysId());
        values.put(COLUMNS[1], entity.getmTitle());
        values.put(COLUMNS[2], entity.getmUrl());
        values.put(COLUMNS[3], entity.getmPublicationDate());
        values.put(COLUMNS[4], entity.getmSiteTitle());
        values.put(COLUMNS[5], entity.getmSummary());
        values.put(COLUMNS[6], entity.getmThumbnailUrl());
        values.put(COLUMNS[7], entity.getmCategory());
        values.put(COLUMNS[8], entity.isRead() ? 0 : 1); //booleanは格納できないため0,1に変換する
        values.put(COLUMNS[9], entity.isNew() ? 0 : 1); //booleanは格納できないため0,1に変換する
        return db.insert(TABLE_NAME, null, values);
    }

    /**
     * データの更新   ----------------④
     *
     * @param entity
     * @return
     */
    public int update(HeadlineEntity entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMNS[0], entity.getmSysId());
        values.put(COLUMNS[1], entity.getmTitle());
        values.put(COLUMNS[2], entity.getmUrl());
        values.put(COLUMNS[3], entity.getmPublicationDate());
        values.put(COLUMNS[4], entity.getmSiteTitle());
        values.put(COLUMNS[5], entity.getmSummary());
        values.put(COLUMNS[6], entity.getmThumbnailUrl());
        values.put(COLUMNS[7], entity.getmCategory());
        values.put(COLUMNS[8], entity.isRead() ? 0 : 1); //booleanは格納できないため0,1に変換する
        values.put(COLUMNS[9], entity.isNew() ? 0 : 1); //booleanは格納できないため0,1に変換する
        String whereClause = COLUMN_SYS_ID + "='" + entity.getmSysId() + "'";
        return db.update(TABLE_NAME, values, whereClause, null);
    }

    /**
     * データの削除   ----------------⑤
     *
     * @param sysId
     * @return
     */
    public int delete(String sysId) {
        String whereClause = COLUMN_SYS_ID + "='" + sysId + "'";
        return db.delete(TABLE_NAME, whereClause, null);
    }
}
