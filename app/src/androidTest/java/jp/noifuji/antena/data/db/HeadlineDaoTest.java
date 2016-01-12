package jp.noifuji.antena.data.db;

import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import org.junit.Test;

import java.util.List;

/**
 * Created by Ryoma on 2015/12/22.
 */
public class HeadlineDaoTest extends AndroidTestCase {
    private HeadlineOpenHelper helper;

    public void setUp() {
        helper = new HeadlineOpenHelper(new RenamingDelegatingContext(getContext(), "test_"));
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.execSQL("insert into headline(sys_id, title, url, pub_date, site_title, summary, thumbnail_url, category, is_read, is_new) values ('34rrgf444', 'Sony', 'http://test.com/123.html', 100, 'himasoku', '概要A', 'http://image.test.com/123.png', 'VIP', 0, 0)");
            db.execSQL("insert into headline(sys_id, title, url, pub_date, site_title, summary, thumbnail_url, category, is_read, is_new) values ('34r45f444', 'Toshiba', 'http://test.com/143.html', 99, 'himasoku', '概要B', 'http://image.test.com/129.png', 'VIP', 1, 0)");
            db.execSQL("insert into headline(sys_id, title, url, pub_date, site_title, summary, thumbnail_url, category, is_read, is_new) values ('34rrgfr44', 'Fujitsu', 'http://test.com/125.html', 98, 'himasoku', '概要C', 'http://image.test.com/189.png', 'VIP', 0, 0)");
            db.execSQL("insert into headline(sys_id, title, url, pub_date, site_title, summary, thumbnail_url, category, is_read, is_new) values ('34uifhndk', 'Hitachi', 'http://test.com/126.html', 45, 'himasoku', '概要D', 'http://image.test.com/89.png', 'VIP', 0, 0)");
        } finally {
            db.close();
        }
    }

    public void tearDown() {
        helper.close();
    }

    @Test
    public void testFindAll() throws Exception {
        SQLiteDatabase db = helper.getReadableDatabase();

        try {
            HeadlineDao headlineDao = new HeadlineDao(db);
            List<HeadlineEntity> entities = headlineDao.findAll();

            assertEquals("検索対象件数が一致すること", 4, entities.size());

            HeadlineEntity first = entities.get(0);
            assertEquals("先頭のIDが一致すること",       "34rrgf444", first.getmSysId());
            assertEquals("先頭のタイトルが一致すること", "Sony", first.getmTitle());
            assertEquals("先頭のURLが一致すること",      "http://test.com/123.html", first.getmUrl());
            assertEquals("先頭の発行日時が一致すること", 100, first.getmPublicationDate());
            assertEquals("先頭のサイト名が一致すること", "himasoku", first.getmSiteTitle());
            assertEquals("先頭のサマリーが一致すること", "概要A", first.getmSummary());
            assertEquals("先頭のサムネイルURLが一致すること", "http://image.test.com/123.png", first.getmThumbnailUrl());
            assertEquals("先頭のカテゴリ一致すること",    "VIP", first.getmCategory());
            assertEquals("先頭の既読状態が一致すること",  true, first.isRead());
            assertEquals("先頭の記事の新旧が一致すること", true, first.isNew());

            HeadlineEntity second = entities.get(1);
            assertEquals("2 番目のタイトルが一致すること", "Toshiba", second.getmTitle());
        } finally {
            db.close();
        }
    }

    @Test
    public void testFindLaterThanTime() throws Exception {
        SQLiteDatabase db = helper.getReadableDatabase();

        try {
            HeadlineDao headlineDao = new HeadlineDao(db);
            List<HeadlineEntity> entities = headlineDao.findLaterThanTime(98);

            assertEquals("検索対象件数が一致すること", 2, entities.size());

            HeadlineEntity first = entities.get(0);
            assertEquals("先頭のIDが一致すること",       "34rrgf444", first.getmSysId());
            assertEquals("先頭のタイトルが一致すること", "Sony", first.getmTitle());
            assertEquals("先頭のURLが一致すること",      "http://test.com/123.html", first.getmUrl());
            assertEquals("先頭の発行日時が一致すること", 100, first.getmPublicationDate());
            assertEquals("先頭のサイト名が一致すること", "himasoku", first.getmSiteTitle());
            assertEquals("先頭のサマリーが一致すること", "概要A", first.getmSummary());
            assertEquals("先頭のサムネイルURLが一致すること", "http://image.test.com/123.png", first.getmThumbnailUrl());
            assertEquals("先頭のカテゴリ一致すること",    "VIP", first.getmCategory());
            assertEquals("先頭の既読状態が一致すること",  true, first.isRead());
            assertEquals("先頭の記事の新旧が一致すること", true, first.isNew());

            HeadlineEntity second = entities.get(1);
            assertEquals("2 番目のタイトルが一致すること", "Toshiba", second.getmTitle());
        } finally {
            db.close();
        }
    }

    @Test
    public void testFindLatestPublicationDate() throws Exception {
        SQLiteDatabase db = helper.getReadableDatabase();

        try {
            HeadlineDao headlineDao = new HeadlineDao(db);
            HeadlineEntity entity = headlineDao.findLatestPublicationDate();


            assertEquals("先頭のIDが一致すること",       "34rrgf444", entity.getmSysId());
            assertEquals("先頭のタイトルが一致すること", "Sony", entity.getmTitle());
            assertEquals("先頭のURLが一致すること",      "http://test.com/123.html", entity.getmUrl());
            assertEquals("先頭の発行日時が一致すること", 100, entity.getmPublicationDate());
            assertEquals("先頭のサイト名が一致すること", "himasoku", entity.getmSiteTitle());
            assertEquals("先頭のサマリーが一致すること", "概要A", entity.getmSummary());
            assertEquals("先頭のサムネイルURLが一致すること", "http://image.test.com/123.png", entity.getmThumbnailUrl());
            assertEquals("先頭のカテゴリ一致すること",    "VIP", entity.getmCategory());
            assertEquals("先頭の既読状態が一致すること",  true, entity.isRead());
            assertEquals("先頭の記事の新旧が一致すること", true, entity.isNew());

        } finally {
            db.close();
        }
    }

    @Test
    public void testFindById() throws Exception {
        SQLiteDatabase db = helper.getReadableDatabase();

        try {
            HeadlineDao headlineDao = new HeadlineDao(db);
            HeadlineEntity entity = headlineDao.findById("34rrgfr44");

            assertEquals("タイトルが一致すること", "Fujitsu", entity.getmTitle());
        } finally {
            db.close();
        }
    }

    @Test
    public void testInsert() throws Exception {
        SQLiteDatabase db = helper.getReadableDatabase();

        HeadlineEntity headline = new HeadlineEntity();
        headline.setmSysId("abcdefg");
        headline.setmTitle("NEC");
        headline.setmUrl("abcdefg.html");
        headline.setmSiteTitle("ABCDEF");
        headline.setmSummary("This is summary.");
        headline.setmThumbnailUrl("Thumnail.png");
        headline.setmPublicationDate(144);
        headline.setmCategory("VIP");
        headline.setIsRead(false);
        headline.setIsNew(true);

        try {
            HeadlineDao headlineDao = new HeadlineDao(db);
            headlineDao.insert(headline);

            HeadlineEntity entity = headlineDao.findById("abcdefg");
            assertEquals("先頭のIDが一致すること",       "abcdefg", entity.getmSysId());
            assertEquals("先頭のタイトルが一致すること", "NEC", entity.getmTitle());
            assertEquals("先頭のURLが一致すること",      "abcdefg.html", entity.getmUrl());
            assertEquals("先頭の発行日時が一致すること", 144, entity.getmPublicationDate());
            assertEquals("先頭のサイト名が一致すること", "ABCDEF", entity.getmSiteTitle());
            assertEquals("先頭のサマリーが一致すること", "This is summary.", entity.getmSummary());
            assertEquals("先頭のサムネイルURLが一致すること", "Thumnail.png", entity.getmThumbnailUrl());
            assertEquals("先頭のカテゴリ一致すること",    "VIP", entity.getmCategory());
            assertEquals("先頭の既読状態が一致すること", false, entity.isRead());
            assertEquals("先頭の記事の新旧が一致すること", true, entity.isNew());
        } finally {
            db.close();
        }
    }

    @Test
    public void testUpdate() throws Exception {
        SQLiteDatabase db = helper.getReadableDatabase();

        HeadlineEntity headline = new HeadlineEntity();
        headline.setmSysId("34uifhndk");
        headline.setmTitle("NEC");
        headline.setmUrl("abcdefg.html");
        headline.setmSiteTitle("ABCDEF");
        headline.setmSummary("This is summary.");
        headline.setmThumbnailUrl("Thumnail.png");
        headline.setmPublicationDate(144);
        headline.setmCategory("VIP");
        headline.setIsRead(false);
        headline.setIsNew(true);

        try {
            HeadlineDao headlineDao = new HeadlineDao(db);
            headlineDao.update(headline);

            HeadlineEntity entity = headlineDao.findById("34uifhndk");
            assertEquals("先頭のIDが一致すること",       "34uifhndk", entity.getmSysId());
            assertEquals("先頭のタイトルが一致すること", "NEC", entity.getmTitle());
            assertEquals("先頭のURLが一致すること",      "abcdefg.html", entity.getmUrl());
            assertEquals("先頭の発行日時が一致すること", 144, entity.getmPublicationDate());
            assertEquals("先頭のサイト名が一致すること", "ABCDEF", entity.getmSiteTitle());
            assertEquals("先頭のサマリーが一致すること", "This is summary.", entity.getmSummary());
            assertEquals("先頭のサムネイルURLが一致すること", "Thumnail.png", entity.getmThumbnailUrl());
            assertEquals("先頭のカテゴリ一致すること",    "VIP", entity.getmCategory());
            assertEquals("先頭の既読状態が一致すること", false, entity.isRead());
            assertEquals("先頭の記事の新旧が一致すること", true, entity.isNew());
        } finally {
            db.close();
        }
    }

    @Test
    public void testDelete() throws Exception {
        SQLiteDatabase db = helper.getReadableDatabase();

        try {
            HeadlineDao headlineDao = new HeadlineDao(db);
            int beforeSize = headlineDao.findAll().size();
            headlineDao.delete("34rrgf444");

            List<HeadlineEntity> list = headlineDao.findAll();
            assertEquals("1件減っていること", beforeSize-1, list.size());

            //34rrgf444の列は消去されているためCursorIndexOutOfBoundsExceptionをスローする
            try {
                headlineDao.findById("34rrgf444");
                assertTrue(false);//テスト失敗
            } catch (CursorIndexOutOfBoundsException e){
            //テスト成功
            }
        } finally {
            db.close();
        }
    }
}