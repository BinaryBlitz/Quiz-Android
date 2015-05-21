package com.quiz.pavel.quiz.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Category;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.Topic;

import java.util.ArrayList;

/**
 * Created by pavelkozemirov on 15.02.15.
 */
public class RTopicListFragment extends Fragment {
    private static final String TAG = "RTopicListFragment";
    public static final String keyRTopicListFragment = "com.pavel.rtopicfragment";

    private ArrayList<Topic> mTopics;

    private int mIdOfCategory = 0;
    private Category mCategory;

    ListView listView;
    DisplayImageOptions options;




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

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
        ImageLoader.getInstance().init(config);

        mIdOfCategory = getActivity().getIntent().getIntExtra(keyRTopicListFragment, 0);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.r_fragment_topics, parent, false);

        mCategory = Mine.getInstance(getActivity()).loadCategoryAr(getActivity()).get(0);
        for (int i = 0; i < Mine.getInstance(getActivity()).loadCategoryAr(getActivity()).size(); i++) {
            if (mIdOfCategory ==  Mine.getInstance(getActivity()).loadCategoryAr(getActivity()).get(i).getId()) {
                mCategory = Mine.getInstance(getActivity()).loadCategoryAr(getActivity()).get(i);
            }
        }

        mTopics = mCategory.mTopics;
        listView = (ListView) v.findViewById(R.id.listView);

        TopicAdapter adapter = new TopicAdapter(mTopics);
        listView.setAdapter(adapter);


        setRetainInstance(true);

        Button btn = (Button)v.findViewById(R.id.button_like_item);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RatingTwoTabsForCatActivity.class);
                intent.putExtra("name", mCategory.getTitle());
                intent.putExtra("id", mCategory.getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        String url = Mine.URL_photo + mCategory.mBackgroundUrl;


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

        final LinearLayout mBackground = (LinearLayout)v.findViewById(R.id.background_view);

        ImageLoader.getInstance().loadImage(url, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Drawable drawable = new BitmapDrawable(getResources(), loadedImage);
                mBackground.setBackground(drawable);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RatingTwoTabsActivity.class);
                intent.putExtra("name", mTopics.get(position).getTitle());
                intent.putExtra("id", mTopics.get(position).getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_topic, null);
            }
            Topic c = (Topic) mTopics.get(position);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.list_item_titleTextView);
            titleTextView.setText(c.getTitle());

            ArcProgress progress = (ArcProgress) convertView.findViewById(R.id.arc_progress);
            progress.setArcAngle(360);

            TextView level = (TextView) convertView.findViewById(R.id.item_level);
            level.setText(String.valueOf(c.getLevel()));
            progress.setProgress(c.getProgress());

            return convertView;
        }
    }
}
