package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.quiz.pavel.quiz.model.IntentJSONSerializer;

/**
 * Created by pavelkozemirov on 24.01.15.
 */
public class StartActivity extends Activity {
    private static final String TAG = "StartActivity";

    private IntentJSONSerializer mIntentJSONSerializer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntentJSONSerializer = IntentJSONSerializer.getInitialize();
        mIntentJSONSerializer.setContext(this);

        try {
            Log.d(TAG, "json has parsed, = " + mIntentJSONSerializer.loadData());

        }catch(Exception e){
            Log.d(TAG, "jsonException");
        }

        Log.d(TAG, "mIntentJSONSerializer.hasAccount() = "+ mIntentJSONSerializer.hasAccount());

        if (mIntentJSONSerializer.hasAccount()) {

            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {

            Intent intent = new Intent(StartActivity.this, LoginSignupActivity.class);
            startActivity(intent);
            finish();

        }
    }
}

