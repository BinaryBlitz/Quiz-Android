package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
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
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
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
    ArrayList<Achievement> mMyAchievements;

    GridView gridview;

    public AchievementsFragment(ArrayList<Achievement> ar) {
        mMyAchievements = ar;
    }

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

        String url = Mine.URL + "/achievements?token=" + Mine.getInstance(getActivity()).getToken();
        Log.d(TAG, "url = " + url);
        JsonArrayRequest arRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
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
                        for (int i = 0; i < mAchievements.size(); i++) {
                            mAchievements.get(i).setAchieved(false);
                            for (int j = 0; j < mMyAchievements.size(); j++) {
                                if (mAchievements.get(i).getId() == mMyAchievements.get(j).getId()) {
                                    mAchievements.get(i).setAchieved(true);
                                }
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.achievement_item, null);
            }

            final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView_achievement);

            if (mAchievements.get(position).getUrl() == null || mAchievements.get(position).getUrl().equals("null")){
                Picasso.with(getActivity())
                        .load(R.drawable.catty)
                        .into(imageView);
            }

            ImageLoader.getInstance().loadImage(Mine.URL_photo + mAchievements.get(position).getUrl(),
                    options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    Drawable drawable = new BitmapDrawable(getResources(), loadedImage);
                    if (mAchievements.get(position).isAchieved()) {
                        imageView.setImageBitmap(loadedImage);
                    } else {
                        imageView.setImageBitmap(toGrayscale(loadedImage));
                    }
                }
            });

            TextView textView = (TextView)convertView.findViewById(R.id.textView_achievement);
            textView.setText(mAchievements.get(position).getName());

            return convertView;
        }


        public Bitmap toGrayscale(Bitmap bmpOriginal) {
            int width, height;
            height = bmpOriginal.getHeight();
            width = bmpOriginal.getWidth();

            Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmpGrayscale);
            Paint paint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            paint.setColorFilter(f);
            c.drawBitmap(bmpOriginal, 0, 0, paint);
            return bmpGrayscale;
        }

    }

}
