package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.quiz.pavel.quiz.model.IntentJSONSerializer;
import com.quiz.pavel.quiz.model.Mine;

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

        if (Mine.getInstance(this).isSignIn(this)) {

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
