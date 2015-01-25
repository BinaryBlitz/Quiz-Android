package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.quiz.pavel.quiz.model.IntentJSONSerializer;

/**
 * Created by pavelkozemirov on 24.01.15.
 */
public class StartActivity extends Activity {


    private IntentJSONSerializer mIntentJSONSerializer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntentJSONSerializer = new IntentJSONSerializer(this);

        // Determine whether the current user is an anonymous user
        if (!mIntentJSONSerializer.hasAccount()) {
            // If user is anonymous, send the user to LoginSignupActivity.class
            Intent intent = new Intent(StartActivity.this,
                    LoginSignupActivity.class);
            startActivity(intent);
            finish();

        } else {

                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

        }
    }
}

