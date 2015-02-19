package com.quiz.pavel.quiz.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by pavelkozemirov on 18.02.15.
 */
public class Mine {
    private static Mine sMine;

    public static final String URL = "https://protected-atoll-5061.herokuapp.com";
//    public static final String URL = "https://192.168.1.39:3000";



    public static final String PREFS_NAME = "MyPrefsFile";

    private String mName;
    private String mEmail;
    private String mToken;
    private int mId;

    private Mine(Context c, JSONObject json) throws JSONException {
        mName = json.getString("name");
        mEmail = json.getString("email");
        mToken = json.getString("token");
        mId = json.getInt("id");

        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("name", mName);
        editor.putString("email", mEmail);
        editor.putString("token", mToken);
        editor.putBoolean("signin", true);
        editor.putInt("id", mId);
        Log.d("MINE", "name= " + mName + "emal ="+ mEmail + "token= "+ mToken + "id= "+mId);

        // Commit the edits!
        editor.commit();

    }
    private Mine(Context c){
            SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);

            mName = settings.getString("name", "");
            mEmail = settings.getString("email", "");
            mToken = settings.getString("token", "");
            mId = settings.getInt("id",1);
            Log.d("MINE", "name= " + mName + "emal ="+ mEmail + "token= "+ mToken + "id= "+mId);



    }

    public static Mine getInstance(Context c, JSONObject json) throws JSONException {

        if(sMine == null){
            sMine = new Mine(c,json);
        }
        return sMine;
    }

    public static Mine getInstance(Context c){
        if(sMine == null){
            sMine = new Mine(c);
        }
        return sMine;
    }
    public static void newInstance(Context c, JSONObject json) throws JSONException {

            sMine = new Mine(c,json);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public boolean isSignIn(Context c){
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean("signin", false);
    }

    public void logOut(Context c){
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("signin", false);

        // Commit the edits!
        editor.commit();
    }

}
