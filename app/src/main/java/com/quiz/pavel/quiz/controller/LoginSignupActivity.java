package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Category;
import com.quiz.pavel.quiz.model.SessionQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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

public class LoginSignupActivity extends Activity {
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



    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        setContentView(R.layout.login_signup);
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
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);

            MessageDigest md = null;
            byte[] bytesOfMessage = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
            }
            try {
                 bytesOfMessage = "regreg".getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
            byte[] thedigest = md.digest(bytesOfMessage);

            JSONObject params = new JSONObject();
            try {
                params.put("name", usernametxt);
                params.put("email",mailtxt);
                params.put("password_digest", thedigest);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL + "/players", params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                String token = response.getString("token");
                            } catch (JSONException e) {

                            }
                            //TODO: wrap out token from response to Intent for MainActivity...

                            Intent intent = new Intent(LoginSignupActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG,"Error Response, have no data from server" );
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
        }
    }


}