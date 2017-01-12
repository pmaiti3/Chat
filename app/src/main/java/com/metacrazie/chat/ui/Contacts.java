package com.metacrazie.chat.ui;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.metacrazie.chat.R;
import com.metacrazie.chat.adapters.ContactsListAdapter;
import com.metacrazie.chat.data.DataProvider;
import com.metacrazie.chat.data.User;
import com.metacrazie.chat.data.UserDBHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by praty on 08/01/2017.
 */

public class Contacts extends AppCompatActivity {

    private static final String TAG = Contacts.class.getSimpleName();

    private ImageView mProfileImage;
    private TextView mUsername;
    private TextView mEmail;
    private ImageView mAddContact;

    private ArrayList<String> mUsernameList = new ArrayList<>();
    private ArrayList<String> mEmailList = new ArrayList<>();
    private ArrayList<String> mProfileImageList = new ArrayList<>();

    private ListView mListView;
    private ContactsListAdapter mContactsListAdapter;

    private String REGISTERED_USERS= "registered_users";
    private String CONVERSATIONS = "conversations";
    private String CHAT_USERS="chart_auth_users";
    private String pref="pref";
    private String startNewChatUser;
    private String FRIENDS = "friends";
    private DatabaseReference newRoom = FirebaseDatabase.getInstance().getReference().child(CONVERSATIONS);
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child(REGISTERED_USERS);
    private DatabaseReference userRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        setTitle(getString(R.string.nav_friends));

        mListView = (ListView)findViewById(R.id.contacts_listview);
        mContactsListAdapter = new ContactsListAdapter(this, mUsernameList, mEmailList);
        mListView.setAdapter(mContactsListAdapter);

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_contact_list(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_contact_list(dataSnapshot);
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                startNewChatUser = ((TextView) view.findViewById(R.id.contacts_username)).getText().toString();
                final String userEmail= ((TextView)view.findViewById(R.id.contacts_email)).getText().toString();
                Log.d(TAG, "start new chat with user: "+startNewChatUser);

                //TODO check if chat already initiated
                final UserDBHandler dbHandler = new UserDBHandler(getApplication());
                if (!dbHandler.hasUser(startNewChatUser)){
                    new AlertDialog.Builder(Contacts.this)
                            .setTitle(getString(R.string.alert_new_chat_title))
                            .setMessage(R.string.alert_new_chat_message)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int i) {

                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put(startNewChatUser, "");
                                    newRoom.updateChildren(map);

                                    Map<String, Object> users = new HashMap<String, Object>();
                                    users.put(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), "");
                                    users.put(startNewChatUser,  "");
                                    newRoom.child(startNewChatUser).child(CHAT_USERS).updateChildren(users);

                                    /*
                                    User chatUser = new User("user", startNewChatUser, userEmail, "random");
                                    dbHandler.addUser(chatUser);
                                    */

                                    ContentValues values = new ContentValues();
                                    values.put(DataProvider.KEY_USERNAME,startNewChatUser);
                                    values.put(DataProvider.KEY_EMAIL, userEmail);
                                    Uri uri = getContentResolver().insert(DataProvider.CONTENT_URI, values);
                                    Log.d(TAG, "added new user via CP");

                                    SharedPreferences mSharedPreferences = getSharedPreferences(pref, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                                    editor.putBoolean("isGroup", false);
                                    editor.apply();

                                    Intent intent = new Intent(Contacts.this, ChatActivity.class);
                                    intent.putExtra("username", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                                    intent.putExtra("roomname", startNewChatUser);
                                    intent.putExtra("email", userEmail);
                                    startActivity(intent);

                                    //pass username and chatroom name as intent extras to ChatActivity

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                            }else{
                            Intent intent = new Intent(Contacts.this, ChatActivity.class);
                            intent.putExtra("username", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                            intent.putExtra("roomname", startNewChatUser);
                            startActivity(intent);
                            }
                    }
                });



    }

    public void append_contact_list(DataSnapshot dataSnapshot){

        Iterator i = dataSnapshot.getChildren().iterator();

        Set<String> set= new HashSet<String>();

        while (i.hasNext()){


            mEmailList.add((String)((DataSnapshot)i.next()).getValue());

            i.next();
            i.next();

            String sUserName= (String)((DataSnapshot)i.next()).getValue();
            mUsernameList.add(sUserName);
            mContactsListAdapter.notifyDataSetChanged();

        }

    }

}
