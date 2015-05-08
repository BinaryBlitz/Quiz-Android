package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.Topic;

import java.util.ArrayList;

/**
 * Created by pavelkozemirov on 15.02.15.
 */
public class TopicListFragment extends MyFragment {
    private static final String TAG = "TopicListFragment";

    public interface TopicListListener {
        public void onOpenRating();
    }

    TopicListListener mCallback;

    private ArrayList<Topic> mTopics;

    private int mNumberOfCategory = 0;

    public TopicListFragment(int numberOfCategory) {
        mNumberOfCategory = numberOfCategory;
    }

    DisplayImageOptions options;

    ExpandableListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

    }

    @Override
    public void onAttach(Activity activity) {
        mTitle = Mine.getInstance(getActivity())
                .loadCategoryAr(getActivity()).get(mNumberOfCategory).getTitle();
        super.onAttach(activity);

        try {
            mCallback = (TopicListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_topics, parent, false);

        mTopics = Mine.getInstance(getActivity()).loadCategoryAr(getActivity())
                .get(mNumberOfCategory).mTopics;
        listView = (ExpandableListView) v.findViewById(R.id.listView);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setGroupIndicator(null);
        ExpandableTopicAdapter adapter = new ExpandableTopicAdapter(mTopics);
        listView.setAdapter(adapter);


        setRetainInstance(true);

        String url = Mine.URL_photo + Mine.getInstance(getActivity())
                .loadCategoryAr(getActivity()).get(mNumberOfCategory).mBackgroundUrl;


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Topic cr = mTopics.get(position);
//
//                Intent i = new Intent(getActivity(), PreGameActivity.class);
//                i.putExtra("topic", mTopics.get(position).getId());
//                i.putExtra("name", mTopics.get(position).getTitle());
//                i.putExtra("category", mNumberOfCategory);
//
//                startActivity(i);
//            }
//        });

        ImageLoader.getInstance().loadImage(url, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Drawable drawable = new BitmapDrawable(getResources(), loadedImage);
                listView.setBackground(drawable);
            }
        });

        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    listView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mMyFragmentListenerCallback.back();
        }
        return true;
    }
}
