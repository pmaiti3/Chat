package com.metacrazie.chat;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Created by praty on 10/01/2017.
 */

public class SearchTask extends AsyncTask<String, String, String> {

    private Context mContext;
    private String mSearchText;
    private String TAG = SearchTask.class.getSimpleName();
    private String REGISTERED_USERS= "registered_users";
    private String CONVERSATIONS = "conversations";
    private String CHAT_USERS="chart_auth_users";
    private String MESSAGES =  "messages";
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child(CONVERSATIONS);

    public SearchTask(Context context, String searchText){
        mContext = context;
        mSearchText =  searchText;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... strings) {

        Log.d(TAG, "start search");
        root.orderByChild(MESSAGES).startAt(mSearchText).endAt(mSearchText+"\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator iterator = dataSnapshot.getChildren().iterator();

                        while (iterator.hasNext()){
                            String user = ((DataSnapshot)iterator.next()).getValue().toString();
                            String msg = ((DataSnapshot)iterator.next()).getValue().toString();
                            Log.d(TAG, user+" : "+msg);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return null;
    }
}
