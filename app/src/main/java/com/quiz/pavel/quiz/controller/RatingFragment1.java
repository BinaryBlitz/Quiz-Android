package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Category;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.PlayerRating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pavel on 19/03/15.
 */
public class RatingFragment1 extends MyFragment {

    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_main);

        mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator("Simple"),
                TestFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("contacts").setIndicator("Contacts"),
                TestFragment.class, null);


        return mTabHost;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }

    public static class TestFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String TAG = "TestFragment";

        private ArrayList<PlayerRating> mPlayers;


        ListView listView;

        private String mName = "general";
        private int spec = 0;
        private int mId = 0;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPlayers = new ArrayList<PlayerRating>();
        }


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static TestFragment newInstance(int sectionNumber) {
            TestFragment fragment = new TestFragment();
            return fragment;
        }

        public TestFragment() {
        }

        private String getUrl() {
            switch (spec){
                case 0: return Mine.URL + "/rankings/" + mName + "?token="
                        + Mine.getInstance(getActivity()).getToken();
                case 1: return Mine.URL + "/rankings/" + mName + "?token="
                        + Mine.getInstance(getActivity()).getToken() + "&topic_id=" + mId;
                case 2: return Mine.URL + "/rankings/" + mName + "?token="
                        + Mine.getInstance(getActivity()).getToken() + "&category_id=" + mId;
            }
            return null;
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


//        mTabHost = new FragmentTabHost(getActivity());
//        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.containerRating);
//
//        mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator("Simple"),
//                CategoryListFragment.class, null);

            listView = (ListView) v.findViewById(R.id.listView_rating);
            listView.setClickable(false);
            listView.setFocusable(false);

            RequestQueue queue = Volley.newRequestQueue(getActivity());

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


                            TopicAdapter adapter = new TopicAdapter(mPlayers);
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



            return v;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }

        private class TopicAdapter extends ArrayAdapter<PlayerRating> {
            public TopicAdapter(ArrayList<PlayerRating> topics) {
                super(getActivity(), 0, topics);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item_ranking, null);
                }
                PlayerRating c = (PlayerRating) mPlayers.get(position);



                TextView titleTextView = (TextView) convertView.findViewById(R.id.list_item_titleTextView);
                if(c.getId() == Mine.getInstance(getActivity()).getId()) {
                    convertView.setBackgroundColor(Color.LTGRAY);
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

                return convertView;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        mTitle = "Рейтинг";

        super.onAttach(activity);

        ((MainSlidingActivity) activity).onSectionAttached(2);
    }
}
