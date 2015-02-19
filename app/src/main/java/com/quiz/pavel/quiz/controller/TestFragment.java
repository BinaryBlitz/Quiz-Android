package com.quiz.pavel.quiz.controller;

import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tagmanager.Container;
import com.nineoldandroids.animation.Animator;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.IntentJSONSerializer;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.Question;
import com.quiz.pavel.quiz.model.Session;
import com.quiz.pavel.quiz.model.SessionManager;
import com.quiz.pavel.quiz.model.SessionQuestion;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.quiz.pavel.quiz.R.id.variant_a_button;

/**
 * Created by pavelkozemirov on 11.12.14.
 *
 * @author Pavel Kozemirov
 * @version 1.0
 */
public class TestFragment extends Fragment {
    private static String TAG = "TestFragment";

    @InjectView(R.id.variant_a_button)
    Button mVariantA;
    @InjectView(R.id.variant_b_button)
    Button mVariantB;
    @InjectView(R.id.variant_c_button)
    Button mVariantC;
    @InjectView(R.id.variant_d_button)
    Button mVariantD;

    @InjectView(R.id.question_text_view)
    TextView mQuestionTextView;
    @InjectView(R.id.timer_textView)
    TextView mTimerTextView;

    @InjectView(R.id.my_points_textView)
    TextView mMyPointsTextView;
    @InjectView(R.id.opponents_points_textView)
    TextView mOpponentsPointsTextView;

    @InjectView(R.id.buttons_broad)
    LinearLayout mButtonsBroad;

    @InjectView(R.id.broad_my_profile)
    LinearLayout mMyProfileBroad;
    @InjectView(R.id.broad_opponent_profile)
    LinearLayout mOpponentProfileBroad;

    @InjectView(R.id.round_shower)
    TextView mRoundShowerTextView;

    private Button mLastPushedButton; //TODO: delete this line


    public SessionManager mSessionManager;
    public Question mCurQuestion;
    private String mDataSession;


    public static TestFragment newInstance() {
        Bundle args = new Bundle();
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_test, parent, false);
        ButterKnife.inject(this, v);



        mSessionManager = SessionManager.getInstance(getActivity());
        mQuestionTextView.setVisibility(View.GONE);
        mRoundShowerTextView.setVisibility(View.GONE);
        mButtonsBroad.setAlpha(0f);


        mSessionManager.mSession.callback = new Session.MyCallback() {

            @Override
            public void callbackCallMine(int i) {

                mMyPointsTextView.setText(String.valueOf(i));

            }

            @Override
            public void callbackCallOpponent(int i) {
                mOpponentsPointsTextView.setText(String.valueOf(i));
            }

//            @Override
//            public void downloadCompleted(){
//                beginGame();
//            }
        };

        mSessionManager.mCallbackOnView = new SessionManager.CallbackOnView() {

            @Override
            public void updateTimer(int i) {
                mTimerTextView.setText(String.valueOf(10 - i));
            }

            @Override
            public void closeRound() {

                onCloseRound();

            }

            @Override
            public void openRound() {

            }

            @Override
            public void opponentChooseAnswer(int i) {

            }
        };


        beginGame();
        return v;
    }

    private void beginGame() {

        mCurQuestion = mSessionManager.mSession.mCurrentSessionQuestion.getQuestion();

        updateGUI();
    }


    private void onCloseRound() {

        mSessionManager.stopTimer();

        Log.d(TAG, "HERE1 mOpponentAnswer = " + mSessionManager.mSession.mCurrentSessionQuestion.mOpponentAnswer);
        switch (mSessionManager.mSession.mCurrentSessionQuestion.mOpponentAnswer) {
            case 0:
                mVariantA.setTextColor(Color.RED);
                break;
            case 1:
                mVariantB.setTextColor(Color.RED);
                break;
            case 2:
                mVariantC.setTextColor(Color.RED);
                break;
            case 3:
                mVariantD.setTextColor(Color.RED);
                break;

        }
        Log.d(TAG, "HERE1 mCorrectAnswer = " + mSessionManager.mSession.mCurrentSessionQuestion.mCorrectAnswer);
        switch (mSessionManager.mSession.mCurrentSessionQuestion.mCorrectAnswer) {
            case 0:
                mVariantA.setTextColor(Color.GREEN);
                break;
            case 1:
                mVariantB.setTextColor(Color.GREEN);
                break;
            case 2:
                mVariantC.setTextColor(Color.GREEN);
                break;
            case 3:
                mVariantD.setTextColor(Color.GREEN);
                break;

        }
        hideIncorrectAnswerVariants();
    }

    // hide buttons of incorrect answers using animation
    Button b1, b2, b3;

    private void hideIncorrectAnswerVariants() {

        Log.d(TAG, "HERE1 mCorrectAnswer= " + mSessionManager.mSession.mCurrentSessionQuestion.mCorrectAnswer);
        switch (mSessionManager.mSession.mCurrentSessionQuestion.mCorrectAnswer) {
            case 0:
                b1 = mVariantB;
                b2 = mVariantC;
                b3 = mVariantD;
                break;
            case 1:
                b1 = mVariantA;
                b2 = mVariantC;
                b3 = mVariantD;
                break;
            case 2:
                b1 = mVariantB;
                b2 = mVariantA;
                b3 = mVariantD;
                break;
            case 3:
                b1 = mVariantB;
                b2 = mVariantC;
                b3 = mVariantA;
                break;
        }
        b1.setAlpha(1f);
        YoYo.with(Techniques.FadeOut).delay(1000).duration(1000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                b1.setAlpha(0f);
                hideCorrectQuestion();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(b1);

        b2.setAlpha(1f);
        YoYo.with(Techniques.FadeOut).delay(1000).duration(1000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                b2.setAlpha(0f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(b2);

        b3.setAlpha(1f);
        YoYo.with(Techniques.FadeOut).delay(1000).duration(1000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                b3.setAlpha(0f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(b3);


    }


    //hide correct answer's variant and question textview
    Button b4;
    TextView tv;

    private void hideCorrectQuestion() {

        tv = mQuestionTextView;

        switch (mSessionManager.mSession.mCurrentSessionQuestion.mCorrectAnswer) {
            case 0:
                b4 = mVariantA;
                break;
            case 1:
                b4 = mVariantB;
                break;
            case 2:
                b4 = mVariantC;
                break;
            case 3:
                b4 = mVariantD;
                break;
        }
        YoYo.with(Techniques.FadeOut).delay(1000).duration(1000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                tv.setAlpha(0f);
                updateData();
                updateGUI();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(tv);
        YoYo.with(Techniques.FadeOut).delay(1000).duration(1000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                b4.setAlpha(0f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(b4);
    }

    /**
     * Update data for UI. {@code mSessionManager != null }
     */
    private void updateData() {

        if (!mSessionManager.mSession.moveCurrentSessionQuestion()) {
            mSessionManager.stopTimer();
            if (getActivity() == null) {
                return;
            }
            getActivity().finish();
            return;
        }
        mCurQuestion = mSessionManager.mSession.mCurrentSessionQuestion.getQuestion();

    }

    private void showRoundTable() {
        mSessionManager.stopTimer();

        mRoundShowerTextView.setAlpha(0f);
        mRoundShowerTextView.setVisibility(View.VISIBLE);

        mRoundShowerTextView.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        hideRoundTable();
                    }
                });
//
//        mQuestionShower.animate()
//                .alpha(0f)
//                .setDuration(1000)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(android.animation.Animator animation) {
//                        mQuestionShower.setVisibility(View.GONE);
//                        hideRoundTable();
//                    }
//                });

    }

    private void hideRoundTable() {
//        mQuestionShower.setAlpha(0f);
//        mQuestionShower.setVisibility(View.VISIBLE);

//        mQuestionShower.animate()
//                .alpha(1f)
//                .setDuration(1000)
//                .setListener(null);

        mRoundShowerTextView.animate()
                .alpha(0f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        mRoundShowerTextView.setVisibility(View.GONE);
                        showNewQuestion();
                    }
                });
    }

    private void showNewQuestion() {

        mQuestionTextView.setAlpha(0f);
        mQuestionTextView.setVisibility(View.VISIBLE);

        mQuestionTextView.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        showAnswerVariants();
                    }
                });


    }

    private void showAnswerVariants() {

        mButtonsBroad.setVisibility(View.VISIBLE);
        mButtonsBroad.setAlpha(1f);
        mVariantA.setVisibility(View.VISIBLE);
        mVariantB.setVisibility(View.VISIBLE);
        mVariantC.setVisibility(View.VISIBLE);
        mVariantD.setVisibility(View.VISIBLE);
        mVariantA.setAlpha(0f);
        mVariantB.setAlpha(0f);
        mVariantC.setAlpha(0f);
        mVariantD.setAlpha(0f);


        YoYo.with(Techniques.FadeIn).duration(1000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mSessionManager.startTimer(0);
                mVariantA.setAlpha(1f);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(mVariantA);
        YoYo.with(Techniques.FadeIn).duration(1000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mVariantB.setAlpha(1f);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(mVariantB);
        YoYo.with(Techniques.FadeIn).duration(1000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mVariantC.setAlpha(1f);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(mVariantC);
        YoYo.with(Techniques.FadeIn).duration(1000).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mVariantD.setAlpha(1f);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(mVariantD);


//        mButtonsBroad.animate()
//                .alpha(1f)
//                .setDuration(1000)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(android.animation.Animator animation) {
//                        mSessionManager.startTimer(0);
//                        mVariantA.setAlpha(1f);
//                        mVariantB.setAlpha(1f);
//                        mVariantC.setAlpha(1f);
//                        mVariantD.setAlpha(1f);
//
//                    }
//                });
    }

    private void updateGUI() {


        mRoundShowerTextView.setText("Round " + mSessionManager.mSession.getNumberOfRound());
        showRoundTable();
        blockOfButtons = false;

        mQuestionTextView.setText(mCurQuestion.getText());

        String[] vars = mCurQuestion.getAnswersText();
        mVariantA.setText(vars[0]);
        mVariantB.setText(vars[1]);
        mVariantC.setText(vars[2]);
        mVariantD.setText(vars[3]);
        mVariantA.setTextColor(Color.BLACK);
        mVariantB.setTextColor(Color.BLACK);
        mVariantC.setTextColor(Color.BLACK);
        mVariantD.setTextColor(Color.BLACK);


    }


    @Override
    public void onDestroy() {                            //TODO: add launching new fragment with results of game
        super.onDestroy();
        String str = "you are loser";
        mSessionManager.stopTimer();
        if (mSessionManager.amIWinner()) {
            str = "you are winner";
        }
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        SessionManager.deleteInstance();
        mSessionManager.mPusher.disconnect();

        //TODO: perhaps, recomment
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//
//
//        // Request a string response from the provided URL.
//        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PATCH,
//                Mine.URL + "/game_session_questions/" + mSessionManager.mSession.
//                        mCurrentSessionQuestion.mId + "/close", null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        //TODO: возможно, сделать, что от этого должно зависить будет ли запускаться следующий раунд
//                    }
//                }
//                , new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                NetworkResponse response = error.networkResponse;
//                if (response != null && response.data != null) {
//                    switch (response.statusCode) {
//                        case 401:
//                            Log.d(TAG, "Error 401" + error.getMessage());
//                            break;
//                    }
//                    //Additional cases
//                }
//            }
//        }){
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("Content-Type","application/json");
//                params.put("Accept","application/json");
//                return params;
//            }
//        };
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);
    }

    private boolean blockOfButtons;

    @OnClick(R.id.variant_a_button)
    public void onButtonAClick() {
        if (blockOfButtons) {
            return;
        }
        YoYo.with(Techniques.Swing).duration(700).playOn(mVariantA);


        blockOfButtons = true;
        mLastPushedButton = mVariantA;
        mSessionManager.iChooseAnswer(getActivity(), 0);
        if (mSessionManager.mSession.mCurrentSessionQuestion.mCorrectAnswer == 0) {
            mVariantA.setTextColor(Color.GREEN);
        } else {
            mVariantA.setTextColor(Color.RED);
        }
    }

    @OnClick(R.id.variant_b_button)
    public void onButtonBClick() {
        if (blockOfButtons) {
            return;
        }
        YoYo.with(Techniques.Swing).duration(700).playOn(mVariantB);


        blockOfButtons = true;

        mLastPushedButton = mVariantB;

        mSessionManager.iChooseAnswer(getActivity(), 1);
        if (mSessionManager.mSession.mCurrentSessionQuestion.mCorrectAnswer == 1) {
            mVariantB.setTextColor(Color.GREEN);
        } else {
            mVariantB.setTextColor(Color.RED);
        }
    }

    @OnClick(R.id.variant_c_button)
    public void onButtonCClick() {
        if (blockOfButtons) {
            return;
        }
        YoYo.with(Techniques.Swing).duration(700).playOn(mVariantC);


        blockOfButtons = true;

        mLastPushedButton = mVariantC;

        mSessionManager.iChooseAnswer(getActivity(), 2);

        if (mSessionManager.mSession.mCurrentSessionQuestion.mCorrectAnswer == 2) {
            mVariantC.setTextColor(Color.GREEN);
        } else {
            mVariantC.setTextColor(Color.RED);
        }

    }

    @OnClick(R.id.variant_d_button)
    public void onButtonDClick() {
        if (blockOfButtons) {
            return;
        }

        YoYo.with(Techniques.Swing).duration(700).playOn(mVariantD);


        blockOfButtons = true;

        mLastPushedButton = mVariantD;

        mSessionManager.iChooseAnswer(getActivity(), 3);

        if (mSessionManager.mSession.mCurrentSessionQuestion.mCorrectAnswer == 3) {
            mVariantD.setTextColor(Color.GREEN);
        } else {
            mVariantD.setTextColor(Color.RED);
        }


    }


}
