package jp.noifuji.antena.data.db;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import org.junit.Test;

/**
 * Created by Ryoma on 2015/12/23.
 */
public class ThumbnailDaoTest extends AndroidTestCase {
    private ThumbnailOpenHelper helper;

    public void setUp() {
        helper = new ThumbnailOpenHelper(new RenamingDelegatingContext(getContext(), "test_"));
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ThumbnailDao thumbnailDao = new ThumbnailDao(db);
            ThumbnailEntity thumbnail = new ThumbnailEntity();
            thumbnail.setmSysId("12345678");
            byte[] bytes = {1,2,3,4};
            thumbnail.setmThumbnail(bytes);
            thumbnailDao.insert(thumbnail);
        } finally {
            db.close();
        }
    }

    public void tearDown() {
        helper.close();
    }

    @Test
    public void testFindById() throws Exception {
        SQLiteDatabase db = helper.getReadableDatabase();

        try {
            ThumbnailDao thumbnailDao = new ThumbnailDao(db);
            ThumbnailEntity entity = thumbnailDao.findById("12345678");

            byte[] bytes = {1,2,3,4};
            assertEquals("データが一致する", bytes[0], entity.getmThumbnail()[0]);
        } finally {
            db.close();
        }
    }

    @Test
    public void testInsert() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }
}