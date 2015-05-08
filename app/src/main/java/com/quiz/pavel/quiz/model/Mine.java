package com.quiz.pavel.quiz.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

/**
 * Created by pavelkozemirov on 18.02.15.
 */
public class Mine {
    private static Mine sMine;

    private static final String TAG= "Mine";

//    public static final String URL = "https://protected-atoll-5061.herokuapp.com";
    public static final String URL = "http://quizapp.binaryblitz.ru/api";
    public static final String URL_photo = "http://quizapp.binaryblitz.ru";


    public static final String PREFS_NAME = "MyPrefsFile";

    private String mName;
    private String mEmail;
    private String mToken;
    private int mId;
    private String mAvatar;

    private Mine(Context c, JSONObject json) throws JSONException {

        Log.d(TAG, "json= " + json);
        mName = json.getString("username");
        mEmail = json.getString("email");
        mToken = json.getString("token");
        mId = json.getInt("id");
        mAvatar = json.getString("avatar_url");

        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", mName);
        editor.putString("email", mEmail);
        editor.putString("token", mToken);
        editor.putBoolean("signin", true);
        editor.putString("avatar_url", mAvatar);
        editor.putInt("id", mId);
        Log.d("MINE", "name= " + mName + "email =" + mEmail + "token= " + mToken + "id= " + mId + " url= " + mAvatar);

        // Commit the edits!
        editor.commit();
    }

    private Mine(Context c) {

        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);

        mName = settings.getString("username", "");
        mEmail = settings.getString("email", "");
        mToken = settings.getString("token", "");
        mAvatar = settings.getString("avatar_url","");
        mId = settings.getInt("id", 1);
        Log.d("MINE", "name= " + mName + "email =" + mEmail + "token= " + mToken + "id= " + mId);

    }

    public static Mine getInstance(Context c, JSONObject json) throws JSONException {

        if (sMine == null) {
            sMine = new Mine(c, json);
        }
        return sMine;
    }

    public static Mine getInstance(Context c) {
        if (sMine == null) {
            sMine = new Mine(c);
        }
        return sMine;
    }

    public static void newInstance(Context c, JSONObject json) throws JSONException {
        sMine = new Mine(c, json);
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

    public boolean isSignIn(Context c) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean("signin", false);
    }

    public void logOut(Context c) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("signin", false);

        // Commit the edits!
        editor.commit();
    }

    public void saveCatTopicJsonAr(Context c, JSONArray jsonArray) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("arrayCat", jsonArray.toString());

        editor.commit();
    }

    public ArrayList<Category> loadCategoryAr(Context c) {

        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        ArrayList<Category> ar = new ArrayList<>();
        String temp = settings.getString("arrayCat","");
        try {
            JSONArray json = (JSONArray) new JSONTokener(temp.toString()).nextValue();
            for (int i = 0; i < json.length(); i++) {
                ar.add(new Category(json.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Log.d(TAG, "Error with json parsing");
        }
        return ar;

    }

    public String getAvatar() {
        return mAvatar;
    }
}
