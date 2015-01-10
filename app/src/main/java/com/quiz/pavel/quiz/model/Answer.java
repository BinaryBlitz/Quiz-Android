package com.quiz.pavel.quiz.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pavelkozemirov on 20.12.14.
 */
public class Answer {

    public String mText;
    public boolean mIsCorrect;
    public int mId;

    public Answer(String text, boolean isCorrect){
        mText = text;
        mIsCorrect = isCorrect;
    }
    public Answer(JSONObject json){
        try {
            mText = json.getString("content");
            mIsCorrect = json.getBoolean("correct");
            mId = json.getInt("id");
        } catch (JSONException e) {
            Log.d("", "Error, parsing in the constructor of answer");

            e.printStackTrace();
        }

    }
}
