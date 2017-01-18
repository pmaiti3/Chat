package com.metacrazie.chat.data;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by praty on 15/01/2017.
 */

public class StarTable {
    public static final String TABLE_STAR = "star_table";

    public static final String KEY_MESSAGES = "_message";
    public static final String KEY_ID = "_id";


    private static final String CREATE_USER_TABLE = "CREATE TABLE if not exists " + TABLE_STAR + "("
            + KEY_ID + " TEXT, " + KEY_MESSAGES + " TEXT"+ ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_USER_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(StarTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_STAR);
        onCreate(database);
    }

}
