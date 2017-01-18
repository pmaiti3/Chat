package com.metacrazie.chat.widget;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.metacrazie.chat.R;
import com.metacrazie.chat.adapters.StarListAdapter;
import com.metacrazie.chat.data.User;
import com.metacrazie.chat.data.UserDBHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praty on 10/01/2017.
 */

public class WidgetService extends RemoteViewsService{

    private static final String TAG = WidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetItemRemoteView(this.getApplicationContext(), intent);
    }

    class WidgetItemRemoteView implements RemoteViewsService.RemoteViewsFactory {
        Context mContext;
        Intent mIntent;

        public UserDBHandler mDBHelper;

        List<User> allUsers = new ArrayList<>();

        public WidgetItemRemoteView(Context mContext, Intent mIntent) {
            this.mContext = mContext;
            this.mIntent = mIntent;
            mDBHelper = new UserDBHandler(mContext);
        }

        @Override
        public void onCreate() {
            allUsers = mDBHelper.getAllUsers();
        }

        @Override
        public void onDataSetChanged() {
            UserDBHandler DBHelper = new UserDBHandler(mContext);
            allUsers = DBHelper.getAllUsers();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {

            if (allUsers != null) {
                Log.d(TAG, allUsers.size() + " is size");
                return allUsers.size();
            } else
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
            Log.d(TAG, "set remote view items: " + user + "," + message);

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
