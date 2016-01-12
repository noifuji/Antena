package jp.noifuji.antena.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HeadlineOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "headline";

    private static final int LATEST_VERSION = 1;

    public HeadlineOpenHelper(Context context) {
        this(context, LATEST_VERSION);
    }

    public HeadlineOpenHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table headline (\n" +
                "        sys_id text primary key, \n" +
                "        title text,\n" +
                "        url text, \n" +
                "        pub_date integer, \n" +
                "        site_title text, \n" +
                "        summary text, \n" +
                "        thumbnail_url text, \n" +
                "        category text, \n" +
                "        is_read integer, \n" +
                "        is_new integer \n" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table headline");

        onCreate(db);
    }
}