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

    public ArrayList<SessionQuestion> mSessionQuestions;

    private int mNumber = 0;


    public static Session newInstance(){
        Session s = new Session();
        return s;
    }

    public Session(){
        mSessionQuestions = SessionQuestionsLab.getSessionsQuestionArray();
    }

    public SessionQuestion getSessionQuestion(){
        if(mNumber < 6){
            mNumber++;
            return mSessionQuestions.get(mNumber);
        }
        return null;
    }






}
