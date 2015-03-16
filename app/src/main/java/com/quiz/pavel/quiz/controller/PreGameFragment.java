package com.quiz.pavel.quiz.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.Session;
import com.quiz.pavel.quiz.model.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pavelkozemirov on 14.02.15.
 */
public class PreGameFragment extends Fragment {

    private static String TAG = "PreGameFragment";

    Timer mTimer;

    public int mTopicId;
    private String mId;

    @InjectView(R.id.name_of_topic) TextView mNameOfTopic;
    @InjectView(R.id.interesting_fact) TextView mInterestingFact;

    public static PreGameFragment newInstance() {
        Bundle args = new Bundle();
        PreGameFragment fragment = new PreGameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = new SessionManager(getActivity());
        sm.mPusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("GOO State changed to " + change.getCurrentState() +
                        " from " + change.getPreviousState());

                if (String.valueOf(change.getCurrentState()) == "CONNECTED") {
                    createLobby();
                }
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("GOO There was a problem connecting!");
            }
        }, ConnectionState.ALL);
        mTopicId = getActivity().getIntent().getIntExtra("topic", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pre_game1, parent, false);

        ButterKnife.inject(this, v);

        String name = getActivity().getIntent().getStringExtra("name");
        mNameOfTopic.setText(name);
        mInterestingFact.setText("Интересный факт...");

        return v;
    }

    int limit;

    public void startTimer() {
        mTimer = new Timer();
        limit = 0;
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "executing action from schedule: " + limit);
                sendReq();   // Question
                limit++;
                if (limit >= 12) {
                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer.purge();
                    }
                    sm.mPusher.disconnect();
                }
            }
        }, 0, 2000);
    }

    private void createLobby() {
        if (getActivity() == null) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JSONObject params = new JSONObject();

        try {
            JSONObject par = new JSONObject();
            par.put("topic_id", mTopicId);

            params.put("lobby", par);

            params.put("token", Mine.getInstance(getActivity()).getToken());

        } catch (JSONException e) {
            Log.d(TAG, "Problem with parsing json(Intent)");
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Mine.URL + "/lobbies",
                params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "LOBBY HAS BEEN CREATED");
                try {
                    mId = response.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listenEventChannel();

                startTimer();
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error Response, have no data from server");
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
        queue.add(stringRequest);
    }

    private void listenEventChannel() {
        if (sm.mPusher.getConnection().getState() == ConnectionState.CONNECTED) {
            sm.mChannel.bind("game-start", new SubscriptionEventListener() {
                @Override
                public void onEvent(String channel, String event, String data) {
                    Log.d(TAG, "EVENT HAS BEEN SEND!!!");
                    myHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "onEvent has handled");
                            if (flagResponse) {
                                Intent i = new Intent(getActivity(), SingleFragmentActivity.class);
                                startActivity(i);
                                sm.online = true;

                                Log.d(TAG, "launch a game");
                                closeThis();
                            } else {
                                myCallbackOnResponse = new OnResponse() {
                                    @Override
                                    public void responseWasGot() {
                                        Intent i = new Intent(getActivity(), SingleFragmentActivity.class);
                                        startActivity(i);
                                        sm.online = true;
                                        Log.d(TAG, "launch a game");
                                        closeThis();
                                    }
                                };
                            }
                        }
                    }, 1000);
                }
            });
        }
    }

    private OnResponse myCallbackOnResponse;

    public interface OnResponse {
        void responseWasGot();
    }

    SessionManager sm;
    boolean flagResponse;

    Handler myHandler = new Handler();

    private void sendReq() {
        if (getActivity() == null) {
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                Mine.URL + "/lobbies/" + mId + "/find?token=" + Mine.getInstance(getActivity())
                        .getToken(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "RESPONSE HAS BEEN GOT");
                        flagResponse = true;

                        sm.mSession = new Session(getActivity(), response);

                        try {
                            sm.online = !response.getBoolean("offline");
                        } catch (JSONException e) {
                            Log.d(TAG, "Problem with parsing json from response");
                        }


                        if (!sm.online) {
                            launchOfflineGame();
                        }

                        if (myCallbackOnResponse != null) {
                            myCallbackOnResponse.responseWasGot();
                        }

                        if (mTimer != null) {
                            mTimer.cancel();
                            mTimer.purge();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error Response, have no data from server(Searching...)");
            }
        });
        queue.add(jsonRequest);
    }

    private void launchOfflineGame() {
        Intent i = new Intent(getActivity(), SingleFragmentActivity.class);
        startActivity(i);

        sm.mPusher.unsubscribe("player-session-" + Mine.getInstance(getActivity()).getId());
        sm.online = false;

        sm.mPusher.disconnect();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
        closeThis();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sm.stopTimer();

        if (!sm.online) {
            if (sm.mPusher != null) {
                sm.mPusher.unsubscribe("player-session-" + Mine.getInstance(getActivity()).getId());
                sm.mPusher.disconnect();
            }
        }

        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
    }

    public void closeLobby() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT,
                Mine.URL + "/lobbies/" + mId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "PATCH HAS BEEN SEND, and LOBBY HAS BEEB CLOSED");
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "closing lobby request - error, perhaps");
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

    private void closeThis() {
        getActivity().finish();
    }


}
