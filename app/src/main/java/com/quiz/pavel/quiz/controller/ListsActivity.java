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
public class ListsActivity extends FragmentActivity
        implements CategoryListFragment.OnCategorySelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = CategoryListFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }


    }

    public void onCategorySelected(int position) {


        // Create fragment and give it an argument for the selected article
        TopicListFragment newFragment = new TopicListFragment();
        Bundle args = new Bundle();

        args.putInt("n", position);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragmentContainer, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
