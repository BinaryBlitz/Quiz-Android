package com.quiz.pavel.quiz.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by pavelkozemirov on 13.12.14.
 */
public class Session {


    private UUID mMyPlayer;
     private UUID mOpponentPlayer;

     private Date mDate;

    public int pointsMine;
    public int pointsOpponent;


    public void addPointsMe(int points){
        pointsMine += points;
        callback.callbackCallMine(pointsMine);                                                                  //CALLBACK



    }
    public void addPointsOpponent(int points){
        pointsOpponent += points;
        callback.callbackCallOpponent(pointsOpponent);                                                                  //CALLBACK



    }


    public ArrayList<SessionQuestion> mSessionQuestions;

    private int mNumber = 0;



    public Session(){
        mSessionQuestions = SessionQuestionsLab.getSessionsQuestionArray();
    }

    public SessionQuestion getSessionQuestion(){
        if(mNumber < 6){
            return mSessionQuestions.get(mNumber++);
        }
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
