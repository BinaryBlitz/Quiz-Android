package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Achievement;
import com.quiz.pavel.quiz.model.Mine;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by pavel on 30/04/15.
 */
public class AchievementsFragment extends MyFragment {

    public final static String TAG = "Achievements";

    DisplayImageOptions options;

    ArrayList<Achievement> mAchievements = new ArrayList<Achievement>();

    GridView gridview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        mTitle = "Достижения";
        super.onAttach(activity);
    }

    AchievementsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.achievements_list, parent, false);

        gridview = (GridView) v.findViewById(R.id.gridView_achievements);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest arRequest = new JsonArrayRequest(Mine.URL
                + "/achievements?token=" + Mine.getInstance(getActivity()).getToken(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "res= " + response);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                mAchievements.add(new Achievement(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        gridview.setAdapter(adapter = new AchievementsAdapter(getActivity()));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(arRequest);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(mAchievements.get(position).getName())
                        .setMessage(mAchievements.get(position).getDescription())
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return v;
    }

    public class AchievementsAdapter extends BaseAdapter {
        private Context mContext;

        public AchievementsAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mAchievements.size();
        }

        public Object getItem(int position) {
            return mAchievements.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.achievement_item, null);
            }

            ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView_achievement);

            if (mAchievements.get(position).getUrl() == null || mAchievements.get(position).getUrl().equals("null")){
                Picasso.with(getActivity())
                        .load(R.drawable.catty)
                        .into(imageView);
            }

            Picasso.with(getActivity())
                    .load(Mine.URL_photo + mAchievements.get(position).getUrl())
                    .placeholder(R.drawable.catty)
                    .into(imageView);

            TextView textView = (TextView)convertView.findViewById(R.id.textView_achievement);
            textView.setText(mAchievements.get(position).getName());

            return convertView;
        }

        public Drawable getDrawable(int pos) {
            return getView(pos, null, null).findViewById(R.id.imageView_achievement).getBackground();
        }


    }

}
