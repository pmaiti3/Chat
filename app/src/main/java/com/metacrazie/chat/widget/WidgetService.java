package com.metacrazie.chat.widget;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.metacrazie.chat.R;
import com.metacrazie.chat.adapters.CustomCursorAdapter;
import com.metacrazie.chat.data.DataTable;
import com.metacrazie.chat.data.User;
import com.metacrazie.chat.data.UserDBHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by praty on 10/01/2017.
 */

public class WidgetService extends RemoteViewsService {

    private static final String TAG = WidgetService.class.getSimpleName();
    private String REGISTERED_USERS = "registered_users";
    private String CONVERSATIONS = "conversations";
    private String MESSAGES = "messages";
    public static final String PROVIDER_NAME = "com.metacrazie.chat.data.DataProvider";
    public static final String URL = "content://"+PROVIDER_NAME+"/user_table";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    private int count = 0;

    private CustomCursorAdapter mAdapter;


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetItemRemoteView(this.getApplicationContext(), intent);
    }


    class WidgetItemRemoteView implements RemoteViewsService.RemoteViewsFactory{
        Context mContext;
        Cursor mCursor;
        private ArrayList<String> userList = new ArrayList<>();
        private ArrayList<String> messageList = new ArrayList<>();
        Intent mIntent;
        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child(CONVERSATIONS);

        public UserDBHandler mDBHelper;

        List<User> allUsers = new ArrayList<>();

        public WidgetItemRemoteView(Context mContext, Intent mIntent) {
            this.mContext = mContext;
            this.mIntent = mIntent;
            mDBHelper = new UserDBHandler(mContext);
        }

        private void populateListView() {
            for (int i = 0; i < getCount(); i++) {
                userList.add("User "+i);
                messageList.add("Message by user "+i);
                Log.d(TAG, "Added list items");
            }

        }

        @Override
        public void onCreate() {

        //    populateListView();


/*            mCursor = getContentResolver().query(CONTENT_URI,
                    new String[]{DataTable.KEY_UID, DataTable.KEY_USERNAME, DataTable.KEY_EMAIL, DataTable.KEY_LAST_MESSAGE},
                    DataTable.KEY_USERNAME+"=?",
                    new String[]{DataTable.KEY_USERNAME},
                    "");

            if (mCursor == null) {
                Log.d(TAG, "null cursor");
            }
            else if (mCursor.getCount() < 1) {
                Log.d(TAG, "cursor value less than 1");
            }
            else {

                while (mCursor.moveToNext()) {
                    userList.add(mCursor.getString(mCursor.getColumnIndex(DataTable.KEY_USERNAME)));
                    Log.d(TAG, mCursor.getString(mCursor.getColumnIndex(DataTable.KEY_USERNAME)));
                    messageList.add(mCursor.getString(mCursor.getColumnIndex(DataTable.KEY_LAST_MESSAGE)));
                    Log.d(TAG, mCursor.getString(mCursor.getColumnIndex(DataTable.KEY_LAST_MESSAGE)));
                }
                }
*/

            allUsers =  mDBHelper.getAllUsers();




        }

        @Override
        public void onDataSetChanged() {
            UserDBHandler DBHelper = new UserDBHandler(mContext);
            allUsers =  DBHelper.getAllUsers();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {

            if (allUsers!=null){
                Log.d(TAG, allUsers.size()+" is size");
                return allUsers.size();
            }
            else
                return 0;

        }

        @Override
        public RemoteViews getViewAt(int i) {

            final RemoteViews remoteView = new RemoteViews(
                    mContext.getPackageName(), R.layout.widget_list_item);

            User newUser = allUsers.get(i);

            String user = newUser.getUsername();
            String message = newUser.getMessage();
            remoteView.setTextViewText(R.id.widget_user, user);
            remoteView.setTextViewText(R.id.widget_message, message);
            Log.d(TAG, "set remote view items: "+user+","+message);

            return remoteView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }


}
