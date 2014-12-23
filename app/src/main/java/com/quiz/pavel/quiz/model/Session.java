package com.quiz.pavel.quiz.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;

/**
 * Created by pavelkozemirov on 13.12.14.
 */
public class Session {
    private static final String TAG = "Session";

    private UUID mMyPlayer;
     private UUID mOpponentPlayer;

     private Date mDate;

    public int pointsMine;
    public int pointsOpponent;

    public int[]


    public void addPointsMe(int points){
        pointsMine += points;
        callback.callbackCallMine(pointsMine);                                                                  //CALLBACK



    }
    public void addPointsOpponent(int points){
        pointsOpponent += points;
        callback.callbackCallOpponent(pointsOpponent);                                                                  //CALLBACK



    }

    public LinkedList<SessionQuestion> mSessionQuestions;




    public Session(){

        mSessionQuestions = SessionQuestionsLab.getSessionsQuestionArray();
        Log.d(TAG,"mSessiongQuestions lenght is, when initializing " + mSessionQuestions.size());
    }

    public SessionQuestion getSessionQuestion(){
        Log.d(TAG,"mSessiongQuestions lenght is, when poll " + mSessionQuestions.size());
        if(!mSessionQuestions.isEmpty()){
            return mSessionQuestions.poll();
        }
        Log.d(TAG,"mSessiongQuestions lenght is, when poll!!!!!!!!!!bullshit " + mSessionQuestions.size());

        return null;
    }



    // The callback interface
    public interface MyCallback {
        void callbackCallMine(int i);
        void callbackCallOpponent(int i);

    }                                                                                                           //CALLBACK

    // The class that takes the callback
        public MyCallback callback;





}
