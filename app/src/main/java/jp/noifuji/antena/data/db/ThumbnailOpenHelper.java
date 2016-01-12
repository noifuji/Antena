package jp.noifuji.antena.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ThumbnailOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "thumbnail";

    private static final int LATEST_VERSION = 2;

    public ThumbnailOpenHelper(Context context) {
        this(context, LATEST_VERSION);
    }

    public ThumbnailOpenHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table thumbnail (\n" +
                "        sys_id text primary key, \n" +
                "        data blob \n" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table thumbnail");

        onCreate(db);
    }
}