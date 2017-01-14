package com.metacrazie.chat.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praty on 03/01/2017.
 */

public class UserDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "user_DB";

    private static final String TABLE_USER = "user_table";

    private static final String KEY_UID = "_uid";
    private static final String KEY_USERNAME = "_username";
    private static final String KEY_EMAIL = "_email";
    private static final String KEY_LAST_MESSAGE = "_lastMessage";
    private static final String KEY_ROOM = "_room";


    public UserDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
        String CREATE_USER_TABLE = "CREATE TABLE if not exists " + TABLE_USER + "("
                + KEY_UID + " TEXT," + KEY_USERNAME + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_LAST_MESSAGE + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        */

        DataTable.onCreate(sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        /*
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(sqLiteDatabase);
        */

        DataTable.onUpgrade(sqLiteDatabase, oldVersion, newVersion);

    }

    public void addUser(User user){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UID, user.getID());
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_LAST_MESSAGE, user.getMessage());
        values.put(KEY_ROOM, user.getRoom());

        sqLiteDatabase.insert(TABLE_USER, null, values);
        sqLiteDatabase.close();

    }

    //get User based on UID, replace UID with username
    public User getUser(String username){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_USER, new String[]
                { KEY_UID, KEY_USERNAME, KEY_EMAIL,  KEY_LAST_MESSAGE, KEY_ROOM}, KEY_USERNAME +" =?", new String[]{username}, null, null, null);

        if (cursor!=null)
            cursor.moveToFirst();

        User user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        return user;

    }

    public List<User> getAllUsers(){

        List<User> userList = new ArrayList<User>();

        String selectQuery =  "SELECT  * FROM "+TABLE_USER;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                User user = new User();
                user.setID(cursor.getString(0));
                user.setUsername(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setMessage(cursor.getString(3));
                user.setRoom(cursor.getString(4));

                userList.add(user);
            }while (cursor.moveToNext());
        }
        return userList;
    }

    public int getUsersCount(){
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        //cursor.close();

        return cursor.getCount();
    }

    public int updateUser(User user){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(KEY_UID,  user.getID());
        value.put(KEY_EMAIL, user.getEmail());
        value.put(KEY_LAST_MESSAGE, user.getMessage());
        value.put(KEY_ROOM, user.getRoom());

        return sqLiteDatabase.update(TABLE_USER, value, KEY_USERNAME + " =?", new String[]{ user.getUsername()  });
    }

    public void deleteUser(User user){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_USER, KEY_USERNAME + " =?", new String[]{user.getUsername()});
        sqLiteDatabase.close();

    }

    public boolean hasUser(String username){
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_USER + " WHERE " + KEY_USERNAME + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {username});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;
        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }

    public boolean hasRoom(String roomname){
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_USER + " WHERE " + KEY_ROOM + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {roomname});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;
        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }



}

