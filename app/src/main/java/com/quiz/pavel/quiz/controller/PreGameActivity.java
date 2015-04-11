package com.quiz.pavel.quiz.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Session;

/**
 * Created by pavelkozemirov on 14.02.15.
 */
public class PreGameActivity extends ActionBarActivity {
    private static final String TAG = "PreGameActivity";

    public static final String EXTRA = "extra.pregameactivity";

    public Session mSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();

        int index = 0;
        index = getIntent().getIntExtra(EXTRA, 0);
        Log.d(TAG, "extra = " + index);

        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            if(index == 0) {
                fragment = PreGameFragment.newInstance();
            } else {
                fragment = ChallengePreGameFragment.newInstance();
            }

            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
        restoreActionBar();
    }
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BasePreGameFragment fragment = (BasePreGameFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        fragment.closeLobby();
        fragment.onDestroy();
    }

}
