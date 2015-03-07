package com.quiz.pavel.quiz.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.quiz.pavel.quiz.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pavel on 07/03/15.
 */
public class PostGameFragment extends Fragment {

    public final static String EXTRA = "extra_postGameFragment";

    @InjectView(R.id.textView) TextView mTextView;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_post_game, parent, false);
        ButterKnife.inject(this, v);

        switch (getActivity().getIntent().getIntExtra(EXTRA, 0)){
            case 1: mTextView.setText("ПОБЕДА");
                break;
            case -1: mTextView.setText("ПОРАЖЕНИЕ");
                break;
            case 0: mTextView.setText("НИЧЬЯ");
                break;
        }

        return v;
    }

}
