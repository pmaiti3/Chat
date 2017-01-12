package com.metacrazie.chat.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacrazie.chat.R;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private String pref="pref";
    private FirebaseAnalytics mFirebaseAnalytics;
    private AppCompatButton login_btn;
    private AppCompatButton chat_btn;
    private FirebaseUser mFirebaseUser;
    private SharedPreferences mSharedPreferences;
    private DatabaseReference mDatabaseReference;
    private String REGISTERED_USERS= "registered_users";
    private String CONVERSATIONS = "conversations";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO if first time then start intro activity

        //TODO check if Logged in or not

        setContentView(R.layout.activity_main);
        mFirebaseAnalytics= FirebaseAnalytics.getInstance(this);
        mSharedPreferences = getSharedPreferences(pref, MODE_PRIVATE);

        boolean isGroup = mSharedPreferences.getBoolean("isGroup", true);
        boolean isFirstRun = mSharedPreferences.getBoolean("first_run", true);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().getRoot();

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(REGISTERED_USERS)){
                    Map<String, Object> map1=new HashMap<>();
                    String temp_key1=mDatabaseReference.push().getKey();
                    String temp_key2=mDatabaseReference.push().getKey();
                    map1.put(REGISTERED_USERS, temp_key1);
                    map1.put(CONVERSATIONS, temp_key2);
                    mDatabaseReference.updateChildren(map1);
                    Log.d(TAG, "database initialised for chat");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        if(isFirstRun)
        {
            Log.d(TAG, "first run");
            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent);
            finish();
        }

        else
        {
            if(mFirebaseUser==null){
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else
            {
                Intent intent = new Intent(MainActivity.this, MainChatActivity.class);
                startActivity(intent);
                finish();
            }

        }


      /*  login_btn = (AppCompatButton)findViewById(R.id.login_btn);
        chat_btn = (AppCompatButton)findViewById(R.id.chat_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainChatActivity.class);
                startActivity(intent);
            }
        });*/
    }

}
