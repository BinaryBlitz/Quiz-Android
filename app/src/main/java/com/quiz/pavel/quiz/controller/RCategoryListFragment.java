package com.quiz.pavel.quiz.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Category;
import com.quiz.pavel.quiz.model.Mine;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by pavelkozemirov on 12.12.14.
 */
public class RCategoryListFragment extends Fragment {
    private static final String TAG = "RCategoryListFragment";

    private ArrayList<Category> mCategories;

    ListView listView;

    public static RCategoryListFragment newInstance() {
        Bundle args = new Bundle();
        RCategoryListFragment fragment = new RCategoryListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
        ImageLoader.getInstance().init(config);

        mCategories = new ArrayList<Category>();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categories, parent, false);
        listView = (ListView) v.findViewById(R.id.listView);
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest stringRequest = new JsonArrayRequest(Mine.URL + "/categories" + "?token=" +
                Mine.getInstance(getActivity()).getToken(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(getActivity() == null) {
                            return;
                        }
                        mCategories.clear();
                        Mine.getInstance(getActivity()).saveCatTopicJsonAr(getActivity(), response);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                mCategories.add(new Category(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                Log.d(TAG, "Error, JSONException");
                            }
                        }

                        TopicAdapter arrayAdapter = new TopicAdapter(mCategories);
                        listView.setAdapter(arrayAdapter);
                        setRetainInstance(true);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Плохое соединение", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Error Response, have no data from server");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RTopicListActivity.class);
                intent.putExtra(RTopicListFragment.keyRTopicListFragment, mCategories.get(position).getId());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });
        return v;
    }



    private class TopicAdapter extends ArrayAdapter<Category> {
        public TopicAdapter(ArrayList<Category> topics) {
            super(getActivity(), 0, topics);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_category, null);
            }
            Category c = (Category)mCategories.get(position);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.list_item_titleTextView);
            titleTextView.setText(c.getTitle());

            ImageView image = (ImageView) convertView.findViewById(R.id.myImageView);

            ImageLoader.getInstance().displayImage(Mine.URL_photo + c.mBannerUrl, image, options);

            return convertView;
        }
    }
}



