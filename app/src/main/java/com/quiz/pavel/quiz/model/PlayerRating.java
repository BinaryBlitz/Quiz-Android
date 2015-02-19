package com.quiz.pavel.quiz.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by pavelkozemirov on 17.02.15.
 */
public class PlayerRating {

    private static final String TAG = "PlayerRating";

    public UUID mId;
    public String mName;
    public int mPoints;
    public int mPosition;
    public boolean mI;

    public PlayerRating(JSONObject json, int position, boolean a){
        try {
            mName = json.getString("name");
            mPoints = json.getInt("points");
            mPosition = position;
        } catch (JSONException e) {
            Log.d(TAG, "Error with parsing json in UserRating constructor");
        }

    }

    public PlayerRating(int position, String name, boolean a){
        mName = name;
        mPoints = -1;
        mI = a;
    }
    public String getTitle(){
        return mName;
    }
}
