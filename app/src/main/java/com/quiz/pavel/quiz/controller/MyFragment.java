package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.quiz.pavel.quiz.model.PlayerProfile;

/**
 * Created by pavel on 18/03/15.
 */
public class MyFragment extends Fragment {

    public String mTitle = "nothing";

    public MyFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mMyFragmentListenerCallback = (MyFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
        mMyFragmentListenerCallback.updateTitle();
    }

    public interface MyFragmentListener {
        public void back();
        public void updateTitle();
        public void openProfileFragment(PlayerProfile p);

    }

    public MyFragmentListener mMyFragmentListenerCallback;
}
