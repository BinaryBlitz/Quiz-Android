package com.quiz.pavel.quiz.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.quiz.pavel.quiz.model.PlayerRating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pavelkozemirov on 12.12.14.
 */
public class RatingFragment extends ListFragment {

    private static final String TAG = "RatingFragment";

    private String mName;

    //0 - nothing, 1 - topic, 2 - category
    private int mId;

    private int spec;

    private ArrayList<PlayerRating> mPlayers;

    public static RatingFragment newInstance(String name) {
        Bundle args = new Bundle();
//        args.putSerializable(EXTRA_CRIME_ID, crimeId);
        RatingFragment fragment = new RatingFragment(name);
        fragment.setArguments(args);
        return fragment;
    }

    public RatingFragment(String name) {
        mName = name;
        spec = 0;
    }

    public RatingFragment(String name, int sp, int id) {
        mName = name;
        spec = sp;
        mId = id;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Log.d(TAG, "url= " + getUrl());



        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, getUrl(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        mPlayers = new ArrayList<PlayerRating>();

                        try {
                            parsingPlayers(response, 10);
                        } catch (JSONException e) {
                            Log.d(TAG, "Error with parsing json response");
                        }

                        TopicAdapter adapter = new TopicAdapter(mPlayers);
                        setListAdapter(adapter);

                        setRetainInstance(true);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error Response, have no data from server");
            }
        });
        queue.add(stringRequest);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        ListView listView = (ListView) v.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.INVISIBLE);
        listView.setClickable(false);
        listView.setFocusable(false);

        return v;
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
            PlayerRating c = (PlayerRating) getListAdapter().getItem(position);

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    { //here u set u rute
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.login, menu);
    }



}
