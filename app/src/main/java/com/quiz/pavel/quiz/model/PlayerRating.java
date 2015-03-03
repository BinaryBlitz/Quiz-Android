package com.quiz.pavel.quiz.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by pavelkozemirov on 17.02.15.
 */
public class PlayerRating {

    private static final String TAG = "PlayerRating";

    private int mId;
    private String mName;
    private int mPoints;
    private int mPosition;

    public PlayerRating(JSONObject json, int i) {
        try {
            mId = json.getInt("id");
            mName = json.getString("name");
            mPoints = json.getInt("points");
            mPosition = i;
        } catch (JSONException e) {
            Log.d(TAG, "Error with parsing json in UserRating constructor");
        }
    }

    public PlayerRating(String name, int position, int points, int id){
        mId = id;
        mName = name;
        mPoints = points;
        mPosition = position;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getPoints() {
        return mPoints;
    }

    public int getPosition() {
        return mPosition;
    }
}
