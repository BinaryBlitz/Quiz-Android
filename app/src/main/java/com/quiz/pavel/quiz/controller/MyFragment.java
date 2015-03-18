package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.quiz.pavel.quiz.model.Mine;

/**
 * Created by pavel on 18/03/15.
 */
public class MyFragment extends Fragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mMyFragmentListenerCallback = (MyFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public interface MyFragmentListener {
        public void back();
    }

    MyFragmentListener mMyFragmentListenerCallback;
}
