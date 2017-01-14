package com.metacrazie.chat.main;


/**
 * Created by praty on 14/01/2017.
 */

/*
Class to handle all room name functions
 */

public class RoomName {

    private static String mFirstUser;
    private static String mSecondUser;
    private static String mMergedName;
    private static String mDisplayName;

    public static String generate_room_name(String name1, String name2){

        if (isBefore(name1, name2))
            mMergedName=name1+"_"+name2;
        else
            mMergedName=name2+"_"+name1;

        return mMergedName;

    }

    public static String display_room_name(String currentUser, String mergedName){

           if (isPrivateChat(mergedName)){
               users_in_chat(mergedName);
               if (mFirstUser.equals(currentUser))
                   return mSecondUser;
               else
                   return mFirstUser;
           }else
               return mergedName;

    }

    public static void users_in_chat(String roomName){

        int i= roomName.indexOf('_');
        mFirstUser = roomName.substring(0, i);
        mSecondUser = roomName.substring(i+1);

    }

    public static boolean isBefore(String name1, String name2){

        if (name1.charAt(0)<name2.charAt(0))
            return true;
        else if (name1.charAt(0)==name2.charAt(0))
            return isBefore(name1.substring(1), name2.substring(1));
        else
            return false;
    }

    public static boolean isPrivateChat(String roomName){

        int i= roomName.length()-1;
        while(i>=0){
            if (roomName.charAt(i)=='_'){
                return true;
            }i--;
        }return false;

    }

    public boolean isGroupChat(String roomName){

        int i= roomName.length()-1;
        while(i>=0){
            if (roomName.charAt(i)=='_'){
                return false;
            }i--;
        }return true;

    }

}
