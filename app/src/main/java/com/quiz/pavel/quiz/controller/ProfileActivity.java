package com.quiz.pavel.quiz.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.quiz.pavel.quiz.R;

/**
 * Created by pavelkozemirov on 15.02.15.
 */
public class ProfileActivity extends FragmentActivity
        implements ProfileFragment.OnAddNewFragmentCallback{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = ProfileFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }



    @Override
    public void addFragment(int position) {
        FragmentManager fm = getSupportFragmentManager();

        ProfileFragment fragment = new ProfileFragment();

        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        ft.add(R.id.fragmentContainer, fragment);

        ft.addToBackStack(null);
        ft.commit();
    }
}
