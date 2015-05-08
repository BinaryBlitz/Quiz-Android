package com.quiz.pavel.quiz.model;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by pavel on 30/04/15.
 */
public class Achievement {
    private final static String TAG = "Achievement";

    private int mId;
    private String mName;
    private String mDescription;
    private boolean mAchieved;
    private String mUrl;

    public String getUrl() {
        return mUrl;
    }


    public void setUrl(String url) {
        mUrl = url;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isAchieved() {

        return mAchieved;
    }

    public void setAchieved(boolean achieved) {
        mAchieved = achieved;
    }

    public String getName() {

        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getId() {

        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public Achievement(JSONObject json) {
        try {
            mName = json.getString("name");
            mId = json.getInt("id");
            mDescription = json.getString("description");
            mUrl= json.getString("icon_url");
        } catch (Exception e) {
            Log.d(TAG, "Error, JSONException");
        }
    }

}
