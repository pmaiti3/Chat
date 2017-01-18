package com.metacrazie.chat.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by praty on 11/01/2017.
 */

//http://www.compiletimeerror.com/2013/12/content-provider-in-android.html#.WHWl7Rt97ZY

public class DataProvider extends ContentProvider{

    public static final String PROVIDER_NAME = "com.metacrazie.chat.data.DataProvider";
    public static final String URL = "content://"+PROVIDER_NAME+"/user_table";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    //added later
    public static final String KEY_UID = "_uid";
    public static final String KEY_USERNAME = "_username";
    public static final String KEY_EMAIL = "_email";
    public static final String KEY_LAST_MESSAGE = "_lastMessage";
    public static final String KEY_ROOM = "_room";


    private UserDBHandler database;
    private static final int USERS = 10;
    private static final int USER_USERNAME = 20;

    private static final UriMatcher uriMatcher = getUriMatcher();
    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "user_table", USERS);
        uriMatcher.addURI(PROVIDER_NAME, "user_table/#", USER_USERNAME);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        database = new UserDBHandler(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);

        sqLiteQueryBuilder.setTables(DataTable.TABLE_USER);

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case USERS:
                break;
            case USER_USERNAME:
                // adding the username to the original query
                sqLiteQueryBuilder.appendWhere(DataTable.KEY_USERNAME + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = sqLiteQueryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case USERS:
                id = sqlDB.insert(DataTable.TABLE_USER, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse("users/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case USERS:
                rowsDeleted = sqlDB.delete(DataTable.TABLE_USER, selection,
                        selectionArgs);
                break;
            case USER_USERNAME:
                String username = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            DataTable.TABLE_USER,
                            DataTable.KEY_USERNAME + "=" + username,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            DataTable.TABLE_USER,
                            DataTable.KEY_USERNAME + "=" + username
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case USERS:
                rowsUpdated = sqlDB.update(DataTable.TABLE_USER,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case USER_USERNAME:
                String username = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DataTable.TABLE_USER,
                            contentValues,
                            DataTable.KEY_USERNAME + "=" + username,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(DataTable.TABLE_USER,
                            contentValues,
                            DataTable.KEY_USERNAME + "=" + username
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }




    private void checkColumns(String[] projection) {
        String[] available = { DataTable.KEY_UID,
                DataTable.KEY_USERNAME, DataTable.KEY_EMAIL,
                DataTable.KEY_LAST_MESSAGE, DataTable.KEY_ROOM };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }


}