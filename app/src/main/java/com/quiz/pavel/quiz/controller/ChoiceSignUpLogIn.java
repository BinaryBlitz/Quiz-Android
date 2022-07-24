package com.quiz.pavel.quiz.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
            VKScope.PHOTOS,
            VKScope.OFFLINE
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_sign_log);
        ButterKnife.inject(this);

        VKSdk.initialize(sdkListener, "4795421");

        String[] fingerprint = VKUtil.getCertificateFingerprint(this, this.getPackageName());

        context = getApplicationContext();

        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

        if (Mine.getInstance(this).isSignIn(this)) {
            startApp();
            return;
        }
    }

    private void startApp() {
        Intent intent = new Intent(ChoiceSignUpLogIn.this, MainSlidingActivity.class);
        startActivity(intent);
        checkPlayServices();
        finish();
    }

    private void firstStartApp() {
        Log.d(TAG, "invoke firstStartUp()");
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            Log.d(TAG, "id = " + regid);

//            if (regid.isEmpty()) {
                registerInBackground();
//            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
        Intent intent = new Intent(ChoiceSignUpLogIn.this, MainSlidingActivity.class);
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
//        YoYo.with(Techniques.Swing).duration(500).playOn(mLogIn);
        Intent intent = new Intent(ChoiceSignUpLogIn.this, LogInActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.signup_choice)
    public void onClick2() {
//        YoYo.with(Techniques.Swing).duration(500).playOn(mSignUp);
        Intent intent = new Intent(ChoiceSignUpLogIn.this, SignUpActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.vk_sighin)
    public void onClick3() {
//        YoYo.with(Techniques.Swing).duration(500).playOn(mSignUpVk);
//        VKSdk.authorize(sMyScope, true, true);
        VKSdk.authorize(sMyScope);
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
            if (VKSdk.isLoggedIn()) {
                firstStartApp();
            } else {

            }
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            firstStartApp();
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
                "/players/authenticate_vk?token=" + token.accessToken,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Mine.newInstance(getBaseContext(), response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "703385555679";

    /**
     * Tag used on log messages.
     */

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;

    String regid;

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty() || registrationId == "") {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(MainSlidingActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String >() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the registration ID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d(TAG, "id = " + msg);

            }
        }.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject par = new JSONObject();
        String token = Mine.getInstance(this).getToken();

        Log.d(TAG, "It has occured!! regid = " + regid);
        try {
            par = new JSONObject();
            par.put("push_token", regid);
            par.put("android", "true");

        } catch (JSONException e) {
            Log.d(TAG, "Problem with parsing json(Intent)");
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Mine.URL + "/push_tokens?token="
                + token, par, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Push_token_id has been send");
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error Response, have no data from server(Push_token_id has been send)");
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

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
}
