package com.quiz.pavel.quiz.controller;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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



        update();



        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer++;
                myHandler.post(myRunnable);
            }
        }, 0, 1000);



        return v;
    }




    private int timer;
    Handler myHandler = new Handler();
    Timer myTimer;
    private  int timeOfOpponentAnswer;





    final Runnable myRunnable = new Runnable() {
        public void run() {
            if(timer >= 10){
                mSessionManager.answerMine(-1, timer);
                update();
            }


            Random r = new Random();


            if(timer == timeOfOpponentAnswer) {
                mSessionManager.answerOpponent(3 - r.nextInt(1), timeOfOpponentAnswer);
            }
            mTimerTextView.setText(String.valueOf(10 - timer));
        }
    };


    /**
    * Update question and variants of answer (new round)
     */
    private void update(){
        Random r = new Random();

        timeOfOpponentAnswer = r.nextInt(10);

        timer = 0;
        myHandler.post(myRunnable);

        Question newQuestion;


        if(mSessionManager.newRound()){
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
    }

    @Override
    public void onDestroy(){
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
        mSessionManager.answerMine(0,timer);
        update();
    }

    @OnClick(R.id.variant_b_button)
    public void onButtonBClick() {
        mSessionManager.answerMine(1, timer);
        update();
    }

    @OnClick(R.id.variant_c_button)
    public void onButtonCClick() {
        mSessionManager.answerMine(2, timer);
        update();
    }

    @OnClick(R.id.variant_d_button)
    public void onButtonDClick() {
        mSessionManager.answerMine(3, timer);
        update();
    }



}
