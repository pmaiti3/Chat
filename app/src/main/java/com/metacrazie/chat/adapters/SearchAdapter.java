package com.metacrazie.chat.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metacrazie.chat.R;

import java.util.ArrayList;

/**
 * Created by praty on 17/01/2017.
 */

public class SearchAdapter extends BaseAdapter {

    private Activity mContext;
    private ArrayList<String> mRoomname = new ArrayList<>();
    private ArrayList<String> mUsername = new ArrayList<>();
    private ArrayList<String> mMessage = new ArrayList<>();

    public SearchAdapter(Activity context, ArrayList<String> username, ArrayList<String> messages, ArrayList<String> rooms)
    {
        super();
        mContext=context;
        mUsername = username;
        mMessage=messages;
        mRoomname = rooms;
        Log.d("adapter", getCount()+"");

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
        TextView mRoomTextView;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        SearchAdapter.ViewHolder holder;
        LayoutInflater inflater =  mContext.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.search_item, null);
            holder = new SearchAdapter.ViewHolder();
            holder.mUserTextView = (TextView) convertView.findViewById(R.id.search_user);
            holder.mMessageTextView = (TextView) convertView.findViewById(R.id.search_msg);
            holder.mRoomTextView = (TextView) convertView.findViewById(R.id.search_room);
            Log.d("Adapter", "set values");
            convertView.setTag(holder);
        }
        else
        {
            holder = (SearchAdapter.ViewHolder) convertView.getTag();
        }

        holder.mUserTextView.setText(mUsername.get(i));
        holder.mMessageTextView.setText(mMessage.get(i));
        holder.mRoomTextView.setText(mRoomname.get(i));

        return convertView;
    }
}