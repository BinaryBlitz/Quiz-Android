package com.quiz.pavel.quiz.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by pavelkozemirov on 20.12.14.
 */
public class Category {
    public static final String TAG = "Category";

    public String mText;
    public UUID mId;
    public ArrayList<Topic> mTopics;

    public Category(JSONObject json){
        try {
            mText = json.getString("name");

            JSONArray ar = json.getJSONArray("topics");

            mTopics = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                mTopics.add(new Topic(ar.getJSONObject(i)));
            }

        } catch (JSONException e) {
            Log.d(TAG, "Error, JSONException");
        }
    }

    public String getTitle(){
        return mText;
    }
    public String getTitleTopics(){
        String str = "";
        for (Topic topic : mTopics) {
            str += " " + topic.getTitle();
        }
        return str;
    }



}
