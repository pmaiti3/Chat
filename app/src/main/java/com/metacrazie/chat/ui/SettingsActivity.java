package com.metacrazie.chat.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacrazie.chat.R;
import com.metacrazie.chat.tasks.SyncTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by praty on 05/01/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private Switch mThemeSwitch;
    private AppCompatButton mChangeDisplayName;
    private AppCompatButton mSignout;

    private String mEmail;
    private String mPassword;

    private FirebaseUser currentUser;
    private String REGISTERED_USERS= "registered_users";
    private String CONVERSATIONS = "conversations";
    private DatabaseReference userList = FirebaseDatabase.getInstance().getReference().child(REGISTERED_USERS);
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setTitle(getString(R.string.action_settings));

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        mThemeSwitch = (Switch)findViewById(R.id.switch_theme);
        mChangeDisplayName = (AppCompatButton)findViewById(R.id.btn_change_username);
        mSignout = (AppCompatButton)findViewById(R.id.btn_signout);

        mChangeDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputNewName();
            }
        });

        mSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }




    public void inputNewName(){

        final EditText nameEditText = new EditText(SettingsActivity.this);
        final EditText emailEditText = new EditText(SettingsActivity.this);
        final EditText passEditText1 = new EditText(SettingsActivity.this);
        final EditText passEditText2 = new EditText(SettingsActivity.this);

        Context context = SettingsActivity.this;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        nameEditText.setHint("New Display Name");
        layout.addView(nameEditText);

        emailEditText.setHint("Current Email");
        layout.addView(emailEditText);

        passEditText1.setHint("Curernt Password");
        layout.addView(passEditText1);

        passEditText2.setHint("Confirm Password");
        layout.addView(passEditText2);

        new AlertDialog.Builder(SettingsActivity.this)
                .setTitle(getString(R.string.change_name_title))
                .setMessage(getString(R.string.change_name_msg))
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (passEditText1.getText().toString().equals(passEditText2.getText().toString())) {
                            mEmail=emailEditText.getText().toString();
                            mPassword=passEditText1.getText().toString();
                            changeName(nameEditText.getText().toString());
                        }else
                        {
                            dialogInterface.dismiss();
                            Toast.makeText(SettingsActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    public void changeName(final String displayName){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName).build();
        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            FirebaseDatabase.getInstance().getReference().child(REGISTERED_USERS)
                                    .child(currentUser.getDisplayName()).removeValue();

                            Map<String, Object> map1 = new HashMap<String, Object>();
                            map1.put(displayName, userList.push().getKey());
                            userList.updateChildren(map1);

                            DatabaseReference userDetails= FirebaseDatabase.getInstance()
                                    .getReference().child(REGISTERED_USERS).child(displayName);

                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("username", displayName);
                            map.put("email", mEmail);
                            map.put("uid", currentUser.getUid());
                            Log.d(TAG, "updated user display name");
                            userDetails.updateChildren(map);
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
        userList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SyncTask mTask = new SyncTask(SettingsActivity.this, mEmail, mPassword);
                mTask.execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
