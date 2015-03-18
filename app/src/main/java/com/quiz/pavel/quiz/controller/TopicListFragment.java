package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
public class TopicListFragment extends ListFragment {
    private static final String TAG = "TopicListFragment";



    private ArrayList<Topic> mTopics;
    private int mNumberOfCategory = 0;

    public interface OnEventTopicListListener {
        public void back();
    }

    OnEventTopicListListener mCallback;


//    public static TopicListFragment newInstance() {
//        Bundle args = new Bundle();
//        TopicListFragment fragment = new TopicListFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

    public TopicListFragment(int numberOfCategory) {
        mNumberOfCategory = numberOfCategory;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainSlidingActivity) activity).onSectionAttached(Mine.getInstance(getActivity())
                .loadCategoryAr(getActivity()).get(mNumberOfCategory).getTitle());

//        try {
//            mCallback = (OnEventTopicListListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnHeadlineSelectedListener");
//        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        if (savedInstanceState == null) {
            Log.d("TAG", " WHAT THE FUCK");
        }
        Log.d(TAG, "numberOf category = " + mNumberOfCategory);

        mTopics = Mine.getInstance(getActivity()).loadCategoryAr(getActivity())
                .get(mNumberOfCategory).mTopics;


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

        Topic cr = ((TopicAdapter) getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), PreGameActivity.class);
        i.putExtra("topic", mTopics.get(position).getId());
        i.putExtra("name", mTopics.get(position).getTitle());

        startActivity(i);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflator){
        super.onCreateOptionsMenu(menu, inflator);
//        inflator.inflate(R.menu.menu_profile_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mCallback.back();
        }
        return true;
    }
}
