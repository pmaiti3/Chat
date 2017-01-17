package com.metacrazie.chat.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacrazie.chat.R;
import com.metacrazie.chat.adapters.SearchAdapter;
import com.metacrazie.chat.data.User;
import com.metacrazie.chat.data.UserDBHandler;
import com.metacrazie.chat.main.MainChatActivity;
import com.metacrazie.chat.tasks.SearchTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by praty on 17/01/2017.
 */

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();
    public static ArrayList<String> mUserList;
    public static ArrayList<String> mMessageList;
    public static ArrayList<String> mRoomList;
    public SearchAdapter mAdapter;
    private ArrayAdapter arrayAdapter;
    private static ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        setTitle(getString(R.string.search));

        Intent intent = getIntent();
        String search = intent.getStringExtra("search");

        mUserList = new ArrayList<>();
        mRoomList = new ArrayList<>();
        mMessageList = new ArrayList<>();

        mListView = (ListView)findViewById(R.id.search_list);
        mAdapter = new SearchAdapter(this, mUserList, mMessageList, mRoomList);
        mListView.setAdapter(mAdapter);

          new SearchTask(SearchActivity.this, search).execute();


    }

    public static void getData(ArrayList<String> user, ArrayList<String> msg, ArrayList<String> room){

        mUserList=user;
        mRoomList=room;
        mMessageList=msg;

        Log.d(TAG, mUserList.size()+"");
        Log.d(TAG, "getData");

        //mAdapter.notifyDataSetChanged();

    }

}
