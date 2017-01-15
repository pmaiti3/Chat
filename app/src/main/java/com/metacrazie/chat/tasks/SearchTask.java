package com.metacrazie.chat.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    private ArrayList<String> resultList = new ArrayList<>();
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
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()){

                    Iterator j = ((DataSnapshot)i.next()).getChildren().iterator();
                    while (j.hasNext()) {


                        j.next();
                        Iterator k = ((DataSnapshot) j.next()).getChildren().iterator();


                        while (k.hasNext()) {
                            DataSnapshot mDataSnapshot =(DataSnapshot) k.next();
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map = (HashMap<String, Object>) (mDataSnapshot).getValue();
                            if (isPresent((String) map.get("msg"))) {
                                k.next();
                                resultList.add(((String)((DataSnapshot)k.next()).getValue())+" : "+((String) map.get("msg")));
                                Log.d(TAG,((String)((DataSnapshot)k.next()).getValue())+" : "+((String) map.get("msg")) );
                            }
                        }




                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return null;
    }

    public boolean isPresent(String compareText){

        if (compareText.toLowerCase().contains(mSearchText.toLowerCase()))
            return true;
        else
            return false;

    }

}
