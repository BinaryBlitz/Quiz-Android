package com.quiz.pavel.quiz.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Category;
import com.quiz.pavel.quiz.model.IntentJSONSerializer;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.Session;
import com.quiz.pavel.quiz.model.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pavelkozemirov on 14.02.15.
 */
public class PreGameFragment extends Fragment {
    private static String TAG = "PreGameFragment";

    private static final String URL = "https://protected-atoll-5061.herokuapp.com";

    Timer mTimer;
    Handler myHandler = new Handler();

    public int mTopicId;
    private String mId;


    public static PreGameFragment newInstance() {
        Bundle args = new Bundle();
        PreGameFragment fragment = new PreGameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopicId = getActivity().getIntent().getIntExtra("topic", 0);
        Log.d(TAG, "(ONCREATE()) here calls createLobby() , mTopicId = after bundle" + mTopicId);
        createLobby();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pre_game, parent, false);

        return v;
    }

    public void startTimer() {

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                myHandler.post(myRunnable);
            }
        }, 0, 1000);

    }


    final Runnable myRunnable = new Runnable() {
        public void run() {
            sendReq();
        }
    };


    private void createLobby() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());



        JSONObject params = new JSONObject();
        Log.d(TAG, "mTopicID = " + mTopicId);
        try {
            JSONObject par = new JSONObject();
            //TODO: change constant id on value from previous activity
            par.put("topic_id", mTopicId);

            params.put("lobby", par);

            params.put("token", Mine.getInstance(getActivity()).getToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL + "/lobbies", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            mId = response.getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startTimer();

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "FUCKOFF, Error Response, have no data from server");
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
    }

    private void sendReq() {

        if (getActivity() == null) {
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        final SessionManager sm = SessionManager.getInstance(getActivity());                                                    //PUSHER




        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                URL + "/lobbies/" + mId + "/find?token=" + Mine.getInstance(getActivity()).getToken(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        sm.mSession = new Session(response);

                        try {
                            sm.online = !response.getBoolean("offline");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(sm.online){

                        sm.mChannel.bind("game-start", new SubscriptionEventListener() {
                            @Override
                            public void onEvent(String channel, String event, String data) {
                                Intent i = new Intent(getActivity(), SingleFragmentActivity.class);
                                startActivity(i);
                            }
                        });
                        } else{
                            Intent i = new Intent(getActivity(), SingleFragmentActivity.class);
                            startActivity(i);

                        }


                        if (mTimer == null) {
                            return;
                        }
                        mTimer.cancel();
                        mTimer.purge();


                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error Response, have no data from server");
            }
        });
        // Add the request to the
        queue.add(jsonRequest);

    }


}
