package com.metacrazie.chat.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metacrazie.chat.R;

import java.util.ArrayList;

/**
 * Created by praty on 12/01/2017.
 */

public class StarListAdapter extends BaseAdapter {

    private Activity mContext;
    private ArrayList<String> mDisplayText;
    private ArrayList<String> mUsername = new ArrayList<>();
    private ArrayList<String> mMessage = new ArrayList<>();

    public StarListAdapter(Activity context, ArrayList<String> username, ArrayList<String> messages)
    {
        super();
        mContext=context;
        mUsername = username;
        mMessage=messages;

    }

    @Override
    public int getCount() {
        return mUsername.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        TextView mUserTextView;
        TextView mMessageTextView;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        LayoutInflater inflater =  mContext.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.star_item, null);
            holder = new ViewHolder();
            holder.mUserTextView = (TextView) convertView.findViewById(R.id.star_user);
            holder.mMessageTextView = (TextView) convertView.findViewById(R.id.star_message);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mUserTextView.setText(mUsername.get(i));
        holder.mMessageTextView.setText(mMessage.get(i));

        return convertView;
    }
}