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
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pavel on 07/03/15.
 */
public class PostGameFragment extends Fragment {
    private final static String TAG = "PostGameFragment";
    public final static String EXTRA = "extra_postGameFragment";

    @InjectView(R.id.textView) TextView mTextView;
    @InjectView(R.id.background_post_game) LinearLayout mBackground;

    @InjectView(R.id.button_play_again) Button mButtonPlayAgain;
    @InjectView(R.id.choose_topic) Button mButtonChooseTopic;

    @InjectView(R.id.my_name_post) TextView mMyName;
    @InjectView(R.id.opponents_name_post) TextView mOpponentsName;

    @InjectView(R.id.my_points_textView) TextView mMyPointsTextView;
    @InjectView(R.id.opponents_points_textView) TextView mOpponentsPointsTextView;

    @InjectView(R.id.opponents_image_imageView) ImageView mOpponentImage;
    @InjectView(R.id.my_image_imageView) ImageView mMyImage;

    @InjectView(R.id.textView_points) TextView mTextViewPointsWord;
    @InjectView(R.id.textView_points_count) TextView mTextViewPointsCount;
    @InjectView(R.id.textView_points_overview) TextView mTextViewPointsOverview;


    DisplayImageOptions options;

    String mMyUsername;
    String mOpponentsUsername;
    String mMyUrl;
    String mOpponentsUrl;
    int mMyPoints;
    int mOpponentsPoints;

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


        try {
            JSONObject json = new JSONObject(getActivity().getIntent().getStringExtra(PostGameFragment.EXTRA));
            Log.d(TAG, json.toString());
            mMyUsername = json.getString("my_username");
            mOpponentsUsername = json.getString("opponents_username");
            mMyUrl = json.getString("my_url_avatar");
            mOpponentsUrl = json.getString("opponents_url_avatar");
            mMyPoints = json.getInt("my_points");
            mOpponentsPoints = json.getInt("opponents_points");
            String urlBackground = json.getString("url_background");
            setBackground(urlBackground);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAvatarsNames();

        return v;
    }

    private void setAvatarsNames() {


        if (mMyPoints > mOpponentsPoints) {
            mTextView.setText("ПОБЕДА");
        } else if (mOpponentsPoints > mMyPoints) {
            mTextView.setText("ПОРАЖЕНИЕ");
        } else {
            mTextView.setText("НИЧЬЯ");
        }

        String myname = mMyUsername;
        String opponentName = mOpponentsUsername;

        if (myname.length() > 10) {
            mMyName.setText(myname.substring(0, 8) + "...");
        } else {
            mMyName.setText(myname);
        }

        if (opponentName.length() > 10) {
            mOpponentsName.setText(opponentName.substring(0, 8) + "...");
        } else {
            mOpponentsName.setText(opponentName);
        }

        Picasso.with(getActivity())
                .load(Mine.URL_photo + mMyUrl)
                .placeholder(R.drawable.catty)
                .into(mMyImage);

        Picasso.with(getActivity())
                .load(Mine.URL_photo + mOpponentsUrl)
                .placeholder(R.drawable.catty)
                .into(mOpponentImage);

        mMyPointsTextView.setText(String.valueOf(mMyPoints));
        mOpponentsPointsTextView.setText(String.valueOf(mOpponentsPoints));


        mTextViewPointsCount.setText(String.valueOf(mMyPoints));
        mTextViewPointsWord.setText(findTextFor(mMyPoints));
        mTextViewPointsOverview.setText(String.valueOf(mMyPoints) + " " + findTextFor(mMyPoints));
    }

    private void setBackground(String url) {
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

    private String findTextFor(int num) {
        num = mMyPoints % 100;

        if (num > 20) {
            num = num % 10;
        }
        if (num == 0) {
            return "очков";
        } else if (num >= 5 && num <= 20) {
            return "очков";
        } else if (num == 1) {
            return "очко";
        } else {
            return "очка";
        }
    }

    @OnClick(R.id.button_play_again)
    public void onClick1() {
        YoYo.with(Techniques.Swing).duration(700).playOn(mButtonPlayAgain);
        getActivity().finish();
    }

    @OnClick(R.id.choose_topic)
    public void onClick2() {
        YoYo.with(Techniques.Swing).duration(700).playOn(mButtonChooseTopic);
        getActivity().finish();

    }

}
