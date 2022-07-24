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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.Topic;

import java.util.ArrayList;

/**
 * Created by pavel on 27/03/15.
 */
public class ChallengeTopicListFragment extends MyFragment {
    private static final String TAG = "ChalleTopicListFragment";

    private ArrayList<Topic> mTopics;

    private int mNumberOfCategory = 0;
    private int mIdOfOpponent;

    DisplayImageOptions options;

    public ChallengeTopicListFragment(int numberOfCategory, int idOfOpponent) {
        mNumberOfCategory = numberOfCategory;
        mIdOfOpponent = idOfOpponent;
    }

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
    }

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categories, parent, false);

        mTopics = Mine.getInstance(getActivity()).loadCategoryAr(getActivity())
                .get(mNumberOfCategory).mTopics;
        listView = (ListView) v.findViewById(R.id.listView);


        TopicAdapter adapter = new TopicAdapter(mTopics);
        listView.setAdapter(adapter);

        setRetainInstance(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Topic cr = mTopics.get(position);

                Intent i = new Intent(getActivity(), PreGameActivity.class);
                i.putExtra("topic", mTopics.get(position).getId());
                i.putExtra("name", mTopics.get(position).getTitle());
                i.putExtra(PreGameActivity.EXTRA, 1);
                i.putExtra("opponent_id", mIdOfOpponent);
                i.putExtra("category", mNumberOfCategory);

                startActivity(i);
            }
        });

        String url = Mine.URL_photo + Mine.getInstance(getActivity())
                .loadCategoryAr(getActivity()).get(mNumberOfCategory).mBackgroundUrl;

        ImageLoader.getInstance().loadImage(url, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Drawable drawable = new BitmapDrawable(getResources(), loadedImage);
                listView.setBackground(drawable);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mMyFragmentListenerCallback.back();
        }
        return true;
    }
}
