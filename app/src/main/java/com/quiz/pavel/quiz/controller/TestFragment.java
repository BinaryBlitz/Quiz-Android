package com.quiz.pavel.quiz.controller;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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


    @InjectView(R.id.variant_a_button) Button mVariantA;
    @InjectView(R.id.variant_b_button) Button mVariantB;
    @InjectView(R.id.variant_c_button) Button mVariantC;
    @InjectView(R.id.variant_d_button) Button mVariantD;

    @InjectView(R.id.question_text_view) TextView mQuestionTextView;
    @InjectView(R.id.timer_textView) TextView mTimerTextView;

    @InjectView(R.id.my_points_textView) TextView mMyPointsTextView;
    @InjectView(R.id.opponents_points_textView) TextView mOpponentsPointsTextView;

    @InjectView(R.id.buttons_broad) LinearLayout mButtonsBroad;



    public SessionManager mSessionManager;





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

        YoYo.with(Techniques.FadeIn).duration(2000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                myTimer = new Timer();
                myTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        timer++;
                        myHandler.post(myRunnable);
                    }
                }, 0, 1000);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(v);





        mSessionManager.mSession.callback = new Session.MyCallback() {

            @Override
            public void callbackCallMine(int i) {                                                               //CALLBACK
                mMyPointsTextView.setText(String.valueOf(i));
            }
            @Override
            public void callbackCallOpponent(int i) {                                                               //CALLBACK
                mOpponentsPointsTextView.setText(String.valueOf(i));
            }
        };



        update();    //TODO: remove update









        return v;
    }







    int round;

    private int timer;
    Handler myHandler = new Handler();
    Timer myTimer;
    private  int timeOfOpponentAnswer;





    final Runnable myRunnable = new Runnable() {
        public void run() {
            if(timer >= 10){
                mSessionManager.answerMine(-1, timer);
                update();       //TODO: remove update
                close();
            }


            Random r = new Random();


            if(timer == timeOfOpponentAnswer) {
                mSessionManager.answerOpponent(3 - r.nextInt(1), timeOfOpponentAnswer);
            }
            mTimerTextView.setText(String.valueOf(10 - timer));
        }
    };


    private void close(){

        YoYo.with(Techniques.FadeOut).duration(2000).playOn(mButtonsBroad);
        YoYo.with(Techniques.FadeOut).duration(2000).playOn(mQuestionTextView);
        YoYo.with(Techniques.FadeOut).duration(2000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                showRound();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(mTimerTextView);

    }


    private void open(){

        YoYo.with(Techniques.FadeIn).duration(2000).playOn(mButtonsBroad);
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(mQuestionTextView);
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(mTimerTextView);
        timer = 0;

    }

    private void showRound(){

        YoYo.with(Techniques.FadeIn).duration(500).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mQuestionTextView.setText("Round " + round);                                                    //bullshit
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                YoYo.with(Techniques.FadeOut).duration(500).playOn(mQuestionTextView);
                open();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(mButtonsBroad);

    }







    /**
    * Update question and variants of answer (new round)
     */
    private void update(){

        Random r = new Random();

        round++;

        timeOfOpponentAnswer = r.nextInt(10);

        timer = 0;

        myHandler.post(myRunnable);

        Question newQuestion;


        if(mSessionManager.newRound()){
            mSessionManager.nextRound();
            newQuestion = mSessionManager.getCurrentQuestion();
        } else{
            if(getActivity() != null) {
                getActivity().finish();
            }
            myTimer.cancel();

            return;
        }






            mQuestionTextView.setText(newQuestion.getText());

        String[] vars = newQuestion.getAnswersText();
        mVariantA.setText(vars[0]);
        mVariantB.setText(vars[1]);
        mVariantC.setText(vars[2]);
        mVariantD.setText(vars[3]);
        //open();
    }

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



    @OnClick(R.id.variant_a_button)
    public void onButtonAClick() {


        YoYo.with(Techniques.Swing)
                .duration(700)
                .playOn(mVariantA);

        mSessionManager.answerMine(0,timer);
        close();
        update();    //TODO: remove update
    }

    @OnClick(R.id.variant_b_button)
    public void onButtonBClick() {

        YoYo.with(Techniques.FadeIn)
                .duration(1500)
                .playOn(mVariantB);
        mSessionManager.answerMine(1, timer);
        close();

        update();       //TODO: remove update
    }

    @OnClick(R.id.variant_c_button)
    public void onButtonCClick() {

        YoYo.with(Techniques.Flash)
                .duration(700)
                .playOn(mVariantC);

        mSessionManager.answerMine(2, timer);
        close();

        update();           //TODO: remove update
    }

    @OnClick(R.id.variant_d_button)
    public void onButtonDClick() {


        YoYo.with(Techniques.Landing)
                .duration(600)
                .playOn(mVariantD);

        mSessionManager.answerMine(3, timer);
        close();

        update();           //TODO: remove update
    }



}
