package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
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

    public TopicListFragment.TopicListListener mCallback;

    @InjectView(R.id.main_favorite_topic_list) ExpandableListView mFavoriteTopicsView;
    @InjectView(R.id.main_new_topic_list) ExpandableListView mNewTopicsView;
    @InjectView(R.id.main_popular_topic_list) ExpandableListView mPopularTopicsView;

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
        mTitle = "Один на один";
        super.onAttach(activity);

        ((MainSlidingActivity) activity).onSectionAttached(2);

        try {
            mCallback = (TopicListFragment.TopicListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
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

        mFavoriteTopicsView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mFavoriteTopicsView.setGroupIndicator(null);
        ExpandableTopicAdapter adapter1 = new ExpandableTopicAdapter(mFavoriteTopics);
        mFavoriteTopicsView.setAdapter(adapter1);

//        TopicAdapter adapter1 = new TopicAdapter(mFavoriteTopics);
//        mFavoriteTopicsView.setAdapter(adapter1);

        mNewTopicsView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mNewTopicsView.setGroupIndicator(null);
        ExpandableTopicAdapter adapter2 = new ExpandableTopicAdapter(mNewTopics);
        mNewTopicsView.setAdapter(adapter2);

//        TopicAdapter adapter2 = new TopicAdapter(mNewTopics);
//        mNewTopicsView.setAdapter(adapter2);

        mPopularTopicsView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mPopularTopicsView.setGroupIndicator(null);
        ExpandableTopicAdapter adapter3 = new ExpandableTopicAdapter(mPopularTopics);
        mPopularTopicsView.setAdapter(adapter3);

        setRetainInstance(true);


//        TopicAdapter adapter3 = new TopicAdapter(mPopularTopics);
//        mPopularTopicsView.setAdapter(adapter3);
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


    public class ExpandableTopicAdapter extends BaseExpandableListAdapter {
        ArrayList<Topic> mTopics;

        public ExpandableTopicAdapter(ArrayList<Topic> topics) {
            mTopics = topics;
        }

        @Override
        public int getGroupCount() {
            return mTopics.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mTopics.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return 1;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_topic, null);
            }
            Topic c = (Topic) mTopics.get(groupPosition);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.list_item_titleTextView);
            titleTextView.setText(c.getTitle());

            ArcProgress progress = (ArcProgress) convertView.findViewById(R.id.arc_progress);
            progress.setArcAngle(360);

            TextView level = (TextView) convertView.findViewById(R.id.item_level);
            level.setText(String.valueOf(c.getLevel()));
            progress.setProgress(c.getProgress());

            return convertView;

        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_child_item_topic, null);
            }
            final int position = groupPosition;

            Button playBtn = (Button) convertView.findViewById(R.id.child_button_play);
            playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Topic cr = mTopics.get(position);

                    int mNumberOfCategory = 1;

                    Intent i = new Intent(getActivity(), PreGameActivity.class);
                    i.putExtra("topic", mTopics.get(position).getId());
                    i.putExtra("name", mTopics.get(position).getTitle());
                    i.putExtra("category", mNumberOfCategory);

                    startActivity(i);
                }
            });

            Button challengeBtn = (Button) convertView.findViewById(R.id.child_button_challenge);
            challengeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "challenge", Toast.LENGTH_SHORT).show();
                }
            });

            Button ratingBtn = (Button) convertView.findViewById(R.id.child_button_rating);
            ratingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onOpenRating();
                }
            });


            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }



}
