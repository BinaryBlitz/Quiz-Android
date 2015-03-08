package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.games.achievement.Achievement;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Category;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.PlayerProfile;
import com.quiz.pavel.quiz.model.PlayerRating;
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
public class ProfileFragment extends Fragment {
    public static final String TAG = "ProfileFragment";

    public interface OnAddNewFragmentCallback {
        public void addFragment(PlayerProfile p);
        public void removeFragment();
        public void setTitle(String str);
    }

    OnAddNewFragmentCallback mCallback;

    LayoutInflater mInflater;
    LinearLayout mGallery;


    @InjectView(R.id.my_photo_imageView) ImageView mPhoto;
    @InjectView(R.id.name) TextView mTextViewName;
    @InjectView(R.id.number_of_friends) TextView mNumberOfFriends;

    public PlayerProfile mPlayerProfile;

    public ProfileFragment(PlayerProfile playerProfile) {
        mPlayerProfile = playerProfile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, parent, false);
        ButterKnife.inject(this, v);
        mNumberOfFriends.setVisibility(View.INVISIBLE);


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

        Picasso.with(getActivity())
                .load(R.drawable.strawberry)
                .fit()
                .into(mPhoto);
        return v;
    }

    private void showListAndPictures() {
        ArrayList<View> arViews = new ArrayList<View>();

        mNumberOfFriends.setText(String.valueOf(mPlayerProfile.list.size()));
        mNumberOfFriends.setVisibility(View.VISIBLE);

        for (int i = 0; i < mPlayerProfile.list.size(); i++) {
            View view = mInflater.inflate(R.layout.simple_horizontal_list_item, mGallery, false);

            arViews.add(view);

            TextView txt = (TextView) view.findViewById(R.id.item_name);
            txt.setText(mPlayerProfile.list.get(i).getName());

            mGallery.addView(view);

            final int id = i;
            ImageView imageButton = (ImageView) view.findViewById(R.id.imageButton);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.addFragment(mPlayerProfile.list.get(id));
                }
            });
        }

//        new AsyncTask<Void, Void, Void>() {
//                            @Override
//                            protected Void doInBackground(Void... params) {
//
//                                return null;
//                            }
//                        }.execute(null, null, null);

        for (int i = 0; i < arViews.size(); i++) {
            ImageView imageButton = (ImageView) arViews.get(i).findViewById(R.id.imageButton);
            Picasso.with(getActivity())
                    .load(R.drawable.strawberry)
                    .fit()
                    .into(imageButton);
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
                            try {
                                id = response.getJSONObject(i).getInt("id");
                                name = response.getJSONObject(i).getString("name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mPlayerProfile.list.add(new PlayerProfile(getActivity(), id, name));
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
        getActivity().finish();
    }

    @OnClick(R.id.settings_profile)
    public void onClickSettings() {
        Intent i = new Intent(getActivity(), SettingsActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.open_list)
    public void onClickOpen() {
        Intent i = new Intent(getActivity(), ListFriendRequestsActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.add_friend)
    public void onClickAdd() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Mine.URL +
                "/friendships?friend_id=" + mPlayerProfile.getId() + "&token=" +
                Mine.getInstance(getActivity()).getToken(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), "added", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnAddNewFragmentCallback) activity;
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
                mCallback.removeFragment();
        }
        return true;
    }

}