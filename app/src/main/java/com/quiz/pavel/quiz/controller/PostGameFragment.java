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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pavel on 07/03/15.
 */
public class PostGameFragment extends Fragment {

    public final static String EXTRA = "extra_postGameFragment";

    @InjectView(R.id.textView) TextView mTextView;
    @InjectView(R.id.background_post_game) LinearLayout mBackground;

    @InjectView(R.id.button_play_again) Button mButtonPlayAgain;
    @InjectView(R.id.choose_topic) Button mButtonChooseTopic;

    DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
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
                .loadCategoryAr(getActivity()).get(0).mBackgroundUrl;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().loadImage(url, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Drawable drawable = new BitmapDrawable(getResources(), loadedImage);
                mBackground.setBackground(drawable);
            }
        });
    }

    @OnClick(R.id.button_play_again)
    public void onClick1() {
        YoYo.with(Techniques.Swing).duration(700).playOn(mButtonPlayAgain);
    }

    @OnClick(R.id.choose_topic)
    public void onClick2() {
        YoYo.with(Techniques.Swing).duration(700).playOn(mButtonChooseTopic);
    }

}
