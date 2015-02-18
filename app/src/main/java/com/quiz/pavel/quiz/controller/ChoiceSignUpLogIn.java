package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.quiz.pavel.quiz.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pavelkozemirov on 01.02.15.
 */
public class ChoiceSignUpLogIn extends Activity {

    @InjectView(R.id.signup_choice)
    Button mSignUp;
    @InjectView(R.id.login_choice)
    Button mLogIn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        setContentView(R.layout.choice_sign_log);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.login_choice)
    public void onClick1() {
        Intent intent = new Intent(ChoiceSignUpLogIn.this, LogInActivity.class);
        startActivity(intent);

    }


    @OnClick(R.id.signup_choice)
    public void onClick2() {
        Intent intent = new Intent(ChoiceSignUpLogIn.this, SignUpActivity.class);
        startActivity(intent);

    }

}
