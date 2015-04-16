package com.quiz.pavel.quiz.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by pavelkozemirov on 13.01.15.
 */
public class Topic {
    public static final String TAG = "Topic";
    public String mText;
    public int mId;
    public int mPoints;

    public Topic(JSONObject json) {
        try {
            mText = json.getString("name");
            mId = json.getInt("id");
            mPoints = json.getInt("points");
        } catch (JSONException e) {
            Log.d(TAG, "Error, JSONException");

        }
    }

    public Topic() {
        mText = "person";
    }

    public String getTitle() {
        return mText;
    }

    public int getId() {
        return mId;
    }

}
