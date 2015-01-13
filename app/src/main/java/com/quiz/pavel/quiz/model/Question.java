package com.quiz.pavel.quiz.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by pavelkozemirov on 11.12.14.
 */
public class Question {

    private UUID mId;
    private String mText;
    private Answer[] mAnswers;


    public Question(String text){

        mText = text;
        mAnswers = new Answer[4];
        for (int i = 0; i < 4; i++) {
            mAnswers[i] = new Answer("answer"+(i+1),false);
        }
        mAnswers[3].mIsCorrect = true;
    }
    public Question(JSONObject json){
        try {
            mText = json.getString("content");
            JSONArray answers = json.getJSONArray("answers");
            mAnswers = new Answer[4];

            for (int i = 0; i < answers.length(); i++) {
                mAnswers[i] = new Answer(answers.getJSONObject(i));
            }
            List<Answer> list =  Arrays.asList(mAnswers);
            Collections.shuffle(list);
            mAnswers = new Answer[list.size()];
            list.toArray(mAnswers);


        } catch (JSONException e) {
            Log.d("", "Error, parsing in the constructor of question");

            e.printStackTrace();
        }

    }

    public String[] getAnswersText() {
        String[] ar = new String[4];
        for (int i = 0; i < 4; i++) {
            ar[i] = mAnswers[i].mText;
        }
        return ar;
    }

    public Answer[] getAnswers(){
        return mAnswers;
    }

    public String getText() {
        return mText;
    }

//
//    public Question(String text, int answer){
//        mText = text;
//        mCorrectAnswer = answer;
//        mVariants = new String[4];
//        mVariants[0] = "aaa";
//        mVariants[1] = "bbb";
//        mVariants[2] = "ccc";
//        mVariants[3] = "ddd";
//    }





}
