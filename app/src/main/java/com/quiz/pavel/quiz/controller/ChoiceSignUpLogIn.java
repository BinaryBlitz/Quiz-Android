package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;
import com.vk.sdk.util.VKUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pavelkozemirov on 01.02.15.
 */
public class ChoiceSignUpLogIn extends FragmentActivity {

    private static final String TAG = "ChoiceSignUpLogIn";

    @InjectView(R.id.signup_choice) Button mSignUp;
    @InjectView(R.id.vk_sighin) Button mSignUpVk;
    @InjectView(R.id.login_choice) Button mLogIn;

    private static final String[] sMyScope = new String[] {
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.OFFLINE
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_sign_log);
        ButterKnife.inject(this);

        VKSdk.initialize(sdkListener, "4795421");

        if (Mine.getInstance(this).isSignIn(this)) {
            startApp();
            return;
        }

//        if (VKSdk.wakeUpSession()) {
//           startApp();
//           return;
//        }

        String[] fingerprint = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Log.d("Fingerprint", fingerprint[0]);
    }

    private void startApp() {
        Intent intent = new Intent(ChoiceSignUpLogIn.this, MainTabsActivity.class);
        startActivity(intent);
        finish();
    }

    private void startLogSign() {
        Intent intent = new Intent(ChoiceSignUpLogIn.this, ChoiceSignUpLogIn.class);
        startActivity(intent);
        finish();
    }

    private void startVKLog() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
        if (VKSdk.isLoggedIn()) {
            startApp();
        } else {

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }


    @OnClick(R.id.login_choice)
    public void onClick1() {
        Intent intent = new Intent(ChoiceSignUpLogIn.this, LogInActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.signup_choice)
    public void onClick2() {
        Intent intent = new Intent(ChoiceSignUpLogIn.this, SignUpActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.vk_sighin)
    public void onClick3() {
        VKSdk.authorize(sMyScope, true, true);
    }

    private final VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(sMyScope);
        }

        @Override
        public void onAccessDenied(final VKError authorizationError) {
            new AlertDialog.Builder(VKUIHelper.getTopActivity())
                    .setMessage(authorizationError.toString())
                    .show();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {

            Log.d(TAG, "token: " + newToken.accessToken + "----" + newToken.toString());
            sendRequest(newToken);

        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            startApp();
        }
    };

    private void sendRequest(VKAccessToken token) {

        RequestQueue queue = Volley.newRequestQueue(this);


        JSONObject params = new JSONObject();
        try {
            params.put("token", token.accessToken);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Mine.URL +
                "/players/authenticate_vk?token=" + token.accessToken, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Mine.newInstance(getBaseContext(), response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        startApp();
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
}
