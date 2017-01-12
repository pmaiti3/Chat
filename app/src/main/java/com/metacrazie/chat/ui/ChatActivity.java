package com.metacrazie.chat.ui;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.metacrazie.chat.R;
import com.metacrazie.chat.adapters.ChatListAdapter;
import com.metacrazie.chat.data.DataProvider;
import com.metacrazie.chat.data.User;
import com.metacrazie.chat.data.UserDBHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by praty on 29/12/2016.
 */

public class ChatActivity extends AppCompatActivity {

    private ListView mListView;
    private EditText mMessageText;
    private TextView chat_conversation;
    private ArrayAdapter<String> mArrayAdapter;
    private ArrayList<String> mMessageList = new ArrayList<>() ;
    private ArrayList<String> mUserList = new ArrayList<>();

    private String REGISTERED_USERS= "registered_users";
    private String CONVERSATIONS = "conversations";
    private String MESSAGES =  "messages";

    private ChatListAdapter mChatListAdapter;
 //   SharedPreferences mSharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
    private String TAG = ChatActivity.class.getSimpleName();

    private String username;
    private String chatroom;
    private String mEmail;
    private String temp_key;
    private DatabaseReference root;
    private UserDBHandler dbHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen);

        final FloatingActionButton mSendFab = (FloatingActionButton) findViewById(R.id.chat_fab);
        mListView = (ListView)findViewById(R.id.chat_list);
        mMessageText = (EditText)findViewById(R.id.chat_text);

        username = getIntent().getExtras().get("username").toString();
        chatroom = getIntent().getExtras().get("roomname").toString();
        setTitle(chatroom);

        root = FirebaseDatabase.getInstance().getReference().child(CONVERSATIONS).child(chatroom).child(MESSAGES);

      //  boolean isGroup= mSharedPreferences.getBoolean("isGroup", true);
        //TODO open add users button

        mListView = (ListView)findViewById(R.id.chat_list);
        mChatListAdapter=new ChatListAdapter(this, mUserList, mMessageList);
        mListView.setAdapter(mChatListAdapter);

        //Initialise database
        dbHandler = new UserDBHandler(this);


        mSendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> map=new HashMap<String, Object>();
                temp_key=root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root= root.child(temp_key);
                Map<String, Object> newMap = new HashMap<String, Object>();
                newMap.put("user", username);
                newMap.put("msg", mMessageText.getText().toString());
                message_root.updateChildren(newMap);

                //TODO Check if the chat is private or group
                /*
                User user = new User("", chatroom, "" , username+" : "+mMessageText.getText().toString());
                dbHandler.updateUser(user);
                Log.d(TAG, "Updated database: "+chatroom);
                */

                ContentValues values = new ContentValues();
                values.put(DataProvider.KEY_LAST_MESSAGE,  username+" : "+mMessageText.getText().toString());
                getContentResolver().update(DataProvider.CONTENT_URI, values, DataProvider.KEY_USERNAME+"=?",new String[] {chatroom});
                Log.d(TAG, "added new msg via CP");


                mMessageText.setText("");
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            mMessageList.add((String) ((DataSnapshot)i.next()).getValue());
            mUserList.add((String) ((DataSnapshot)i.next()).getValue());

            mChatListAdapter.notifyDataSetChanged();

        }
    }
}
