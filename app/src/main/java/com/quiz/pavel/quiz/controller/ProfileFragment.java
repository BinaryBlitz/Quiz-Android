package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.PlayerProfile;
import com.quiz.pavel.quiz.model.Topic;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private ArrayList<Topic> mTopics;

    ArrayList<PlayerProfile> myFriendList;

    @InjectView(R.id.my_photo_imageView) ImageView mPhoto;
    @InjectView(R.id.name) TextView mTextViewName;
    @InjectView(R.id.number_of_friends) TextView mNumberOfFriends;
    @InjectView(R.id.milti_button) Button mMultiButton;
    @InjectView(R.id.challenge) Button mChallengeButton;
    @InjectView(R.id.main_scroll_view) ScrollView mScrollView;
    @InjectView(R.id.profile_favorite_topics_listview) ListView mFavoriteTopics;

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

//        downloadData();

        mNumberOfFriends.setVisibility(View.INVISIBLE);
        mMultiButton.setVisibility(View.INVISIBLE);

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


        return v;
    }

    private void downloadData() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        final JsonObjectRequest arRequest = new JsonObjectRequest(Mine.URL + "/players/"
                + mPlayerProfile.getId() + "?token=" + Mine.getInstance(getActivity()).getToken(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            Log.d(TAG, "response = " + response);
                            int id  = 0;
                            String name = "";
                            String url = "";
                            try {
                                id = response.getInt("id");
                                name = response.getString("name");
                                url = response.getString("avatar_url");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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
                        Log.d(TAG, "Error Response");
                    }
                });
        queue.add(arRequest);
    }

    private void setFavoriteTopics() {
        mFavoriteTopics.setFocusable(false);

        TopicAdapter adapter = new TopicAdapter(mTopics);
        mFavoriteTopics.setAdapter(adapter);

        setRetainInstance(true);
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

            return convertView;
        }
    }

}