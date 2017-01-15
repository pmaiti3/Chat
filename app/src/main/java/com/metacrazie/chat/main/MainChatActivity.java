package com.metacrazie.chat.main;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacrazie.chat.R;
import com.metacrazie.chat.tasks.SearchTask;
import com.metacrazie.chat.data.DataProvider;
import com.metacrazie.chat.ui.SettingsActivity;
import com.metacrazie.chat.adapters.ChatRoomAdapter;
import com.metacrazie.chat.data.User;
import com.metacrazie.chat.data.UserDBHandler;
import com.metacrazie.chat.ui.AboutActivity;
import com.metacrazie.chat.ui.StarLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private String TAG= MainChatActivity.class.getSimpleName();

    private ListView mListView;
    private Button mAddRoomBtn;
    private EditText mRoomEditText;

    private String pref="pref";

    private TextView mNavHeaderText;

    private ArrayAdapter<String> mArrayAdapter;
    private ChatRoomAdapter mChatAdapter;

    private String roomName;
    private ArrayList<String> list_of_rooms= new ArrayList<>();
    private ArrayList<String> list_of_messages = new ArrayList<>();

    private String name;
    private String REGISTERED_USERS= "registered_users";
    private String CONVERSATIONS = "conversations";
    private String CHAT_USERS="chart_auth_users";

    private UserDBHandler dbHandler = new UserDBHandler(this);

    private DatabaseReference root= FirebaseDatabase.getInstance().getReference().child(CONVERSATIONS);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //DrawerLayout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Navigation
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mListView = (ListView)findViewById(R.id.chatlistView);

        View headerLayout= navigationView.getHeaderView(0);
        mNavHeaderText = (TextView) headerLayout.findViewById(R.id.nav_header_textView);

        FloatingActionButton groupFab =  (FloatingActionButton)findViewById(R.id.group_fab);
        FloatingActionButton privateFab = (FloatingActionButton)findViewById(R.id.private_fab);
        groupFab.setTitle("Start Group Chat");
        privateFab.setTitle("Start Private Chat");

/*
        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.chat_room_item, list_of_rooms);
        mListView.setAdapter(mArrayAdapter);
*/

        //custom adapter with data from content provider using loader

        mChatAdapter = new ChatRoomAdapter(this, list_of_rooms, list_of_messages);
        mListView.setAdapter(mChatAdapter);


        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        name = user.getDisplayName();
        Log.d(TAG, name);

        mNavHeaderText.setText(name);

        groupFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText groupEditText = new EditText(MainChatActivity.this);

                new AlertDialog.Builder(MainChatActivity.this)
                        .setTitle(getString(R.string.alert_new_group_message))
                        .setMessage(getString(R.string.message_group_chat))
                        .setView(groupEditText)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Map<String, Object> map=new HashMap<String, Object>();
                                map.put(groupEditText.getText().toString(), root.push().getKey());
                                root.updateChildren(map);

                                Map<String, Object> users = new HashMap<String, Object>();
                                users.put(user.getDisplayName(), "");
                                root.child(groupEditText.getText().toString()).child(CHAT_USERS).updateChildren(users);

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

            }
        });

        privateFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(MainChatActivity.this, Contacts.class);
                startActivity(intent);
            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list_of_messages.clear();
                list_of_rooms.clear();

                Iterator i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    DataSnapshot mDataSnapshot =(DataSnapshot) i.next();
                    if (mDataSnapshot.child(CHAT_USERS).hasChild(user.getDisplayName())) {

                        roomName= mDataSnapshot.getKey();
                        if (!dbHandler.hasRoom(roomName)){

                            //CHANGE

                            ContentValues values = new ContentValues();
                            values.put(DataProvider.KEY_USERNAME, RoomName.display_room_name(user.getDisplayName(),roomName));
                            values.put(DataProvider.KEY_ROOM, roomName);
                            getContentResolver().insert(DataProvider.CONTENT_URI, values);
                            Log.d(TAG, "added new user via CP");
                        }
                        String getName= RoomName.display_room_name(user.getDisplayName(),roomName);
                        list_of_rooms.add(getName);
                        String string = updateRoomMessage((mDataSnapshot).getKey());
                        list_of_messages.add(string);

                    }

                }

                    if (list_of_rooms.size()!=0){
                        mChatAdapter.notifyDataSetChanged();
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainChatActivity.this, ChatActivity.class);
                String user1= ((TextView)view.findViewById(R.id.textView)).getText().toString();

                if (dbHandler.hasRoom(user1)){
                    intent.putExtra("roomname", user1);
                }else {
                    intent.putExtra("roomname", RoomName.generate_room_name(user1, name));
                }
                intent.putExtra("username", name);
                startActivity(intent);
            }
        });
    }

    public String updateRoomMessage(String mergedRoomName){

        String otherUser = RoomName.display_room_name(FirebaseAuth.getInstance()
                .getCurrentUser().getDisplayName(), mergedRoomName);

        if (dbHandler.hasRoom(mergedRoomName)) {
            User user = dbHandler.getUser(otherUser);
            if (user.getMessage() != null)
                return user.getMessage();
            else
                return null;
        }
        else
            return "random";

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_chat, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                new SearchTask(MainChatActivity.this, s).execute();
                Log.d(TAG, "onQueryTextSubmit "+s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "onQueryTextChange "+s);
                return false;
            }
        });

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chat) {

            //TODO check which activity is running

            Intent intent = new Intent(MainChatActivity.this, MainChatActivity.class);
            startActivity(intent);
            finish();
            // Handle the camera action
        } else if (id == R.id.nav_friends) {
            Intent intent = new Intent(MainChatActivity.this, Contacts.class);
            startActivity(intent);

        } else if (id == R.id.nav_starred) {
            Intent intent = new Intent(MainChatActivity.this, StarLoader.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainChatActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainChatActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
