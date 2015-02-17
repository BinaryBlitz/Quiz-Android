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

    public PlayerRating(JSONObject json){
        try {
            mName = json.getString("name");
            mPoints = json.getInt("points");
        } catch (JSONException e) {
            Log.d(TAG, "Error with parsing json in UserRating constructor");
        }

    }

    public PlayerRating(){
        mName = "FUCKER";
        mPoints = 12312;
    }
}
