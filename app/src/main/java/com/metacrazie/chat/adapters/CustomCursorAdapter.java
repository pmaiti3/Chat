package com.metacrazie.chat.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metacrazie.chat.R;

/**
 * Created by praty on 12/01/2017.
 */

public class CustomCursorAdapter extends CursorAdapter {

    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.widget_list_item, parent, false);

        return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // here we are setting our data
        // that means, take the data from the cursor and put it in views

        TextView textViewName = (TextView) view.findViewById(R.id.widget_user);
        textViewName.setText(cursor.getString(cursor.getColumnIndex("_username")));

        TextView textViewMessage = (TextView) view.findViewById(R.id.widget_message);
        textViewMessage.setText(cursor.getString(cursor.getColumnIndex("_lastMessage")));
    }
}