package com.quiz.pavel.quiz.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Topic;

import java.util.ArrayList;

/**
 * Created by pavelkozemirov on 12.12.14.
 */
public class TopicListFragment extends ListFragment {



    private ArrayList<Topic> mTopics;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //getActivity().setTitle(R.string.topics_title);
        mTopics = new ArrayList<Topic>();
        mTopics.add(new Topic());
        mTopics.add(new Topic());
        mTopics.add(new Topic());



        TopicAdapter adapter = new TopicAdapter(mTopics);
        setListAdapter(adapter);

        setRetainInstance(true);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        ListView listView = (ListView)v.findViewById(android.R.id.list);
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

        return v;
    }

    private class TopicAdapter extends ArrayAdapter<Topic> {
        public TopicAdapter(ArrayList<Topic> topics){
            super(getActivity(),0,topics);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_topic, null);
            }
            Topic c = (Topic)getListAdapter().getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());


            CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(false);


            return convertView;
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
//        Crime cr = (Crime)(getListAdapter().getItem(position));
        Topic cr = ((TopicAdapter)getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), SingleFragmentActivity.class);

        startActivity(i);
    }

}



