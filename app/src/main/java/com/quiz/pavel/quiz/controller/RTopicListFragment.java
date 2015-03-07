package com.quiz.pavel.quiz.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.Topic;

import java.util.ArrayList;

/**
 * Created by pavelkozemirov on 15.02.15.
 */
public class RTopicListFragment extends ListFragment {
    private static final String TAG = "TopicListFragment";



    private ArrayList<Topic> mTopics;


    public static RTopicListFragment newInstance() {
        Bundle args = new Bundle();
        RTopicListFragment fragment = new RTopicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        if (savedInstanceState == null) {
            Log.d("TAG", " WHAT THE FUCK");
        }

//TODO: recomment, ger topics from all of categories
//        int i = Mine.getInstance(getActivity()).loadCategoryAr(getActivity()).size();
//        for (int j = 0; j < i; j++) {
//            ArrayList<Topic> topics = Mine.getInstance(getActivity()).loadCategoryAr(getActivity()).get(i).mTopics;
//            mTopics.addAll(topics);
//        }

        mTopics = Mine.getInstance(getActivity()).loadCategoryAr(getActivity()).get(0).mTopics;


        ListView listView = (ListView) v.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;

            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        TopicAdapter adapter = new TopicAdapter(mTopics);
        setListAdapter(adapter);

        setRetainInstance(true);

        return v;
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
            Topic c = (Topic) getListAdapter().getItem(position);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.list_item_titleTextView);
            titleTextView.setText(c.getTitle());


            return convertView;
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Topic topic = ((TopicAdapter) getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), RRatingActivity.class);
        i.putExtra("by_topic", true);
        i.putExtra("topic_id", topic.getId());
        i.putExtra(RRatingActivity.EXTRA_NAME_OF_TOPIC, topic.getTitle());

        startActivity(i);
    }
}
