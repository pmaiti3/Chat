package com.metacrazie.chat.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metacrazie.chat.R;

import java.util.ArrayList;

/**
 * Created by praty on 08/01/2017.
 */

public class ContactsListAdapter extends BaseAdapter {

    private Activity mContext;
    private ArrayList<String> mUsername;
    private ArrayList<String> mEmail;

    public ContactsListAdapter(Activity context, ArrayList<String> username, ArrayList<String> email){
        mContext = context;
        mUsername = username;
        mEmail = email;
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
        TextView mUsernameTextView;
        TextView mEmailTextView;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ContactsListAdapter.ViewHolder holder;
        LayoutInflater inflater =  mContext.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.contacts_list_item, null);
            holder = new ContactsListAdapter.ViewHolder();
            holder.mUsernameTextView = (TextView) convertView.findViewById(R.id.contacts_username);
            holder.mEmailTextView = (TextView) convertView.findViewById(R.id.contacts_email);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ContactsListAdapter.ViewHolder) convertView.getTag();
        }

        holder.mUsernameTextView.setText(mUsername.get(i));
        holder.mEmailTextView.setText(mEmail.get(i));

        return convertView;
    }
}
