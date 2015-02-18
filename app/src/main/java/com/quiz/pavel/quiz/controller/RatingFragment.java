package com.quiz.pavel.quiz.controller;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Category;
import com.quiz.pavel.quiz.model.IntentJSONSerializer;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.PlayerRating;
import com.quiz.pavel.quiz.model.Topic;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pavelkozemirov on 12.12.14.
 */
public class RatingFragment extends ListFragment {

    private static final String TAG = "RatingFragment";

    private static final String URL = "https://protected-atoll-5061.herokuapp.com";


    private ArrayList<PlayerRating> mPlayers;

    public static RatingFragment newInstance() {
        Bundle args = new Bundle();
//        args.putSerializable(EXTRA_CRIME_ID, crimeId);
        RatingFragment fragment = new RatingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Log.d(TAG, "token= " + Mine.getInstance(getActivity()).getToken());


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, URL + "/rankings/general?token="
                + Mine.getInstance(getActivity()).getToken(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        mPlayers = new ArrayList<PlayerRating>();

                        try {
                            JSONArray array = response.getJSONArray("top_rankings");
                            for (int i = 0; i < array.length(); i++) {
                                mPlayers.add(new PlayerRating(array.getJSONObject(i)));
                            }


                        } catch (JSONException e) {
                            Log.d(TAG, "Error with parsing json response");
                        }
                        mPlayers.add(new PlayerRating());

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
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


//        mItem = (ClipData.Item).findViewById(R.id.menu_item_show_subtitle);

        // getActivity().setTitle(R.string.topics_title);


//        mTopics = CrimeLab.get(getActivity()).getCrimes();


//        ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(), android.R.layout.simple_list_item_1, mCrimes);

//        mIconNumber = android.R.drawable.ic_menu_help;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        ListView listView = (ListView) v.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


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
                        .inflate(R.layout.list_item_topic, null);
            }
            PlayerRating c = (PlayerRating) getListAdapter().getItem(position);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.list_item_titleTextView);
            titleTextView.setText(c.getTitle());

//
//            CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
//            solvedCheckBox.setChecked(false);


            return convertView;
        }
    }
}
