package com.quiz.pavel.quiz.controller;

import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.filippudak.ProgressPieView.ProgressPieView;
import com.nineoldandroids.animation.Animator;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.MyButton;
import com.quiz.pavel.quiz.model.Question;
import com.quiz.pavel.quiz.model.Session;
import com.quiz.pavel.quiz.model.SessionManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.quiz.pavel.quiz.R.style.ButtonVar;

/**
 * Created by pavelkozemirov on 11.12.14.
 *
 * @author Pavel Kozemirov
 * @version 1.0
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

    @InjectView(R.id.buttons_broad) TableLayout mButtonsBroad;

    @InjectView(R.id.broad_my_profile) LinearLayout mMyProfileBroad;
    @InjectView(R.id.broad_opponent_profile) LinearLayout mOpponentProfileBroad;

    @InjectView(R.id.round_shower) TextView mRoundShowerTextView;

    @InjectView(R.id.my_name) TextView mMyName;
    @InjectView(R.id.opponents_name) TextView mOpponentsName;

    @InjectView(R.id.opponents_image_imageView) ImageView mOpponentImage;
    @InjectView(R.id.my_image_imageView) ImageView mMyImage;

    @InjectView(R.id.background_game) LinearLayout mBackground;


    private ProgressPieView mProgressPieView;
    private static final int SIZE = 96;
    private static final int MARGIN = 8;


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
        setBackground();

        mSessionManager.mSession.callback = new Session.MyCallback() {

            @Override
            public void callbackCallMine(int i) {
                mMyPointsTextView.setText(String.valueOf(i));
            }

            @Override
            public void callbackCallOpponent(int i) {
                mOpponentsPointsTextView.setText(String.valueOf(i));
            }

        };

        mSessionManager.mCallbackOnView = new SessionManager.CallbackOnView() {

            @Override
            public void updateTimer(int i) {
                mTimerTextView.setText(String.valueOf(10 - i));
                mProgressPieView.setProgress(i * 10);
                mProgressPieView.setText(String.valueOf(10 - i));
            }

            @Override
            public void closeRound() {
                blockOfButtons = true;
                onCloseRound();

            }

            @Override
            public void openRound() {

            }

            @Override
            public void opponentChooseAnswer(int i) {

            }
        };

        mSessionManager.listenEvent();





        setAvatarsNames();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                beginGame();
            }
        }, 1000);

        float density = getResources().getDisplayMetrics().density;
        int size = (int) (density * SIZE);
        int margin = (int) (density * MARGIN);

        // Default version
        mProgressPieView = (ProgressPieView) v.findViewById(R.id.progressPieView);
        mProgressPieView.setOnProgressListener(new ProgressPieView.OnProgressListener() {
            @Override
            public void onProgressChanged(int progress, int max) {
                if (!mProgressPieView.isTextShowing()) {
                    mProgressPieView.setShowText(true);
                    mProgressPieView.setShowImage(false);
                }
            }

            @Override
            public void onProgressCompleted() {
                if (!mProgressPieView.isImageShowing()) {
                    mProgressPieView.setShowImage(true);
                }
                mProgressPieView.setShowText(false);
//                mProgressPieView.setImageResource(R.drawable.ic_action_accept);
            }
        });

        mTimerTextView.setVisibility(View.INVISIBLE);

        return v;
    }

    private void setBackground() {
        String url = Mine.URL_photo + Mine.getInstance(getActivity())
                .loadCategoryAr(getActivity()).get(mSessionManager.mSession.mCategoryId).mBackgroundUrl;
//        try {
//            mBackground.setBackgroundDrawable(new BitmapDrawable(getResources(), Picasso.with(getActivity()).load(url).get() ));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Log.d(TAG, "url= " + url);

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

    private void setAvatarsNames() {
        String myname = mSessionManager.mSession.getMyName();
        String opponentName = mSessionManager.mSession.getOpponentsName();

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

        if (mSessionManager.mSession.mMyAvatarUrl == "null" ||
                mSessionManager.mSession.mMyAvatarUrl == null) {
            Picasso.with(getActivity())
                    .load(R.drawable.catty)
                    .fit()
                    .into(mMyImage);
        } else {
            Picasso.with(getActivity())
                    .load(Mine.URL_photo + mSessionManager.mSession.mMyAvatarUrl)
                    .fit()
                    .into(mMyImage);
        }

        if (mSessionManager.mSession.mOpponentAvatarUrl == "null" ||
                mSessionManager.mSession.mOpponentAvatarUrl == null) {
            Picasso.with(getActivity())
                    .load(R.drawable.catty)
                    .fit()
                    .into(mOpponentImage);
        } else {
            Picasso.with(getActivity())
                    .load(Mine.URL_photo + mSessionManager.mSession.mOpponentAvatarUrl)
                    .fit()
                    .into(mOpponentImage);
        }
    }

    private void beginGame() {
        mCurQuestion = mSessionManager.mSession.mCurrentSessionQuestion.getQuestion();
        updateGUI();
    }


    private void onCloseRound() {

        mSessionManager.stopTimer();

        Log.d(TAG, "onCloseRound, mOpponentAnswer = " + mSessionManager.mSession.mCurrentSessionQuestion.mOpponentAnswer);
        switch (mSessionManager.mSession.mCurrentSessionQuestion.mOpponentAnswer) {
            case 0:

                try {
                    Drawable img = getResources().getDrawable( R.drawable.point );
                    img.setBounds( 0, 0, 60, 60 );
                    if(mVariantA.getCompoundDrawables()[0] != null) {
                        mVariantA.setCompoundDrawables(img, null, img, null);
                    } else {
                        mVariantA.setCompoundDrawables(null, null, img, null);
                    }
                } catch (Exception ex) {

                }
                break;
            case 1:
                try {
                    Drawable img = getResources().getDrawable( R.drawable.point );
                    img.setBounds(0, 0, 60, 60);
                    if(mVariantB.getCompoundDrawables()[0] != null) {
                        mVariantB.setCompoundDrawables(img, null, img, null);
                    } else {
                        mVariantB.setCompoundDrawables(null, null, img, null);
                    }
                } catch (Exception ex) {

                }
                break;
            case 2:
                try {
                    Drawable img = getResources().getDrawable( R.drawable.point );
                    img.setBounds(0, 0, 60, 60);
                    if(mVariantC.getCompoundDrawables()[0] != null) {
                        mVariantC.setCompoundDrawables(img, null, img, null);
                    } else {
                        mVariantC.setCompoundDrawables(null, null, img, null);
                    }
                } catch (Exception ex) {

                }
                break;
            case 3:
                try {
                    Drawable img = getResources().getDrawable( R.drawable.point );
                    img.setBounds(0, 0, 60, 60);
                    if(mVariantD.getCompoundDrawables()[0] != null) {
                        mVariantD.setCompoundDrawables(img, null, img, null);
                    } else {
                        mVariantD.setCompoundDrawables(null, null, img, null);
                    }
                } catch (Exception ex) {

                }
                break;
        }



        Log.d(TAG, "onCloseRound, mCorrectAnswer = " + mSessionManager.mSession.mCurrentSessionQuestion.mCorrectAnswer);
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

        Log.d(TAG, "hideIncorrectAnswerVariants, mCorrectAnswer= " + mSessionManager.mSession.mCurrentSessionQuestion.mCorrectAnswer);
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
        YoYo.with(Techniques.FadeOut)
                .delay(1000)
                .duration(500)
                .withListener(new Animator.AnimatorListener() {
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
        YoYo.with(Techniques.FadeOut)
                .delay(1000)
                .duration(500)
                .withListener(new Animator.AnimatorListener() {
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
        YoYo.with(Techniques.FadeOut)
                .delay(1000)
                .duration(500)
                .withListener(new Animator.AnimatorListener() {
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
        YoYo.with(Techniques.FadeOut)
                .delay(500)
                .duration(900)
                .withListener(new Animator.AnimatorListener() {
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
        YoYo.with(Techniques.FadeOut)
                .delay(500)
                .duration(900)
                .withListener(new Animator.AnimatorListener() {
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
            Intent intent = new Intent(getActivity(), PostGameActivity.class);
            intent.putExtra(PostGameFragment.EXTRA, mSessionManager.amIWinner());
            sendToCloseGameSession();
            startActivity(intent);
            finish = true;

            getActivity().finish();
            return;
        }
        mCurQuestion = mSessionManager.mSession.mCurrentSessionQuestion.getQuestion();

    }

    private void sendToCloseGameSession() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PATCH,
                Mine.URL + "/game_sessions/" + mSessionManager.mSession.mId + "/close?token=" +
                Mine.getInstance(getActivity()).getToken(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "PATCH on close game session HAS SEND, response: " + response.toString());
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    //Add cases
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    boolean finish = false;

    private void showRoundTable() {

        Log.d(TAG, "SHOW TABLE");
        mSessionManager.stopTimer();

        mRoundShowerTextView.setAlpha(0f);
        mRoundShowerTextView.setVisibility(View.VISIBLE);
        mQuestionTextView.setVisibility(View.GONE);

        mRoundShowerTextView.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        hideRoundTable();
                    }
                });

    }

    private void hideRoundTable() {

        mRoundShowerTextView.animate()
                .alpha(0f)
                .setStartDelay(200)
                .setDuration(500)
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
                .setDuration(200)
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


        YoYo.with(Techniques.FadeIn)
                .duration(1100)
                .withListener(new Animator.AnimatorListener() {
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

        YoYo.with(Techniques.FadeIn)
                .duration(1100)
                .withListener(new Animator.AnimatorListener() {
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
        YoYo.with(Techniques.FadeIn)
                .duration(1100)
                .withListener(new Animator.AnimatorListener() {
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

        YoYo.with(Techniques.FadeIn)
                .duration(1100)
                .withListener(new Animator.AnimatorListener() {
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
        mVariantA.setTextColor(Color.WHITE);
        mVariantB.setTextColor(Color.WHITE);
        mVariantC.setTextColor(Color.WHITE);
        mVariantD.setTextColor(Color.WHITE);

        mVariantA.setBackgroundResource(R.drawable.shape);
        mVariantB.setBackgroundResource(R.drawable.shape);
        mVariantC.setBackgroundResource(R.drawable.shape);
        mVariantD.setBackgroundResource(R.drawable.shape);

        mVariantA.setCompoundDrawables(null, null, null, null);
        mVariantB.setCompoundDrawables(null, null, null, null);
        mVariantC.setCompoundDrawables(null, null, null, null);
        mVariantD.setCompoundDrawables(null, null, null, null);



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        String str = "you are loser";
        mSessionManager.stopTimer();

        // game was aborted by back button
//        if(!finish) {
//            Intent intent = new Intent(getActivity(), PostGameActivity.class);
//            intent.putExtra(PostGameFragment.EXTRA, mSessionManager.amIWinner());
//            startActivity(intent);
//        }

        mSessionManager.deleteInstance();
        mSessionManager.mPusher.disconnect();
        mSessionManager.myHandler.removeCallbacks(mSessionManager.myRunnable);


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

        try {
            Drawable img = getResources().getDrawable( R.drawable.point );
            img.setBounds( 0, 0, 60, 60 );

            mVariantA.setCompoundDrawables(img, null, null, null);
        } catch (Exception ex) {

        }

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
        try {
            Drawable img = getResources().getDrawable( R.drawable.point );
            img.setBounds( 0, 0, 60, 60 );
            mVariantB.setCompoundDrawables(img, null, null, null);
        } catch (Exception ex) {

        }


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

        try {
            Drawable img = getResources().getDrawable( R.drawable.point );
            img.setBounds( 0, 0, 60, 60 );
            mVariantC.setCompoundDrawables(img, null, null, null);
        } catch (Exception ex) {

        }

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

        try {
            Drawable img = getResources().getDrawable( R.drawable.point );
            img.setBounds( 0, 0, 60, 60 );
            mVariantD.setCompoundDrawables(img, null, null, null);
        } catch (Exception ex) {

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
