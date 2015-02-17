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
    public Topic(JSONObject json){
        try {
            mText = json.getString("name");
        } catch (JSONException e) {
             Log.d(TAG, "Error, JSONException");

        }
    }
   public Topic(){
       mText = "person";
   }
    public String getTitle(){
        return mText;
    }

}
