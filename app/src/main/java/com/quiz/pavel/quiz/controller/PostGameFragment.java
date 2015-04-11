package com.quiz.pavel.quiz.controller;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pavel on 07/03/15.
 */
public class PostGameFragment extends Fragment {

    public final static String EXTRA = "extra_postGameFragment";

    @InjectView(R.id.textView) TextView mTextView;

    @InjectView(R.id.background_post_game)
    LinearLayout mBackground;


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

        setBackground();

        return v;
    }

    private void setBackground() {
        String url = Mine.URL_photo + Mine.getInstance(getActivity())
                .loadCategoryAr(getActivity()).get(1).mBackgroundUrl;
//        try {
//            mBackground.setBackgroundDrawable(new BitmapDrawable(getResources(), Picasso.with(getActivity()).load(url).get() ));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                mBackground.setBackground(drawable);

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(getActivity()).load(url).into(target);
    }
}
