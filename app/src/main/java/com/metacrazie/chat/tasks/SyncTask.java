package com.metacrazie.chat.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.metacrazie.chat.R;

/**
 * Created by praty on 15/01/2017.
 */

public class SyncTask extends AsyncTask<String, String, String> {

    Activity mContext;
    private String TAG = SyncTask.class.getSimpleName();
    private String mEmail;
    private String mPassword;
    private ProgressDialog mProgressBar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public SyncTask(Activity context, String email, String password){
        mContext = context;
        mEmail = email;
        mPassword = password;
    }

    @Override
    protected String doInBackground(String... strings) {

        signOut();
        signIn(mEmail, mPassword);
        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mProgressBar = new ProgressDialog(mContext);
        mProgressBar.setMessage(mContext.getString(R.string.progress_bar));
        mProgressBar.show();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mProgressBar.cancel();
    }

    public void signIn(String email, String password){

        Log.d(TAG, "sign in existing user");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(mContext, mContext.getString(R.string.auth_fail),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    public void signOut()
    {
        mAuth.signOut();
    }

}
