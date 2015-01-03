package com.quiz.pavel.quiz.controller;

import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Question;
import com.quiz.pavel.quiz.model.Session;
import com.quiz.pavel.quiz.model.SessionManager;
import com.quiz.pavel.quiz.model.SessionQuestion;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.quiz.pavel.quiz.R.id.variant_a_button;

/**
 * Created by pavelkozemirov on 11.12.14.
 */
public class TestFragment extends Fragment {
    private static String TAG = "TestFragment";

    @InjectView(R.id.variant_a_button) Button mVariantA;
    @InjectView(R.id.variant_b_button) Button mVariantB;
    @InjectView(R.id.variant_c_button) Button mVariantC;
    @InjectView(R.id.variant_d_button) Button mVariantD;

    @InjectView(R.id.question_text_view) TextView mQuestionTextView;
    @InjectView(R.id.timer_textView) TextView mTimerTextView;

    @InjectView(R.id.my_points_textView) TextView mMyPointsTextView;
    @InjectView(R.id.opponents_points_textView) TextView mOpponentsPointsTextView;

    @InjectView(R.id.buttons_broad) LinearLayout mButtonsBroad;

    @InjectView(R.id.broad_my_profile) LinearLayout mMyProfileBroad;
    @InjectView(R.id.broad_opponent_profile) LinearLayout mOpponentProfileBroad;

    @InjectView(R.id.round_shower) TextView mRoundShowerTextView;

    private Button mLastPushedButton;


    public SessionManager mSessionManager;
    public Question mCurQuestion;



    public static TestFragment newInstance(){
        Bundle args = new Bundle();
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_test, parent, false);
        ButterKnife.inject(this,v);

        mSessionManager = SessionManager.newInstance();

        mSessionManager.startTimer();

        mLoadingView = v.findViewById(R.id.round_shower);
        mContentView = v.findViewById( R.id.question_text_view);

        mSessionManager.mSession.callback = new Session.MyCallback() {

            @Override
            public void callbackCallMine(int i) {

                mMyPointsTextView.setText(String.valueOf(i));
                mLastPushedButton.setBackgroundColor(0xff00ff00);
                //TODO: change to switch with new parameter as a condition

            }
            @Override
            public void callbackCallOpponent(int i) {
                //mOpponentsPointsTextView.setText(String.valueOf(i));
            }
        };

        mSessionManager.mCallbackOnView = new SessionManager.CallbackOnView() {
            @Override
            public void updateTimer(int i) {
                mTimerTextView.setText(String.valueOf(10 - i));
            }
            @Override
            public void closeRound(){

                updateData();
                updateGUI();

            }
            @Override
            public void openRound(){


            }
            @Override
            public void opponentChooseAnswer(int i){
                switch (i){
                    case 0:                 mVariantA.setBackgroundColor(0xffff0000);
                    case 1:                 mVariantB.setBackgroundColor(0xffff0000);
                    case 2:                 mVariantC.setBackgroundColor(0xffff0000);
                    case 3:                 mVariantD.setBackgroundColor(0xffff0000);
                        //TODO: NEEDS TO REWORK, SET a COLOR OF BACKGROUND AFTER FINISH OF A ROUND
                }
            }
        };
        mCurQuestion = mSessionManager.mSession.mCurrentSessionQuestion.getQuestion();

        updateGUI();


        return v;
    }


    private void updateData(){

            if(!mSessionManager.mSession.moveCurrentSessionQuestion()){
                getActivity().finish();
            }
            mCurQuestion = mSessionManager.mSession.mCurrentSessionQuestion.getQuestion();

    }

    private void showRoundTable(){
        mSessionManager.stopTimer();

        mLoadingView.setAlpha(0f);
        mLoadingView.setVisibility(View.VISIBLE);

        mLoadingView.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(null);

        mContentView.animate()
                .alpha(0f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        mContentView.setVisibility(View.GONE);
                        hideRoundTable();
                    }
                });

    }
    private void hideRoundTable(){
        mContentView.setAlpha(0f);
        mContentView.setVisibility(View.VISIBLE);

        mContentView.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(null);

        mLoadingView.animate()
                .alpha(0f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        mLoadingView.setVisibility(View.GONE);
                        justMethod();
                    }
                });
    }
    private void justMethod(){
        mSessionManager.startTimer();
        Log.d(TAG, "startTime() has been already worked");
    }

    private void updateGUI(){


        mRoundShowerTextView.setText("Round "+ mSessionManager.mSession.getNumberOfRound());
        showRoundTable();
        blockOfButtons = false;

        mQuestionTextView.setText(mCurQuestion.getText());

        String[] vars = mCurQuestion.getAnswersText();
        mVariantA.setText(vars[0]);
        mVariantB.setText(vars[1]);
        mVariantC.setText(vars[2]);
        mVariantD.setText(vars[3]);
        mVariantA.setBackgroundColor(getResources().getColor(R.color.button_material_light));
        mVariantB.setBackgroundColor(getResources().getColor(R.color.button_material_light));
        mVariantC.setBackgroundColor(getResources().getColor(R.color.button_material_light));
        mVariantD.setBackgroundColor(getResources().getColor(R.color.button_material_light));



    }


    private View mContentView;


    private View mLoadingView;


    @Override
    public void onDestroy(){                            //TODO: add launching new fragment with results of game
        super.onDestroy();
        String str = "you are loser";
        if(mSessionManager.amIWinner())
        {
            str = "you are winner";
        }
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();

    }

    private boolean blockOfButtons;

    @OnClick(R.id.variant_a_button)
    public void onButtonAClick() {
        YoYo.with(Techniques.Swing).duration(700).playOn(mVariantA);

        if(blockOfButtons){
            return;
        }
        blockOfButtons = true;
        mLastPushedButton = mVariantA;
        mSessionManager.iChooseAnswer(0);
        mVariantA.setBackgroundColor(0xffff0000);

    }

    @OnClick(R.id.variant_b_button)
    public void onButtonBClick() {
        YoYo.with(Techniques.Swing).duration(700).playOn(mVariantB);

        if(blockOfButtons){
            return;
        }
        blockOfButtons = true;

        mLastPushedButton = mVariantB;

        mSessionManager.iChooseAnswer(1);
        mVariantB.setBackgroundColor(0xffff0000);

    }

    @OnClick(R.id.variant_c_button)
    public void onButtonCClick() {
        YoYo.with(Techniques.Swing).duration(700).playOn(mVariantC);

        if(blockOfButtons){
            return;
        }
        blockOfButtons = true;

        mLastPushedButton = mVariantC;

        mSessionManager.iChooseAnswer(2);

        mVariantC.setBackgroundColor(0xffff0000);


    }

    @OnClick(R.id.variant_d_button)
    public void onButtonDClick() {

        YoYo.with(Techniques.Swing).duration(700).playOn(mVariantD);

        if(blockOfButtons){
            return;
        }
        blockOfButtons = true;

        mLastPushedButton = mVariantD;

        mSessionManager.iChooseAnswer(3);
        mVariantD.setBackgroundColor(0xffff0000);



    }



}
