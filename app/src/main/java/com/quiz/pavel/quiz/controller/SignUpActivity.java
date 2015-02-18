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
import com.quiz.pavel.quiz.model.IntentJSONSerializer;
import com.quiz.pavel.quiz.model.Mine;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    // Declare Variables
    String usernametxt;
    String passwordtxt;
    String mailtxt;
    @InjectView(R.id.signup) Button mSignUp;
    @InjectView(R.id.username) EditText mUsernameEditText;
    @InjectView(R.id.mail) EditText mMailEditText;
    @InjectView(R.id.password) EditText mPasswordEditText;
    private static final String URL = "https://protected-atoll-5061.herokuapp.com";


    private IntentJSONSerializer mIntentJSONSerializer;

    /** Called when the activity is first created. */
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
            try {
                params.put("name", usernametxt);
                params.put("email", mailtxt);
                params.put("password_digest", md5(passwordtxt));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "SIGNUP,pass = "+ md5(passwordtxt));

            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL + "/players", params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            try {
                                Mine.getInstance(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(SignUpActivity.this,
                                    MainTabsActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Sign up Error Response, have no data from server");
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null){
                        switch(response.statusCode){
                            case 422:
                                Toast.makeText(getApplicationContext(), "Error: такой существует", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        //Additional cases
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
    public static String md5(String input) {

        String md5 = null;

        if(null == input) return null;

        try {

            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            //Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        return md5;
    }


}