package com.quiz.pavel.quiz.model;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


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

    public SessionManager(Context c) {

        mPusher = new Pusher("d982e4517caa41cf637c");
        mChannel = mPusher.subscribe("player-session-" + Mine.getInstance(c).getId());
//        Pusher pusher = new Pusher("d982e4517caa41cf637c");


        sSessionManager = this;

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg){
            mSession.opponentsAnswer(0,0);
            mCallbackOnView.opponentChooseAnswer(mSession.mCurrentSessionQuestion.mOpponentAnswer);
        }
    };

    public void listenEvent() {

        myHandler = new Handler();

        if(!online) {
            return;
        }

        mChannel.bind("opponent-answer", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                try {
                    Log.d(TAG, "DATA: " + data);
                    JSONObject json = new JSONObject(data);

                    mSession.mCurrentSessionQuestion.mOpponentAnswer =
                            mSession.mCurrentSessionQuestion.searchNubById(json.getInt("answer_id"));

                    mSession.mCurrentSessionQuestion.mOpponentTimeOfAnswer = json.getInt("answer_time");

                    handler.sendEmptyMessage(1);

                } catch (JSONException e) {
                    Log.d(TAG, "Problem with parsing data sended via pusher");
                }
            }
        });
    }


    public static SessionManager getInstance(Context c) {
        if (sSessionManager == null) {
            sSessionManager = new SessionManager(c);
            Log.d(TAG, "new instance of SM was created");
        }
        return sSessionManager;
    }

    public void deleteInstance() {
        stopTimer();
        myHandler.removeCallbacks(myRunnable);
        sSessionManager = null;
    }

    public void startTimer(int delay) {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer++;
                myHandler.post(myRunnable);
            }
        }, delay, 1000);

    }

    public void stopTimer() {
        if (mTimer == null) {
            return;
        }
        mTimer.cancel();
        mTimer.purge();
    }

    public void iChooseAnswer(Context c, int number) {
        mSession.myAnswer(c, number, timer);
    }


    public int amIWinner() {
        if (mSession.pointsMine > mSession.pointsOpponent) {
            return 1;
        } else if (mSession.pointsMine < mSession.pointsOpponent) {
            return -1;
        } else {
            return 0;
        }
    }


    // gets false if cannot carry on game = have not questions
    public boolean newRound() {
        if (mSession.mSessionQuestions.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }


    private int timer;
    public Handler myHandler;
    Timer mTimer;
    int round;


    public final Runnable myRunnable = new Runnable() {
        public void run() {
            if (timer == 11 || mSession.bothPlayersAreAnswered()) {
                round++;
                if (round > 7) {
                    deleteInstance();
                }
                mCallbackOnView.closeRound();
                mCallbackOnView.openRound();
                timer = 0;
            }


            if (!online) {
                if (timer == mSession.mCurrentSessionQuestion.mOpponentTimeOfAnswer) {
                    mSession.opponentsAnswer(0, 0);
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
