package com.metacrazie.chat.tasks;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacrazie.chat.R;
import com.metacrazie.chat.adapters.SearchAdapter;
import com.metacrazie.chat.data.User;
import com.metacrazie.chat.data.UserDBHandler;
import com.metacrazie.chat.main.RoomName;
import com.metacrazie.chat.ui.SearchActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by praty on 10/01/2017.
 */

public class SearchTask extends AsyncTask<String, String, String> {

    private Activity mContext;
    private ProgressDialog mProgressBar;
    private String mSearchText;
    private String TAG = SearchTask.class.getSimpleName();

    private String REGISTERED_USERS= "registered_users";
    private String CONVERSATIONS = "conversations";
    private String CHAT_USERS="chart_auth_users";
    private String MESSAGES =  "messages";

    private ArrayList<String> mUserList;
    private ArrayList<String> mMessageList;
    private ArrayList<String> mRoomList;

    private ListView mListView;

    private List<User> mListUser;
    private UserDBHandler helper;
    private SearchAdapter mAdapter;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child(CONVERSATIONS);

    public SearchTask(Activity context, String searchText){
        mContext = context;
        mSearchText =  searchText;
        helper=new UserDBHandler(mContext);
        mUserList = new ArrayList<>();
        mRoomList = new ArrayList<>();
        mMessageList = new ArrayList<>();

        mListView = (ListView)mContext.findViewById(R.id.search_list);
        mAdapter = new SearchAdapter(mContext, mUserList, mMessageList, mRoomList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mProgressBar = new ProgressDialog(mContext);
        mProgressBar.setMessage(mContext.getString(R.string.search_progress));
        mProgressBar.show();
    }

    @Override
    protected void onPostExecute(String s) {
        //super.onPostExecute(s);
        mProgressBar.cancel();
        mAdapter.notifyDataSetChanged();
        SearchActivity.getData(mUserList, mMessageList, mRoomList);
    }

    @Override
    protected String doInBackground(String... strings) {

        Log.d(TAG, "start search");


        mListUser = helper.getAllUsers();
        int count = helper.getUsersCount();

        int j=0;
        while (j<count){

            User user = mListUser.get(j);
            final String room = user.getRoom();

            Log.d(TAG, room);

            DatabaseReference mDataRef = root.child(room).child(MESSAGES);
            mDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterator iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()){

                        HashMap<String, Object> map = (HashMap<String, Object>) ((DataSnapshot)iterator.next()).getValue();
                        String msg = (String) map.get("msg");
                        String usr = (String) map.get("user");

                        if (isPresent(msg)){
                            mUserList.add( usr);
                            mMessageList.add(" : "+msg);
                            mRoomList.add(RoomName.display_room_name(FirebaseAuth
                                    .getInstance()
                                    .getCurrentUser()
                                    .getDisplayName(),room));
                            Log.d(TAG, usr+" : "+msg);
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            j++;

        }

        return null;

    }

    public boolean isPresent(String compareText){

        if (compareText.toLowerCase().contains(mSearchText.toLowerCase()))
            return true;
        else
            return false;

    }

}
