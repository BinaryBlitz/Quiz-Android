package com.quiz.pavel.quiz.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.PlayerProfile;

/**
 * Created by pavelkozemirov on 15.02.15.
 */
public class ProfileActivity extends ActionBarActivity
        implements ProfileFragment.OnAddNewFragmentCallback{

    private static final String TAG = "ProfileFragment";

    ActionBar mActionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);

        mActionBar = getSupportActionBar();
        mActionBar.setTitle(Mine.getInstance(this).getName());


        fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new ProfileFragment(new PlayerProfile(this, Mine.getInstance(this).getId(),
                    Mine.getInstance(this).getName()));
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }

        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                update();
                method();
            }
        });
    }

    private void method() {
        currentFragment = (ProfileFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        mActionBar.setTitle(currentFragment.mPlayerProfile.getName());
        Log.d(TAG, "ONBACKSTACKCHANGEDLISTENER");
    }

    FragmentManager fm;
    ProfileFragment currentFragment;
    FragmentTransaction ft;

    @Override
    public void addFragment(PlayerProfile p) {

        mActionBar.setTitle(p.getName());
//        Log.d(TAG, "id = " + id);

        ProfileFragment fragment = new ProfileFragment(p);
//        fragment.mPlayerProfile = player;



        ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        ft.replace(R.id.fragmentContainer, fragment);

        ft.addToBackStack(null);
        ft.commit();
    } 

    private void update() {
        if(fm.getBackStackEntryCount() == 0) {
            Log.d(TAG, "getBackStackentrycount = 0");
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setTitle(Mine.getInstance(this).getName());
        } else {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void removeFragment() {
        fm.popBackStack();
    }

    @Override
    public void setTitle(String str) {
    }


}
