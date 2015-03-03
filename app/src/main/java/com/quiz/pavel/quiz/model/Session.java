package com.quiz.pavel.quiz.model;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

    public int pointsMine;
    public int pointsOpponent;

    public SessionQuestion mCurrentSessionQuestion;

    private String mMyName = "Я";
    private String mOpponentsName = "Оппонент";



    public LinkedList<SessionQuestion> mSessionQuestions;

    private boolean mOpponentIsAnwered;
    private boolean mIIsAnswered;

    public boolean bothPlayersAreAnswered() {
        return mOpponentIsAnwered && mIIsAnswered;
    }


    public Session(JSONObject response) {
        mSessionQuestions = new LinkedList<SessionQuestion>();


        try {
            mMyName = response.getString("host_name");
            mOpponentsName = response.getString("opponent_name");
        } catch (JSONException ex) {

        }

        try {
            JSONArray sqs = response.getJSONArray("game_session_questions");

            for (int i = 0; i < sqs.length(); i++) {
                mSessionQuestions.add(new SessionQuestion(sqs.getJSONObject(i)));
                Log.d(TAG, "fot constructor of sessionQuestion: " + sqs.getJSONObject(i).toString());

            }

        } catch (JSONException e) {
            Log.d(TAG, "Error, error with parsing in the constructor of session");
        }

        moveCurrentSessionQuestion();
    }

    public String getMyName() {
        return mMyName;
    }

    public String getOpponentsName() {
        return mOpponentsName;
    }

    public MyCallback callback;

    public interface MyCallback {
        void callbackCallMine(int i);

        void callbackCallOpponent(int i);
    }

    public void myAnswer(Context c, int number, int time) {
        mIIsAnswered = true;
        mCurrentSessionQuestion.mMyAnswer = number;
        mCurrentSessionQuestion.mMyTimeOfAnswer = time;

        sendData(c, number, time);

        if (mCurrentSessionQuestion.mCorrectAnswer == number) {
            addPointsMe(time);
        }
    }

    public void opponentsAnswer(int answer, int time) {

        mOpponentIsAnwered = true;

        Log.d(TAG, "opponentAnswer(): answer= " + answer + "; time = " + time);
        Log.d(TAG, "opponentAnswer(): correctAnswer = " + mCurrentSessionQuestion.mCorrectAnswer);

        if (mCurrentSessionQuestion.mOpponentAnswer == -1 &&
                mCurrentSessionQuestion.mOpponentTimeOfAnswer == -1) {
            mCurrentSessionQuestion.mOpponentTimeOfAnswer = time;
            mCurrentSessionQuestion.mOpponentAnswer = answer;
        }

        Log.d(TAG, "mCurrentSessionQuestion.mCorrectAnswer = " + mCurrentSessionQuestion.mCorrectAnswer +
                " mCurrentSessionQuestion.mOpponentAnswer = " + mCurrentSessionQuestion.mOpponentAnswer);

        if (mCurrentSessionQuestion.mCorrectAnswer == mCurrentSessionQuestion.mOpponentAnswer) {
            Log.d(TAG, "opponentAnswer() = " + String.valueOf(mCurrentSessionQuestion.mOpponentTimeOfAnswer));
            addPointsOpponent(mCurrentSessionQuestion.mOpponentTimeOfAnswer);
        }
    }

    /**
     *
     * @param time how much time I spend
     */
    public void addPointsMe(int time) {
        if (!mSessionQuestions.isEmpty()) {
            pointsMine += (20 - time);
        } else {
            pointsMine = pointsMine * 2;
        }
        callback.callbackCallMine(pointsMine);
    }

    /**
     *
     * @param time how much time opponent spends
     */
    public void addPointsOpponent(int time) {
        if (!mSessionQuestions.isEmpty()) {
            pointsOpponent += (20 - time);
        } else {
            pointsOpponent = pointsOpponent * 2;
        }
        callback.callbackCallOpponent(pointsOpponent);
    }

    public int getNumberOfRound() {
        return 6 - mSessionQuestions.size();
    }

    public boolean moveCurrentSessionQuestion() {

        if (!mSessionQuestions.isEmpty()) {
            mCurrentSessionQuestion = mSessionQuestions.poll();
            mIIsAnswered = false;
            mOpponentIsAnwered = false;
            return true;
        }

        return false;
    }

    public void sendData(Context c, int number, int time) {


//        Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(c);

        final JSONObject params = new JSONObject();
        try {
            JSONObject par = new JSONObject();
            par.put("time", time);
            par.put("answer_id", mCurrentSessionQuestion.getQuestion().getAnswer(number).mId);
            params.put("token", Mine.getInstance(c).getToken());
            params.put("game_session_question", par);

        } catch (JSONException e) {

        }
        Log.d(TAG, "PREPARING TO SEND PATCH");
        Log.d(TAG, "PREPARING, json: " + params.toString());
        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT,
                Mine.URL + "/game_session_questions/" + mCurrentSessionQuestion.mId, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "PATCH HAS SEND, response: " + response.toString());

                        //TODO: возможно, сделать, что от этого должно зависить будет ли запускаться следующий раунд
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
//                Log.d(TAG, "PATCH HAS SEND, MAYBE, status code: " + error.networkResponse.statusCode);
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 401:
                            Log.d(TAG, "Error 401" + error.getMessage());
                            break;
                    }
                    //Add cases
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
//        sendReqAlter(c, number, time);

    }

    private void sendReqAlter(Context c, int number, int time) {

        final JSONObject params = new JSONObject();
        try {
            JSONObject par = new JSONObject();
            par.put("time", time);
            par.put("answer_id", mCurrentSessionQuestion.getQuestion().getAnswer(number).mId);
            params.put("token", Mine.getInstance(c).getToken());
            params.put("game_session_question", par);

        } catch (JSONException e) {

        }
        Thread t = new Thread() {

            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;
                JSONObject json = new JSONObject();

                try {
                    URI uri = new URI(Mine.URL + "/game_session_questions/" + mCurrentSessionQuestion.mId);
                    HttpClientStack.HttpPatch post = new HttpClientStack.HttpPatch(uri);
//                    json.put("email", email);
//                    json.put("password", pwd);
                    StringEntity se = new StringEntity( params.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                    /*Checking response */
                    if(response!=null){
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                }

                Looper.loop(); //Loop in the message queue
            }
        };

        t.start();
    }


}
