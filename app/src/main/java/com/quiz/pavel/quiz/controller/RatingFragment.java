package com.quiz.pavel.quiz.controller;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class RatingFragment extends ListFragment{

    private ArrayList<Topic> mTopics;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

//        mItem = (ClipData.Item).findViewById(R.id.menu_item_show_subtitle);

       // getActivity().setTitle(R.string.topics_title);
        mTopics = new ArrayList<Topic>();
        mTopics.add(new Topic());
        mTopics.add(new Topic());
        mTopics.add(new Topic());



//        mTopics = CrimeLab.get(getActivity()).getCrimes();


//        ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(), android.R.layout.simple_list_item_1, mCrimes);
        TopicAdapter adapter = new TopicAdapter(mTopics);
        setListAdapter(adapter);

        setRetainInstance(true);
//        mIconNumber = android.R.drawable.ic_menu_help;

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        ListView listView = (ListView)v.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


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
}
