package jp.noifuji.antena.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ryoma on 2015/12/18.
 */
public class ThumbnailDao {
    // テーブルの定数
    private static final String TABLE_NAME = "thumbnail";
    private static final String COLUMN_SYS_ID = "sys_id";
    private static final String COLUMN_THUMBNAIL = "data";
    private static final String[] COLUMNS = {COLUMN_SYS_ID, COLUMN_THUMBNAIL};

    // SQLiteDatabase
    private SQLiteDatabase db;

    /**
     * コンストラクタ
     *
     * @param db
     */
    public ThumbnailDao(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 特定IDのデータを取得   ----------------②
     *
     * @param sysId
     * @return
     */
    public ThumbnailEntity findById(String sysId) {
        String selection = COLUMN_SYS_ID + "='" + sysId + "'";
        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMNS,
                selection,
                null,
                null,
                null,
                null);

        ThumbnailEntity entity = new ThumbnailEntity();
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            entity.setmSysId(cursor.getString(0));
            entity.setmThumbnail(cursor.getBlob(1));
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
    public long insert(ThumbnailEntity entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMNS[0], entity.getmSysId());
        values.put(COLUMNS[1], entity.getmThumbnail());
        return db.insert(TABLE_NAME, null, values);
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

    /**
     * 全てのデータの削除   ----------------⑤
     *
     * @return
     */
    public int allClear() {
        return db.delete(TABLE_NAME, null, null);
    }
}
