package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.PlayerProfile;
import com.quiz.pavel.quiz.model.Topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pavel on 17/04/15.
 */
public class MainFragment extends MyFragment {
    private final static String TAG = "MainFragment";

    @InjectView(R.id.main_favorite_topic_list) ListView mFavoriteTopicsView;
    @InjectView(R.id.main_new_topic_list) ListView mNewTopicsView;
    @InjectView(R.id.main_popular_topic_list) ListView mPopularTopicsView;

    ArrayList<Topic> mFavoriteTopics = new ArrayList<Topic>();
    ArrayList<Topic> mNewTopics = new ArrayList<Topic>();
    ArrayList<Topic> mPopularTopics = new ArrayList<Topic>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_tab, parent, false);
        ButterKnife.inject(this, v);

        mFavoriteTopicsView.setFocusable(false);
        mNewTopicsView.setFocusable(false);
        mPopularTopicsView.setFocusable(false);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest arRequest = new JsonObjectRequest(Mine.URL + "/pages/home?token="
                + Mine.getInstance(getActivity()).getToken(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mFavoriteTopics = new ArrayList<Topic>();
                        mNewTopics = new ArrayList<Topic>();
                        mPopularTopics = new ArrayList<Topic>();

                        try {
                            writeInTopics(response.getJSONArray("featured_topics"), mNewTopics);
                            writeInTopics(response.getJSONArray("favorite_topics"), mFavoriteTopics);
                            writeInTopics(response.getJSONArray("friends_favorite_topics"), mPopularTopics);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setTopics();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error Response");
                    }
                });
        queue.add(arRequest);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainSlidingActivity) activity).onSectionAttached(2);
        mTitle = "Один на один";
    }

    private void writeInTopics(JSONArray ar, ArrayList<Topic> topics) throws JSONException {
        for (int i = 0; i < 3; i++) {
            topics.add(new Topic(ar.getJSONObject(i)));
        }
    }

    private void setTopics() {
        if(mPopularTopics == null || getActivity() == null) {
            return;
        }
        TopicAdapter adapter1 = new TopicAdapter(mFavoriteTopics);
        mFavoriteTopicsView.setAdapter(adapter1);

        TopicAdapter adapter2 = new TopicAdapter(mNewTopics);
        mNewTopicsView.setAdapter(adapter2);

        TopicAdapter adapter3 = new TopicAdapter(mPopularTopics);
        mPopularTopicsView.setAdapter(adapter3);
    }

    private class TopicAdapter extends ArrayAdapter<Topic> {
        ArrayList<Topic> mTopics;
        public TopicAdapter(ArrayList<Topic> topics) {
            super(getActivity(), 0, topics);
            mTopics = topics;
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
