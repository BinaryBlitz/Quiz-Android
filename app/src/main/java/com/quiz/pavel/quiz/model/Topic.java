package com.quiz.pavel.quiz.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pavelkozemirov on 13.01.15.
 */
public class Topic {
    public static final String TAG = "Topic";
    public String mText;
    public int mId;
    public int mPoints;
    private int mProgress;

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

    public int getLevel() {
        int resScore = mPoints;
        int lvl = 0;

        int pointsForLevel = 50;
        int diff = 50;

        while (true) {
            pointsForLevel += diff;

            if(resScore - pointsForLevel<0){
                mProgress = (resScore*100)/pointsForLevel;
                return lvl;
            } else {
                resScore -= pointsForLevel;
                lvl++;
            }
        }

    }

    public int getProgress() {
        return mProgress;
    }

}
