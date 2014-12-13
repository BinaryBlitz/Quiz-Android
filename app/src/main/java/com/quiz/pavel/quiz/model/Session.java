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


    public static Session newInstance(){
        Session s = new Session(UUID.randomUUID(), UUID.randomUUID());
        return s;
    }


    public SessionQuestion getSessionQuestion(){
        return mSessionQuestions.get(0);
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
