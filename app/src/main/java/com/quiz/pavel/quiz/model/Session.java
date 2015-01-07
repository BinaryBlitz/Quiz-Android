package com.quiz.pavel.quiz.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
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


    public SessionQuestion mCurrentSessionQuestion;

    public LinkedList<SessionQuestion> mSessionQuestions;

    private boolean mOpponentIsAnwered;
    private boolean mIIsAnswered;

    public boolean bothPlayersAreAnswered(){
        return mOpponentIsAnwered && mIIsAnswered;
    }


    public Session(){
        mSessionQuestions = SessionQuestionsLab.getSessionsQuestionArray();
        Log.d(TAG,"mSessiongQuestions lenght is, when initializing " + mSessionQuestions.size());
        Random r = new Random();
        mSessionQuestions.push(new SessionQuestion(3 - r.nextInt(3),r.nextInt(6)));
        mSessionQuestions.push(new SessionQuestion(3 - r.nextInt(3),r.nextInt(6)));
        mSessionQuestions.push(new SessionQuestion(3 - r.nextInt(3),r.nextInt(6)));
        mSessionQuestions.push(new SessionQuestion(3 - r.nextInt(3),r.nextInt(6)));
        mSessionQuestions.push(new SessionQuestion(3 - r.nextInt(3),r.nextInt(6)));
        mSessionQuestions.push(new SessionQuestion(3 - r.nextInt(3),r.nextInt(6)));
        moveCurrentSessionQuestion();

    }

    // The class that takes the callback
    public MyCallback callback;

    // The callback interface
    public interface MyCallback {
        void callbackCallMine(int i);
        void callbackCallOpponent(int i);

    }

    public void myAnswer(int number, int time){
        mIIsAnswered = true;
        mCurrentSessionQuestion.mMyAnswer = number;
        mCurrentSessionQuestion.mMyTimeOfAnswer = time;
        if(mCurrentSessionQuestion.mCorrectAnswer == number){
            addPointsMe(time);
        }
    }
    public void opponentsAnswer(){
        mOpponentIsAnwered = true;
        if(mCurrentSessionQuestion.mCorrectAnswer == mCurrentSessionQuestion.mOpponentAnswer){
            addPointsOpponent(mCurrentSessionQuestion.mOpponentTimeOfAnswer);
        }
    }
    public void addPointsMe(int time){
        pointsMine += (150 - 10 * time);
        callback.callbackCallMine(pointsMine);                                                                  //CALLBACK
    }
    public void addPointsOpponent(int time){
        pointsOpponent += (150 - 10 * time);
        callback.callbackCallOpponent(pointsOpponent);                                                                  //CALLBACK
    }


    public int getNumberOfRound(){
        return 6 - mSessionQuestions.size();
    }




                                                                                           //CALLBACK
    public boolean moveCurrentSessionQuestion(){
        if(!mSessionQuestions.isEmpty()){
            mCurrentSessionQuestion = mSessionQuestions.poll();
            mIIsAnswered = false;
            mOpponentIsAnwered = false;
            return true;
        }

        return false;
    }





}
