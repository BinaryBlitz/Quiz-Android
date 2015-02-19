package com.quiz.pavel.quiz.model;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.quiz.pavel.quiz.controller.SingleFragmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


/**
 * Created by pavelkozemirov on 18.12.14.
 */
public class SessionManager {
    private static final String TAG = "SessionManager";
    private static SessionManager sSessionManager;


    public Session mSession;
    public boolean online;
    public Channel mChannel;
    public Pusher mPusher;

    public SessionManager(Context c){

        mPusher = new Pusher("d982e4517caa41cf637c");
        mChannel = mPusher.subscribe("player-session-" + Mine.getInstance(c).getId());
//        Pusher pusher = new Pusher("d982e4517caa41cf637c");
        mPusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("GOO State changed to " + change.getCurrentState() +
                        " from " + change.getPreviousState());

            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("GOO There was a problem connecting!");
            }
        }, ConnectionState.ALL);

//        mChannel = pusher.subscribe("test_channel");
//
//        mChannel.bind("my_event", new SubscriptionEventListener() {
//            @Override
//            public void onEvent(String channel, String event, String data) {
//                Log.d(TAG, "THATs good");
//            }
//        });

//        mChannel.bind("opponent-answer", new SubscriptionEventListener() {
//            @Override
//            public void onEvent(String channel, String event, String data) {
//                int time = 0, answer = 0;
//
//                try {
//                    JSONObject json = new JSONObject(data);
//                    time = json.getInt("answer_time");
//                    answer = json.getInt("answer_id");
//                } catch (JSONException e) {
//                    Log.d(TAG,"data doesnt parse to json");
//                }
//
//                mSession.opponentsAnswer(answer, time);
//            }
//        });
    }

    public static SessionManager getInstance(Context c){
        if(sSessionManager == null) {
            sSessionManager = new SessionManager(c);
            Log.d(TAG, "new instance of SM was created");
        }
        return sSessionManager;
    }

    public static void deleteInstance(){
        sSessionManager = null;
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

    public void iChooseAnswer(Context c, int number){
        mSession.myAnswer(c, number, timer);
    }







    public boolean amIWinner(){
        if(mSession.pointsMine > mSession.pointsOpponent){
            return true;                                                                                //BULLSHIT
        }
        return false;
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
            if(timer >= 11 || mSession.bothPlayersAreAnswered()){
                mCallbackOnView.closeRound();
                mCallbackOnView.openRound();
                timer = 0;
            }


            if(!online) {
                if (timer == mSession.mCurrentSessionQuestion.mOpponentTimeOfAnswer) {
                    mSession.opponentsAnswer(0,0);
                    mCallbackOnView.opponentChooseAnswer(mSession.mCurrentSessionQuestion.mOpponentAnswer);
                }
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
