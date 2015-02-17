package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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


        if (mIntentJSONSerializer.hasAccount()) {

            Intent intent = new Intent(StartActivity.this, MainTabsActivity.class);
            startActivity(intent);
            finish();

        } else {

            Intent intent = new Intent(StartActivity.this, ChoiceSignUpLogIn.class);
            startActivity(intent);
            finish();

        }
    }
}

