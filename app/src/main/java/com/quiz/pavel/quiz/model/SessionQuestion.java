package com.quiz.pavel.quiz.model;

import android.util.Log;

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





}
