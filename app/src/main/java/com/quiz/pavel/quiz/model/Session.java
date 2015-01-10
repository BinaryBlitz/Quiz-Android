package com.quiz.pavel.quiz.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;

/**
 * Created by pavelkozemirov on 13.12.14.
 */
public class Session {
    private static final String TAG = "Session";

    private static final String URL = "https://protected-atoll-5061.herokuapp.com";

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


    public Session(Context c){
        mSessionQuestions =  new LinkedList<SessionQuestion>();
        Log.d(TAG,"mSessiongQuestions lenght is, when initializing " + mSessionQuestions.size());
//        Random r = new Random();
//        mSessionQuestions.push(new SessionQuestion(3 - r.nextInt(3),r.nextInt(6)));
//        mSessionQuestions.push(new SessionQuestion(3 - r.nextInt(3),r.nextInt(6)));
//        mSessionQuestions.push(new SessionQuestion(3 - r.nextInt(3),r.nextInt(6)));
//        mSessionQuestions.push(new SessionQuestion(3 - r.nextInt(3),r.nextInt(6)));
//        mSessionQuestions.push(new SessionQuestion(3 - r.nextInt(3),r.nextInt(6)));
//        mSessionQuestions.push(new SessionQuestion(3 - r.nextInt(3),r.nextInt(6)));




        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(c);

        JSONObject params = new JSONObject();
        try {
            params.put("host_id", 1);
            params.put("topic_id",1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL + "/game_sessions", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d(TAG,"Error, parsing in the constructor of session $$" + String.valueOf(response));

                            JSONArray sqs = response.getJSONArray("game_session_questions");
                            Log.d(TAG,"Error, parsing in the constructor of session $$"+ String.valueOf(sqs) );
                            Log.d(TAG,"Error, parsing in the constructor of session lenght()= $$"+ String.valueOf(sqs.length()) );

                            for (int i = 0; i < sqs.length(); i++) {
                                Log.d(TAG,"Error, parsing in the constructor of session getJSONOBJ= $$"+ String.valueOf(sqs.getJSONObject(i)) );

                                mSessionQuestions.push(new SessionQuestion(sqs.getJSONObject(i)));
                            }




                        } catch (JSONException e) {
                            Log.d(TAG,"Error, parsing in the constructor of session" );

                        }
                        moveCurrentSessionQuestion();

                        callback.downloadCompleted();
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Error Response, have no data from server" );

//                mTextView.setText("That didn't work!");
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Accept","application/json");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        //TODO: move it at least to a single method
        //TODO: create at least a single method for parsing JSON



    }

    // The class that takes the callback
    public MyCallback callback;

    // The callback interface
    public interface MyCallback {
        void callbackCallMine(int i);
        void callbackCallOpponent(int i);
        void downloadCompleted();

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
