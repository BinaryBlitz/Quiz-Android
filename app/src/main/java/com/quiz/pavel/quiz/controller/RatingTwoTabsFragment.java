package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.PlayerProfile;
import com.quiz.pavel.quiz.model.PlayerRating;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pavel on 16/05/15.
 */
public class RatingTwoTabsFragment extends Fragment {
    private static final String TAG = "RatingTwoTabsFragment";
    private ArrayList<PlayerRating> mPlayers;

    ListView listView;

    private int mTopicId;

    private String mName;
    private int spec = 0;
    private int mId = 0;
    private boolean mIsCategory;

    public RatingTwoTabsFragment(String name, int id, boolean cat) {
        mName = name;
        mTopicId = id;
        mIsCategory = cat;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayers = new ArrayList<PlayerRating>();
    }


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */


    private String getUrl() {
        String str;
        if (mIsCategory) {
            if (mName.equals("weekly")) {
                str = "/rankings/category?weekly=true&category_id=";
            } else {
                str = "/rankings/category?weekly=false&category_id=";
            }
        } else {
            if (mName.equals("weekly")) {
                str = "/rankings/topic?weekly=true&topic_id=";
            } else {
                str = "/rankings/topic?weekly=false&topic_id=";
            }
        }

        return Mine.URL + str + mTopicId + "&token="
                + Mine.getInstance(getActivity()).getToken();
    }


    private void parsingPlayers(JSONObject response, int numberOfTopRating)
            throws JSONException {
        JSONArray array = response.getJSONArray("rankings");
        for (int i = 0; i < numberOfTopRating; i++) {
            mPlayers.add(new PlayerRating( array.getJSONObject(i), i + 1));
        }

        int m = 0;
        try {
            m = response.getInt("position");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray arrayNoTop = response.getJSONArray("player_rankings");

        for (int i = 0; i < arrayNoTop.length(); i++) {
            if (arrayNoTop.getJSONObject(i).getInt("mId") == Mine.getInstance(getActivity()).getId()) {
                mPlayers.add(new PlayerRating("...", -1, -1, -1));
                mPlayers.add(new PlayerRating(Mine.getInstance(getActivity()).getName(), m,
                        arrayNoTop.getJSONObject(i).getInt("points"), Mine.getInstance(getActivity()).getId()));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_rating_list, container, false);

        listView = (ListView) v.findViewById(R.id.listView_rating);
        listView.setClickable(false);
        listView.setFocusable(false);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Log.d(TAG, "url = " + getUrl());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, getUrl(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, "point1");

                        mPlayers = new ArrayList<PlayerRating>();

                        try {
                            parsingPlayers(response, 10);
                        } catch (JSONException e) {
                            Log.d(TAG, "Error with parsing json response");
                        }
                        int n = 1;

                        try {
                            n = response.getInt("position");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        TopicAdapter adapter = new TopicAdapter(mPlayers, n);
                        listView.setAdapter(adapter);

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error Response, have no data from server");
            }
        });
        queue.add(stringRequest);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlayerProfile playerProfile = new PlayerProfile(getActivity(), mPlayers.get(position).getId(),
                        mPlayers.get(position).getName(), mPlayers.get(position).mUrl);

//                    mMyFragmentListenerCallback.openProfileFragment(playerProfile);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private class TopicAdapter extends ArrayAdapter<PlayerRating> {
        int myPosition;

        public TopicAdapter(ArrayList<PlayerRating> topics, int pos) {
            super(getActivity(), 0, topics);
            myPosition = pos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_ranking, null);
            }
            PlayerRating c = (PlayerRating) mPlayers.get(position);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.list_item_titleTextView);
            if(c.getPosition() == myPosition + 1) {
//                    convertView.setBackgroundColor(Color.LTGRAY);
            }
            titleTextView.setText(c.getName());

            TextView numberTextView = (TextView) convertView.findViewById(R.id.number_textView);
            if(c.getPosition() > 0) {
                numberTextView.setText(String.valueOf(c.getPosition()));
            } else {
                numberTextView.setText("");
            }

            TextView pointsTextView = (TextView) convertView.findViewById(R.id.points_textView);
            if(c.getPoints() >= 0){
                pointsTextView.setText(String.valueOf(c.getPoints()));
            } else {
                pointsTextView.setText("");
            }

            ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView_photo);

            if (c.mUrl == null || c.mUrl.equals("null")){
                Picasso.with(getActivity())
                        .load(R.drawable.catty)
                        .into(imageView);
            }

            Picasso.with(getActivity())
                    .load(Mine.URL_photo + c.mUrl)
                    .placeholder(R.drawable.catty)
                    .into(imageView);

            return convertView;
        }
    }

}
