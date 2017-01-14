package com.metacrazie.chat.data;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by praty on 11/01/2017.
 */

public class DataTable {

    public static final String TABLE_USER = "user_table";

    public static final String KEY_UID = "_uid";
    public static final String KEY_USERNAME = "_username";
    public static final String KEY_EMAIL = "_email";
    public static final String KEY_LAST_MESSAGE = "_lastMessage";
    public static final String KEY_ROOM = "_room";

    private static final String CREATE_USER_TABLE = "CREATE TABLE if not exists " + TABLE_USER + "("
            + KEY_UID + " TEXT," + KEY_USERNAME + " TEXT,"
            + KEY_EMAIL + " TEXT," + KEY_LAST_MESSAGE + " TEXT,"  + KEY_ROOM + " TEXT"+ ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_USER_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(DataTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(database);
    }

}
