package com.metacrazie.chat.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by praty on 15/01/2017.
 */

public class StarDBHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "star_DB";

    private static final String TABLE_STAR = "star_table";

    public static final String KEY_MESSAGES = "_message";
    public static final String KEY_ID = "_id";


    public StarDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StarTable.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        StarTable.onUpgrade(sqLiteDatabase, i , i1);
    }
}
