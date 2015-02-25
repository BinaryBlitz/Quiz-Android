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
    public JSONArray mJsonTopics;

    public Category(JSONObject json) {
        try {
            mText = json.getString("name");

            JSONArray ar = json.getJSONArray("topics");
            mJsonTopics = ar;

            mTopics = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                mTopics.add(new Topic(ar.getJSONObject(i)));
            }

        } catch (JSONException e) {
            Log.d(TAG, "Error, JSONException");
        }
    }

    public String getJsonTopics() {
        return mJsonTopics.toString();
    }

    public static ArrayList<Topic> stringToArray(String jsonAr) {
        JSONArray ar;
        try {
            ar = new JSONArray(jsonAr);
            ArrayList<Topic> topics = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                topics.add(new Topic(ar.getJSONObject(i)));
            }
            return topics;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTitle() {
        return mText;
    }

    public String getTitleTopics() {
        String str = "";
        for (Topic topic : mTopics) {
            str += " " + topic.getTitle();
        }
        return str;
    }


}
