package com.metacrazie.chat.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacrazie.chat.R;
import com.metacrazie.chat.main.MainChatActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by praty on 25/12/2016.
 */

public class LoginActivity extends AppCompatActivity {

    private static String TAG=LoginActivity.class.getSimpleName();
    private String ADD_USER="add_user";
    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView mStatusText;
    private TextInputLayout mEmailLayout;
    private TextInputLayout mPasswordLayout;
    private EditText mNameField;
    private AppCompatButton mSignInBtn;
    private AppCompatButton mSignUpBtn;
    private AppCompatButton mSignOutBtn;
    private String mEmail;
    private String mPassword;
    private String displayName;
    private boolean isUserProfileUpdated = false;

    private String REGISTERED_USERS= "registered_users";
    private String CONVERSATIONS = "conversations";
    private static int flag=0;

    private ProgressDialog mProgressBar;

    private DatabaseReference userList = FirebaseDatabase.getInstance().getReference().child(REGISTERED_USERS);
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mEmailField = (EditText)findViewById(R.id.email);
        mPasswordField = (EditText)findViewById(R.id.password);
        mNameField = (EditText)findViewById(R.id.display_name);
        mStatusText = (TextView)findViewById(R.id.login_status);
        mEmailLayout = (TextInputLayout)findViewById(R.id.email_layout);
        mPasswordLayout = (TextInputLayout)findViewById(R.id.password_layout);
        mSignInBtn = (AppCompatButton)findViewById(R.id.sign_in_btn);
        mSignUpBtn = (AppCompatButton)findViewById(R.id.sign_up_btn);
        mSignOutBtn = (AppCompatButton)findViewById(R.id.sign_out_btn);
        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    //user signed in
                    //TODO get info and pass to chat screen
                    //activate signout

                    if(user.isEmailVerified()){

                        Toast.makeText(getApplicationContext(), getString(R.string.email_verified), Toast.LENGTH_SHORT).show();


                        if (user.getDisplayName()==null) {

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayName).build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {


                                                Map<String, Object> map = new HashMap<String, Object>();
                                                map.put(displayName, userList.push().getKey());
                                                userList.updateChildren(map);

                                                DatabaseReference userDetails = FirebaseDatabase.getInstance().getReference().child(REGISTERED_USERS).child(displayName);

                                                Map<String, Object> map2 = new HashMap<String, Object>();
                                                map2.put("username", displayName);
                                                map2.put("email",mEmail);
                                                map2.put("uid", user.getUid());
                                                Log.d(TAG, "details added to firebase");
                                                userDetails.updateChildren(map2);
                                                Log.d(TAG, "User profile updated.");
                                                isUserProfileUpdated = true;
                                            }
                                        }
                                    });

                            userList.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.d(TAG, "addValueEventListener called");
                                    if (isUserProfileUpdated) {
                                        Log.d(TAG, "child added to userlist");
                                        Log.d(TAG, "displayname verified not null");
                                        Log.d(TAG, "Starting chat screen");

                                        UsernameAsyncTask mTask = new UsernameAsyncTask();
                                        mTask.execute();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    Log.d(TAG, "no change in userlist");

                                }
                            });
                        }
                        else {

                            flag=0;



                            startChat(user);
                            //do something
                        }
                    }
                    else
                    {
                        user.sendEmailVerification();
                        Toast.makeText(getApplicationContext(), getString(R.string.email_verification), Toast.LENGTH_LONG ).show();
                        signOut();
                    }

                    Log.d(TAG, "user signed in: "+user.getUid());
                }else{
                    //user signed out
                    //TODO start sign in
                    Log.d(TAG, "user not signed in");
                }
                if (flag==0)
                    updateUI(user);
            }
        };

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayName = mNameField.getText().toString();
                mEmail = mEmailField.getText().toString();
                mPassword = mPasswordField.getText().toString();
                signIn(mEmail, mPassword);
            }
        });
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayName = mNameField.getText().toString();
                mEmail = mEmailField.getText().toString();
                mPassword = mPasswordField.getText().toString();
                signUp(mEmail, mPassword);
            }
        });
        mSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null)
        mAuth.removeAuthStateListener(mAuthListener);
    }


    //new User
    public void signUp(String email, String password){

        Log.d(TAG, "create new account");
        if(!validateForm())
            return;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mSignUpBtn.setClickable(false);
                        }


                    }
                });
        }


    //Existing user
    public void signIn(String email, String password){

        Log.d(TAG, "sign in existing user");
       if (flag==0) {
           if (!validateForm())
               return;
       }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    public static void signOut()
    {
        mAuth.signOut();
    }



    // Validate input fields
    private boolean validateForm()
    {
        boolean valid=true;

        if(TextUtils.isEmpty(mNameField.getText())){
            mNameField.setError("Required");
            valid=false;
        }else {
            mNameField.setError(null);
        }
        if(TextUtils.isEmpty(mEmail)){
            mEmailField.setError("Required");
            valid=false;
        }else {
            mEmailField.setError(null);
        }
        if (TextUtils.isEmpty(mPassword)){
            mPasswordField.setError("Required");
            valid=false;
        }else {
            mPasswordField.setError(null);
        }
        return valid;
    }

    //Handle UI updates
    public void updateUI(FirebaseUser user){
        if(user!=null)
        {
            mEmailLayout.setVisibility(View.INVISIBLE);
            mPasswordLayout.setVisibility(View.INVISIBLE);
            mSignInBtn.setVisibility(View.INVISIBLE);
            mSignUpBtn.setVisibility(View.INVISIBLE);
            mSignOutBtn.setVisibility(View.VISIBLE);
            mStatusText.setText(getString(R.string.status_yes));
        }else
        {
            mEmailLayout.setVisibility(View.VISIBLE);
            mPasswordLayout.setVisibility(View.VISIBLE);
            mSignInBtn.setVisibility(View.VISIBLE);
            mSignUpBtn.setVisibility(View.VISIBLE);
            mSignOutBtn.setVisibility(View.INVISIBLE);
            mStatusText.setText(getString(R.string.status_no));
            mStatusText.setVisibility(View.INVISIBLE);
        }
    }


    public void startChat(FirebaseUser user)
    {
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = new Intent(LoginActivity.this, MainChatActivity.class);
        startActivity(intent);
        finish();
    }

    //AsyncTask to override display name bug in Firebase
    private class UsernameAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            flag=1;
            signOut();
            signIn(mEmail, mPassword);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressBar = new ProgressDialog(LoginActivity.this);
            mProgressBar.setMessage(getString(R.string.progress_bar));
            mProgressBar.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressBar.cancel();
        }
    }

}
