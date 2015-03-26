package com.cs792.Users;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.cs792.projectapp.LoginScreen;
import com.cs792.projectapp.MainScreen;

import java.util.HashMap;

/**
 * Created by sudeelet on 2/28/2015.
 */
public class UserSessionManager {

    /* Shared Preferences reference */
    SharedPreferences shared;

    /* Shared Preference Editor */
    SharedPreferences.Editor edit;

    /* Context */
    Context context;

    /* Shared pref_mode */
    int PRIVATE_MODE=0;

    /* Shared Pref file name */
    private static final String PREFER_NAME="ActiveUsers";

    private static final String IS_USER_LOGGED_IN="userLogin";

    /* User name */
    public static final String USERKEY_NAME="user";

    /* User Type */
    public static final String USERTYPE_KEY_NAME="type";

    /* User id */
    public static final String USERKEY_ID="id";

    /*User Email */
    public static final String USERKEY_EMAIL="email";

    /* Constructor */
    public UserSessionManager(Context context) {
        this.context = context;
        shared = this.context.getSharedPreferences(PREFER_NAME,PRIVATE_MODE);
        edit = shared.edit();
    }

    /* Creates a new user login session */
    public void createUserLoginSession(String user, String type,String email, int id) {
        edit.putBoolean(IS_USER_LOGGED_IN,true);
        edit.putString(USERKEY_NAME,user);
        edit.putString(USERTYPE_KEY_NAME, type);
        edit.putString(USERKEY_EMAIL,email);
        edit.putString(USERKEY_ID,String.valueOf(id));
        edit.commit();
    }

    /* Checks if user is Logged in */
    public boolean checkLogin() {
        if(!isLoggedIn()) {
            Intent intent = new Intent(this.context, MainScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return false;
        }
        return true;
    }

    /* Returns the details of the user who is logged in */
    public HashMap<String,String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String,String>();
        user.put(USERKEY_NAME,shared.getString(USERKEY_NAME,""));
        user.put(USERTYPE_KEY_NAME,shared.getString(USERTYPE_KEY_NAME,""));
        user.put(USERKEY_EMAIL,shared.getString(USERKEY_EMAIL,""));
        user.put(USERKEY_ID,shared.getString(USERKEY_ID,""));
        return user;
    }

    /* method to check the user status */
    public boolean isLoggedIn() {
        return shared.getBoolean(IS_USER_LOGGED_IN,false);
    }

    /* User Logout method. Clears the shared preferences */
    public void logout() {
        edit.clear();
        edit.commit();
        Intent intent2 = new Intent(context,MainScreen.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent2);
    }
}
