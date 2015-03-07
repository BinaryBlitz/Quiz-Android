package com.quiz.pavel.quiz.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.quiz.pavel.quiz.R;

/**
 * Created by pavelkozemirov on 14.02.15.
 */
public class PreGameActivity extends FragmentActivity
        {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = PreGameFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }


    }

//    @Override
//    public void launchGame() {
//
//        PostGameFragment postGameFragment = new PostGameFragment();
//        FragmentManager fm = getSupportFragmentManager();
//
//        FragmentTransaction ft = fm.beginTransaction();
//
//        // Replace whatever is in the fragment_container view with this fragment,
//        // and add the transaction to the back stack so the user can navigate back
////        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
//
//        ft.replace(R.id.fragmentContainer, postGameFragment);
//
//        ft.commit();
//    }
}
