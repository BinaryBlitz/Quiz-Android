package com.quiz.pavel.quiz.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Category;
import com.quiz.pavel.quiz.model.Mine;

/**
 * Created by pavel on 16/05/15.
 */
public class RTopicListActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final ActionBar actionBar = getSupportActionBar();

        Category cat = Mine.getInstance(this).loadCategoryAr(this).get(0);
        for (int i = 0; i < Mine.getInstance(this).loadCategoryAr(this).size(); i++) {
            if (getIntent().getIntExtra(RTopicListFragment.keyRTopicListFragment, 0) ==  Mine.getInstance(this)
                    .loadCategoryAr(this).get(i).getId()) {
                cat = Mine.getInstance(this).loadCategoryAr(this).get(i);
            }
        }
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(cat.getTitle());

        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new RTopicListFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.exit, R.anim.enter);
    }
}
