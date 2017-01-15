package com.metacrazie.chat.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.metacrazie.chat.R;
import com.metacrazie.chat.adapters.StarListAdapter;
import com.metacrazie.chat.data.StarTable;

import java.util.ArrayList;

/**
 * Created by praty on 15/01/2017.
 */

public class StarLoader extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = StarLoader.class.getSimpleName();
    public static final String PROVIDER_NAME = "com.metacrazie.chat.data.StarProvider";
    public static final String URL = "content://"+PROVIDER_NAME+"/star_table";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    private static final int LOADER_ID = 0x01;
    private TextView textView;
    private ListView listView;
    private ArrayList<String> arrayUserList=new ArrayList<>();
    private ArrayList<String> arrayMessageList=new ArrayList<>();
    private StarListAdapter mArrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star_activity);

        listView = (ListView)findViewById(R.id.star_list);
        mArrayAdapter= new StarListAdapter(this,arrayUserList, arrayMessageList);
        listView.setAdapter(mArrayAdapter);


        getSupportLoaderManager().initLoader(LOADER_ID,null, this);
    }


    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(
                this.getApplication(),
                CONTENT_URI,
                new String[]{
                StarTable.KEY_MESSAGES},
                null,
                null,
                null);
        return loader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {

        cursor.moveToFirst();
        String text="";
        while (!cursor.isAfterLast()) {
            text = cursor.getString(0);
            cursor.moveToNext();
        }
        arrayUserList.add(text.substring(0, text.indexOf(':')-1));
        arrayMessageList.add(text.substring(text.indexOf(':')+1));

        mArrayAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }


}
