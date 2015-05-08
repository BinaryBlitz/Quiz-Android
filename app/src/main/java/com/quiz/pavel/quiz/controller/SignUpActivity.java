package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pavelkozemirov on 24.01.15.
 */

public class SignUpActivity extends Activity {
    private static String TAG = "LoginSignupActivity";

    String usernametxt;
    String passwordtxt;
    String mailtxt;

    @InjectView(R.id.signup)
    Button mSignUp;
    @InjectView(R.id.username)
    EditText mUsernameEditText;
    @InjectView(R.id.mail)
    EditText mMailEditText;
    @InjectView(R.id.password)
    EditText mPasswordEditText;



    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        setContentView(R.layout.signup);
        ButterKnife.inject(this);


    }

    @OnClick(R.id.signup)
    public void onClick() {
        usernametxt = mUsernameEditText.getText().toString();
        passwordtxt = mPasswordEditText.getText().toString();
        mailtxt = mMailEditText.getText().toString();
        if (usernametxt.equals("") && passwordtxt.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please complete the sign up form",
                    Toast.LENGTH_LONG).show();

        } else {
            RequestQueue queue = Volley.newRequestQueue(this);


            JSONObject params = new JSONObject();
            JSONObject json = new JSONObject();
            try {
                json.put("username", usernametxt);
                json.put("name", "nothing");
                json.put("password", passwordtxt);

                params.put("player", json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "params = " + params);

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Mine.URL + "/players", params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Mine.newInstance(getBaseContext(), response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(SignUpActivity.this, MainSlidingActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        switch (response.statusCode) {
                            case 422:
                                Log.d(TAG, "reponse data" + response.data);
                                Toast.makeText(getApplicationContext(), "Error: такой существует", Toast.LENGTH_SHORT).show();
                                break;
                        }
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
        }
    }
//
//    public static String md5(String input) {
//
//        String md5 = null;
//
//        if (null == input) return null;
//
//        try {
//
//            //Create MessageDigest object for MD5
//            MessageDigest digest = MessageDigest.getInstance("MD5");
//
//            //Update input string in message digest
//            digest.update(input.getBytes(), 0, input.length());
//
//            //Converts message digest value in base 16 (hex)
//            md5 = new BigInteger(1, digest.digest()).toString(16);
//
//        } catch (NoSuchAlgorithmException e) {
//
//            e.printStackTrace();
//        }
//        return md5;
//    }


}