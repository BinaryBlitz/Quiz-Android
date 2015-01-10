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


    public int mMyTimeOfAnswer;
    public int mOpponentTimeOfAnswer;

    public int mMyAnswer;
    public int mOpponentAnswer;

    public static String[] sQuestions = new String[]{"what is 1 name?","what is 2 name?","what is 3 name?", "what is 4 name?","what is 5 name?","what is 6 name?"};
    public String getTextQuestion(){
        Random r = new Random();
        return sQuestions[r.nextInt(6)];
    }

    public Question getQuestion(){
        return mQuestion;
    }

    public SessionQuestion(int opponentAnswer, int opponentTimeOfAnswer){
        mQuestion = new Question(getTextQuestion());

        for (int i = 0; i < 4; i++) {
            if( mQuestion.getAnswers()[i].mIsCorrect == true){
                mCorrectAnswer = i;
            }
        }
        mOpponentAnswer = opponentAnswer;
        mOpponentTimeOfAnswer = opponentTimeOfAnswer;
        Log.d(TAG,"mOpponentTimeOfAnswer = "+opponentTimeOfAnswer);
    }

    public SessionQuestion(JSONObject json){
        try {
            mQuestion = new Question(json.getJSONObject("question"));
            for (int i = 0; i < 4; i++) {
                if( mQuestion.getAnswers()[i].mIsCorrect == true){
                    mCorrectAnswer = i;
                }
            }
            int opponent_answer_id = json.getInt("opponent_answer_id");
            for (int i = 0; i < 4; i++) {
                if( mQuestion.getAnswers()[i].mId == opponent_answer_id){
                    mOpponentAnswer = i;
                }
            }
            mOpponentTimeOfAnswer = json.getInt("opponent_time");


        } catch (JSONException e) {
            Log.d(TAG,"Error, parsing in the constructor of sessionQuestion" );

            e.printStackTrace();
        }
    }





}
