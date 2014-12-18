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



    public SessionManager mSessionManager;
    private SessionQuestion mSessionQuestion;




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

        update();



        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                i++;
                myHandler.post(myRunnable);
            }
        }, 0, 1000);





        return v;
    }

    private int i;
    Handler myHandler = new Handler();
    Timer myTimer;






    final Runnable myRunnable = new Runnable() {
        public void run() {
            if(i >= 10){
                update();
            }
            mTimerTextView.setText(String.valueOf(10 - i));
        }
    };


    /**
    * Update question and variants of answer
     */
    private void update(){
        i = 0;
        myHandler.post(myRunnable);
        Question newQuestion = null;
        try {
            newQuestion = mSessionManager.getCurrentQuestion();
        }catch(Exception ex){
            getActivity().finish();
            return;
        }

        mQuestionTextView.setText(newQuestion.getText());

        String[] vars = newQuestion.getAnswers();
        mVariantA.setText(vars[0]);
        mVariantB.setText(vars[1]);
        mVariantC.setText(vars[2]);
        mVariantD.setText(vars[3]);
    }




    @OnClick(R.id.variant_a_button)
    public void onButtonAClick() {
        update();
    }

    @OnClick(R.id.variant_b_button)
    public void onButtonBClick() {
        update();
    }

    @OnClick(R.id.variant_c_button)
    public void onButtonCClick() {
        update();
    }

    @OnClick(R.id.variant_d_button)
    public void onButtonDClick() {
        update();
    }



}
