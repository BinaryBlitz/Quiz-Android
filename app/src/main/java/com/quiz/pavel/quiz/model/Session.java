package com.quiz.pavel.quiz.model;

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

    private ArrayList<SessionQuestion> mSessionQuestions;

    private int mNumber = 0;


    public static Session newInstance(){
        Session s = new Session(UUID.randomUUID(), UUID.randomUUID());
        return s;
    }


    public SessionQuestion getSessionQuestion(){
        if(mNumber < 6){
            mNumber++;
            return mSessionQuestions.get(mNumber);
        }
        return null;
    }


    public UUID getmMyPlayer() {
        return mMyPlayer;
    }

    public void setmMyPlayer(UUID mMyPlayer) {
        this.mMyPlayer = mMyPlayer;
    }

    public UUID getmOpponentPlayer() {
        return mOpponentPlayer;
    }

    public void setmOpponentPlayer(UUID mOpponentPlayer) {
        this.mOpponentPlayer = mOpponentPlayer;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mExpireDate) {
        this.mDate = mExpireDate;
    }

    public Session(UUID player1, UUID player2){
        mMyPlayer = player1;
        mOpponentPlayer = player2;
//        mDate = date;
        mSessionQuestions = SessionQuestion.generateSessionQuestions();
    }



}
