package com.quiz.pavel.quiz.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;

/**
 * Created by pavelkozemirov on 13.12.14.
 */
public class SessionQuestion {
    private static String TAG = "SessionQuestion";

    private Question mQuestion;
    public int mCorrectAnswer;

    public int mId;


    public int mMyTimeOfAnswer;
    public int mOpponentTimeOfAnswer;

    public int mMyAnswer;
    public int mOpponentAnswer;


    public Question getQuestion(){
        return mQuestion;
    }


    public SessionQuestion(JSONObject json){
        try {
            mQuestion = new Question(json.getJSONObject("question"));
            for (int i = 0; i < 4; i++) {
                if( mQuestion.getAnswers()[i].mIsCorrect == true){
                    mCorrectAnswer = i;
                }
            }
            if (!json.isNull("opponent_answer_id")){
                int opponent_answer_id = json.getInt("opponent_answer_id");
                for (int i = 0; i < 4; i++) {
                    if( mQuestion.getAnswers()[i].mId == opponent_answer_id){
                        mOpponentAnswer = i;
                    }
                }
                mOpponentTimeOfAnswer = json.getInt("opponent_time");
            } else{
                mOpponentTimeOfAnswer = -1;
                mOpponentAnswer = -1;
            }
            mId = json.getInt("id");


        } catch (JSONException e) {
            Log.d(TAG,"Error, parsing in the constructor of sessionQuestion" );

            e.printStackTrace();
        }
    }

    public int searchNubById(int id){
        for (int i = 0; i < mQuestion.getAnswers().length; i++) {
            if (id == mQuestion.getAnswers()[i].mId){
                return i;
            }
        }
        return 0;
    }





}
