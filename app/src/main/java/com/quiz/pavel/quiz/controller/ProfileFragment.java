package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.PlayerProfile;
import com.quiz.pavel.quiz.model.Topic;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pavelkozemirov on 12.12.14.
 */
public class ProfileFragment extends MyFragment {
    public static final String TAG = "ProfileFragment";

    public interface ProfileFragmentListener {
        public void openSearchFragment();
        public void openFriendsListFragment(PlayerProfile p);
        public void openCategoryListChallenge();
        public void setIdForChallenge(int id);
    }

    ProfileFragmentListener mCallback;

    DisplayImageOptions options;


    LayoutInflater mInflater;
    LinearLayout mGallery;

    private ArrayList<Topic> mTopics = new ArrayList<Topic>();

    ArrayList<PlayerProfile> myFriendList;

    @InjectView(R.id.my_photo_imageView) ImageView mPhoto;
    @InjectView(R.id.name) TextView mTextViewName;
    @InjectView(R.id.number_of_friends) TextView mNumberOfFriends;
    @InjectView(R.id.personal_tab) TextView mPersonalTab;
    @InjectView(R.id.personal_tab_textView) TextView mPersonalTabText;
    @InjectView(R.id.milti_button) Button mMultiButton;
    @InjectView(R.id.challenge) Button mChallengeButton;
    @InjectView(R.id.main_scroll_view) ScrollView mScrollView;
    @InjectView(R.id.profile_favorite_topics_listview) ListView mFavoriteTopics;

    @InjectView(R.id.profile_wins) TextView mWins;
    @InjectView(R.id.profile_dead_heats) TextView mDeadHeats;
    @InjectView(R.id.profile_losses) TextView mLosses;


    public PlayerProfile mPlayerProfile;

    public ProfileFragment(PlayerProfile playerProfile) {
        mPlayerProfile = playerProfile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, parent, false);
        ButterKnife.inject(this, v);

        mNumberOfFriends.setVisibility(View.INVISIBLE);
        mMultiButton.setVisibility(View.INVISIBLE);

        mPersonalTab.setVisibility(View.GONE);
        mPersonalTabText.setVisibility(View.GONE);

        mNumberOfFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.openFriendsListFragment(mPlayerProfile);
            }
        });


        mTextViewName.setText(mPlayerProfile.getName() + " id:" + mPlayerProfile.getId());

        mGallery = (LinearLayout)v.findViewById(R.id.id_gallery);
        mGallery.removeAllViews();
        mGallery.removeAllViewsInLayout();

        mInflater = LayoutInflater.from(getActivity());

        if(mPlayerProfile.list == null) {
            downloadListOfFriends();
        } else {
            showListAndPictures();
        }

        if (mPlayerProfile.mAvatarUrl == null || mPlayerProfile.mAvatarUrl == "null") {
            Picasso.with(getActivity())
                .load(R.drawable.catty)
                .fit()
                .into(mPhoto);
        } else {
            ImageLoader.getInstance().displayImage(Mine.URL_photo + mPlayerProfile.mAvatarUrl, mPhoto, options);
        }

        downloadMyFriends();
        downloadData();

        return v;
    }

    private void downloadData() {
//        if(!mTopics.isEmpty()) {
//            setFavoriteTopics();
//            return;
//        }
        new AsyncTask<String, String, JSONObject>() {
            @Override
            protected JSONObject doInBackground(String... uri) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response;
                JSONObject responseString = null;
                try {
                    response = httpclient.execute(new HttpGet(Mine.URL + "/players/"
                + mPlayerProfile.getId() + "?token=" + Mine.getInstance(getActivity()).getToken()));
                    StatusLine statusLine = response.getStatusLine();
                    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        response.getEntity().writeTo(out);
                        responseString = new JSONObject(out.toString());
                        out.close();
                    } else{
                        //Closes the connection.
                        response.getEntity().getContent().close();
                        throw new IOException(statusLine.getReasonPhrase());
                    }
                } catch (ClientProtocolException e) {
                    Log.d(TAG, "Data problem");
                } catch (IOException e) {
                    Log.d(TAG, "Data problem");
                } catch (JSONException e) {
                    Log.d(TAG, "Data problem, parsing to jsonobject");
                }
                return responseString;
            }

            @Override
            protected void onPostExecute(JSONObject response) {
                super.onPostExecute(response);
                Log.d(TAG, "res = " + response);
                try {
                    JSONArray ar = response.getJSONArray("favorite_topics");
                    for (int j = 0; j < ar.length(); j++) {
                        mTopics.add(new Topic(ar.getJSONObject(j)));
                    }
                    setFavoriteTopics();
                    JSONObject totaljson =  response.getJSONObject("total_score");
                    setTotalScore(totaljson);
                    JSONObject srjson = response.getJSONObject("score");
                    setPersonalScore(srjson);
                } catch (JSONException e) {

                }
            }
        }.execute(null,null,null);
    }

    private void setTotalScore(JSONObject jsonObject) throws JSONException {
        final int wins = jsonObject.getInt("wins");
        final int draws = jsonObject.getInt("draws");
        final int losses = jsonObject.getInt("losses");
        mWins.setText(String.valueOf(wins));
        mDeadHeats.setText(String.valueOf(draws));
        mLosses.setText(String.valueOf(losses));

    }

    private void setPersonalScore(JSONObject jsonObject) throws JSONException {
        int mine = jsonObject.getInt("wins");
        int him = jsonObject.getInt("losses");
        mPersonalTab.setText(mine + "-" + him);
        mPersonalTab.setVisibility(View.VISIBLE);
        mPersonalTabText.setVisibility(View.VISIBLE);
    }

    private void setFavoriteTopics() {
        mFavoriteTopics.setFocusable(false);
        if(mTopics == null || getActivity() == null) {
            return;
        }
        TopicAdapter adapter = new TopicAdapter(mTopics);
        mFavoriteTopics.setAdapter(adapter);
    }

    private void setNameOfMultiButton() {
        if(mPlayerProfile.isMe()) {
            mMultiButton.setText("Настройки");
            mChallengeButton.setVisibility(View.INVISIBLE);
        } else if(mPlayerProfile.isInMyFriends(myFriendList)) {
            mMultiButton.setText("Убрать из друзей");
            mChallengeButton.setVisibility(View.VISIBLE);
        } else {
            mMultiButton.setText("Добавить в друзья");
            mChallengeButton.setVisibility(View.VISIBLE);
        }
        mMultiButton.setVisibility(View.VISIBLE);
    }

    private void showListAndPictures() {
        ArrayList<View> arViews = new ArrayList<View>();

        mNumberOfFriends.setText(String.valueOf(mPlayerProfile.list.size()));
        mNumberOfFriends.setVisibility(View.VISIBLE);

        for (int i = 0; i < mPlayerProfile.list.size(); i++) {
            View view = mInflater.inflate(R.layout.simple_horizontal_list_item, mGallery, false);

            arViews.add(view);

            TextView txt = (TextView) view.findViewById(R.id.item_name);
            txt.setText(mPlayerProfile.list.get(i).getShortName());

            mGallery.addView(view);

            final int id = i;
            ImageView imageButton = (ImageView) view.findViewById(R.id.imageButton);

            Log.d(TAG, "url link = " +  mPlayerProfile.list.get(i).mAvatarUrl );

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMyFragmentListenerCallback.openProfileFragment(mPlayerProfile.list.get(id));
                }
            });
        }

        for (int i = 0; i < arViews.size(); i++) {
            ImageView imageButton = (ImageView) arViews.get(i).findViewById(R.id.imageButton);

            if (mPlayerProfile.list.get(i).mAvatarUrl == null || mPlayerProfile.list.get(i).mAvatarUrl == "null") {
                Picasso.with(getActivity())
                        .load(R.drawable.catty)
                        .fit()
                        .into(imageButton);
            } else {
                Picasso.with(getActivity())
                        .load(Mine.URL_photo + mPlayerProfile.list.get(i).mAvatarUrl)
                        .fit()
                        .into(imageButton);
            }
        }
    }

    private void downloadListOfFriends() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest arRequest = new JsonArrayRequest(Mine.URL + "/players/"
                + mPlayerProfile.getId() + "/friends?token="
                + Mine.getInstance(getActivity()).getToken(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mPlayerProfile.list = new ArrayList<PlayerProfile>();

                        for (int i = 0; i < response.length(); i++) {

                            int id  = 0;
                            String name = "";
                            String url = "";
                            try {
                                id = response.getJSONObject(i).getInt("id");
                                name = response.getJSONObject(i).getString("name");
                                url = response.getJSONObject(i).getString("avatar_url");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mPlayerProfile.list.add(new PlayerProfile(getActivity(), id, name, url));
                        }
                        showListAndPictures();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error Response");
                    }
                });
        queue.add(arRequest);
    }

    @OnClick(R.id.logout)
    public void onClick() {
        Mine.getInstance(getActivity()).logOut(getActivity());
        Intent intent = new Intent(getActivity(), ChoiceSignUpLogIn.class);
        startActivity(intent);
        setEmptyForPushNotificationId();
        getActivity().finish();
    }

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    private void setEmptyForPushNotificationId() {
        final SharedPreferences prefs = getGCMPreferences(getActivity());
        int appVersion = getAppVersion(getActivity());
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, "");
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getActivity().getSharedPreferences(MainSlidingActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @OnClick(R.id.challenge)
    public void onClickChallenge() {
        mCallback.setIdForChallenge(mPlayerProfile.getId());
        mCallback.openCategoryListChallenge();
    }


    @OnClick(R.id.open_list)
    public void onClickOpen() {
        Intent i = new Intent(getActivity(), ListFriendRequestsActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.milti_button)
    public void onClickAdd() {
        if(mPlayerProfile.isMe()) {
            Intent i = new Intent(getActivity(), SettingsActivity.class);
            startActivity(i);
        } else if(mPlayerProfile.isInMyFriends(myFriendList)) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.DELETE, Mine.URL +
                    "/friendships/unfriend?friend_id=" + mPlayerProfile.getId() + "&token=" +
                    Mine.getInstance(getActivity()).getToken(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "deleted");
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error with response");
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
        } else {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            Log.d(TAG, "url = " + Mine.URL +
                    "/friendships?friend_id=" + mPlayerProfile.getId() + "&token=" +
                    Mine.getInstance(getActivity()).getToken());

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Mine.URL +
                    "/friendships?friend_id=" + mPlayerProfile.getId() + "&token=" +
                    Mine.getInstance(getActivity()).getToken(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getActivity(), "added", Toast.LENGTH_SHORT).show();
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error with response");
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

    private void downloadMyFriends() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest arRequest = new JsonArrayRequest(Mine.URL + "/players/"
                + Mine.getInstance(getActivity()).getId() + "/friends?token="
                + Mine.getInstance(getActivity()).getToken(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        myFriendList = new ArrayList<PlayerProfile>();
                        for (int i = 0; i < response.length(); i++) {
                            int id  = 0;
                            String name = "";
                            String url = "";
                            try {
                                id = response.getJSONObject(i).getInt("id");
                                name = response.getJSONObject(i).getString("name");
                                url = response.getJSONObject(i).getString("avatar_url");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            myFriendList.add(new PlayerProfile(getActivity(), id, name, url));
                        }
                        setNameOfMultiButton();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error Response");
                    }
                });
        queue.add(arRequest);
    }

    @Override
    public void onAttach(Activity activity) {
        mTitle = mPlayerProfile.getName();
        super.onAttach(activity);

        try {
            mCallback = (ProfileFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflator){
        super.onCreateOptionsMenu(menu, inflator);
        inflator.inflate(R.menu.menu_profile_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mMyFragmentListenerCallback.back();
                break;
            case R.id.action_search:
                mCallback.openSearchFragment();
                break;
        }
        return true;
    }

    private class TopicAdapter extends ArrayAdapter<Topic> {
        public TopicAdapter(ArrayList<Topic> topics) {
            super(getActivity(), 0, topics);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_topic, null);
            }
            Topic c = (Topic) mTopics.get(position);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.list_item_titleTextView);
            titleTextView.setText(c.getTitle());

            ArcProgress progress = (ArcProgress) convertView.findViewById(R.id.arc_progress);
            progress.setArcAngle(360);

            TextView level = (TextView) convertView.findViewById(R.id.item_level);
            level.setText(String.valueOf(c.getLevel()));
            progress.setProgress(c.getProgress());

            return convertView;
        }
    }

    private void downloadData1() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest arRequest = new JsonObjectRequest(Request.Method.GET, Mine.URL + "/players/"
                + mPlayerProfile.getId() + "?token=" + Mine.getInstance(getActivity()).getToken(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "res = " + response);
                        try {
                            JSONArray ar = response.getJSONArray("favorite_topics");
                            for (int j = 0; j < 3; j++) {
                                mTopics.add(new Topic(ar.getJSONObject(j)));
                            }
                            setFavoriteTopics();

                        } catch (JSONException e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error Response: DATA");
                    }
                });
        queue.add(arRequest);
    }
}