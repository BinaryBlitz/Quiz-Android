package com.quiz.pavel.quiz.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Question;
import com.quiz.pavel.quiz.model.Session;
import com.quiz.pavel.quiz.model.SessionQuestion;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.quiz.pavel.quiz.R.id.variant_a_button;

/**
 * Created by pavelkozemirov on 11.12.14.
 */
public class TestFragment extends Fragment {


    @InjectView(variant_a_button) Button mVariantA;
    @InjectView(R.id.variant_b_button) Button mVariantB;
    @InjectView(R.id.variant_c_button) Button mVariantC;
    @InjectView(R.id.variant_d_button) Button mVariantD;

    private TextView mQuestionTextView;




    public Session mSession;
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
        mQuestionTextView = (TextView)v.findViewById(R.id.question_text_view);

        Question demo = new Question();
        mQuestionTextView.setText(demo.getText());



        mSession = Session.newInstance();

        mSessionQuestion = mSession.getSessionQuestion();
        mQuestionTextView.setText(mSessionQuestion.getText());
        String[] vars = mSessionQuestion.getQuestion().getVariants();

        mVariantA.setText(vars[0]);
        mVariantB.setText(vars[1]);
        mVariantC.setText(vars[2]);
        mVariantD.setText(vars[3]);

        mVariantA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSessionQuestion = mSession.getSessionQuestion();
                if(mSessionQuestion != null) {
                    mQuestionTextView.setText(mSessionQuestion.getText());
                }
                String[] vars = mSessionQuestion.getQuestion().getVariants();

                mVariantA.setText(vars[0]);
                mVariantB.setText(vars[1]);
                mVariantC.setText(vars[2]);
                mVariantD.setText(vars[3]);
            }
        });




        return v;
    }




}
