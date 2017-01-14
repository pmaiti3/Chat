package com.metacrazie.chat.adapters;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metacrazie.chat.R;

import java.util.ArrayList;

/**
 * Created by praty on 13/01/2017.
 */

public class ChatRoomAdapter extends BaseAdapter {

    private Activity mContext;
    private ArrayList<String> mRoomList;
    private ArrayList<String> mMessageList;

    public ChatRoomAdapter(Activity context, ArrayList<String> roomList, ArrayList<String> messageList){

        mContext = context;
        mMessageList = messageList;
        mRoomList = roomList;

    }

    private class ViewHolder{
        TextView mRoomTextView;
        TextView mMessageTextView;
    }

    @Override
    public int getCount() {
        return mRoomList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 1;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ChatRoomAdapter.ViewHolder holder;
        LayoutInflater inflater =  mContext.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.chat_room_item, null);
            holder = new ChatRoomAdapter.ViewHolder();
            holder.mRoomTextView = (TextView) convertView.findViewById(R.id.textView);
            holder.mMessageTextView = (TextView) convertView.findViewById(R.id.main_last_msg_textview);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ChatRoomAdapter.ViewHolder) convertView.getTag();
        }

        holder.mRoomTextView.setText(mRoomList.get(i));
        holder.mMessageTextView.setText(mMessageList.get(i));

        return convertView;
    }
}
