package com.quiz.pavel.quiz.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;

/**
 * Created by pavelkozemirov on 15.02.15.
 */
public class ListsActivity extends ActionBarActivity
        implements  TopicListFragment.OnEventTopicListListener {
    private final static String TAG = "ListsActivity";

    ActionBar mActionBar;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mActionBar = getSupportActionBar();
        mActionBar.setTitle("Категории");

        mFragmentManager = getSupportFragmentManager();

        Fragment fragment = mFragmentManager.findFragmentById(R.id.fragmentContainer);

//        if (fragment == null) {
//            fragment = CategoryListFragment.newInstance();
//            mFragmentManager.beginTransaction()
//                    .add(R.id.fragmentContainer, fragment)
//                    .commit();
//        }

        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                update();
            }
        });
    }

//    @Override
//    public void onCategorySelected(int position) {
//        // Create fragment and give it an argument for the selected article
//        TopicListFragment newFragment = new TopicListFragment(position);
//
//        Bundle args = new Bundle();
//        args.putInt("number_of_category", position);
//
//        newFragment.setArguments(args);
//
//        mFragmentTransaction = mFragmentManager.beginTransaction();
//
//        // Replace whatever is in the fragment_container view with this fragment,
//        // and add the transaction to the back stack so the user can navigate back
//        mFragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
//
//        mFragmentTransaction.replace(R.id.fragmentContainer, newFragment);
//        mFragmentTransaction.addToBackStack(null);
//
//        // Commit the transaction
//        mFragmentTransaction.commit();
//    }

    private void update() {
        if(mFragmentManager.findFragmentById(R.id.fragmentContainer) instanceof CategoryListFragment) {
            mActionBar.setTitle("Категории");
            mActionBar.setDisplayHomeAsUpEnabled(false);
        } else {
            mActionBar.setTitle("Темы");
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void back() {
        mFragmentManager.popBackStack();
        Log.d(TAG, "BACK");
    }
}
