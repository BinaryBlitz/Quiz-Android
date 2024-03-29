package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pavelkozemirov on 01.02.15.
 */
public class LogInActivity extends Activity {
    private static String TAG = "LoginActivity";

    String usernametxt;
    String passwordtxt;
    String mailtxt;
    @InjectView(R.id.login)
    Button mSignUp;
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
        setContentView(R.layout.login);
        ButterKnife.inject(this);


    }

    @OnClick(R.id.login)
    public void onClick() {
        passwordtxt = mPasswordEditText.getText().toString();
        mailtxt = mMailEditText.getText().toString();

        if (mailtxt.equals("") && passwordtxt.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please complete the sign up form",
                    Toast.LENGTH_LONG).show();

        } else {


            RequestQueue queue = Volley.newRequestQueue(this);


            JSONObject params = new JSONObject();
            try {
                params.put("email", mailtxt);
                params.put("password_digest", md5(passwordtxt));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "LOGIN, email:" + mailtxt + " pass:" + md5(passwordtxt));

            // Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                    Mine.URL + "/players/authenticate", params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            //TODO: wrap out token from response to Intent for MainActivity...


                            try {
                                Mine.newInstance(getBaseContext(), response);
                            } catch (Exception e) {
                                Log.d(TAG, "problems with SaveData()");
                            }

                            Intent intent = new Intent(LogInActivity.this,
                                    MainSlidingActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "FUCKOFF, Error Response, have no data from server");
//                    NetworkResponse response = error.networkResponse;
//                    if(response != null && response.data != null){
//                        switch(response.statusCode){
//                            case 422:
//                                Toast.makeText(getApplicationContext(), "Error: такой существует", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                        //Additional cases
//                    }
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

        if (null == input) return null;

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
