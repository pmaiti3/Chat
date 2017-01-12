package com.metacrazie.chat.data;

/**
 * Created by praty on 03/01/2017.
 */

public class User {

    private String mUID;
    private String mUsername;
    private String mEmail;
    private String mProfileImage;
    private String mMessage;

    //Empty constructor
    public User(){

    }

    //Parameterised constructor
    public User(String id, String username, String email,String message){
        mUID = id;
        mUsername = username;
        mEmail = email;
        mMessage = message;
    }

    public String getID(){
        return this.mUID;
    }

    public void setID(String ID){
        mUID =ID;
    }

    public String getUsername(){
        return mUsername;
    }

    public void setUsername(String username){
        mUsername = username;
    }

    public String getEmail(){
        return mEmail;
    }

    public void setEmail(String email){
        mEmail = email;
    }

    public String getProfileImage(){
        return mProfileImage;
    }

    public void setProfileImage(String profileImage){
        mProfileImage = profileImage;
    }

    public String getMessage(){ return mMessage; }

    public void setMessage(String message){
        mMessage = message;
    }

}
