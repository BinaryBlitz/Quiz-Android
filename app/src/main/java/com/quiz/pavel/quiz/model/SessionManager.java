package com.quiz.pavel.quiz.model;

import android.os.Handler;
import android.util.Log;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by pavelkozemirov on 18.12.14.
 */
public class SessionManager {
    private final String TAG = "SessionManager";

    public Session mSession;


    public SessionManager(){
        mSession = new Session();


    }

    public void startTimer(int delay){

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer++;
                myHandler.post(myRunnable);
            }
        }, delay, 1000);

    }

    public void stopTimer(){
        if(mTimer == null){
            return;
        }
        mTimer.cancel();
        mTimer.purge();

    }

    public void iChooseAnswer(int number){
        mSession.myAnswer(number, timer);
    }







    public boolean amIWinner(){
        if(mSession.pointsMine > mSession.pointsOpponent){
            return true;                                                                                //BULLSHIT
        }
        return false;
    }


    public static SessionManager newInstance(){
        SessionManager ob = new SessionManager();
        return ob;
    }











    // gets false if cannot carry on game = have not questions
    public boolean newRound(){
        if(mSession.mSessionQuestions.isEmpty()){
            return false;
        } else {
            return true;
        }
    }






    private int timer;
    Handler myHandler = new Handler();
    Timer mTimer;



    final Runnable myRunnable = new Runnable() {
        public void run() {
            if(timer >= 11){
                mCallbackOnView.closeRound();
                mCallbackOnView.openRound();
                timer = 0;
            }



            if(timer == mSession.mCurrentSessionQuestion.mOpponentTimeOfAnswer){
                mSession.opponentsAnswer();
                mCallbackOnView.opponentChooseAnswer(mSession.mCurrentSessionQuestion.mOpponentAnswer);
            }


            mCallbackOnView.updateTimer(timer);

        }
    };


    // The callback interface
    public interface CallbackOnView {
        void updateTimer(int i);
        void closeRound();
        void openRound();
        void opponentChooseAnswer(int i);

    }                                                                                                           //CALLBACK

    // The class that takes the callback
    public CallbackOnView mCallbackOnView;



}
