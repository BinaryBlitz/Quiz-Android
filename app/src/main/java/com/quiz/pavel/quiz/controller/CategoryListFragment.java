package com.quiz.pavel.quiz.controller;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Category;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.PlayerProfile;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by pavelkozemirov on 12.12.14.
 */
public class CategoryListFragment extends MyFragment {
    private static final String TAG = "CategoryListFragment";

    private ArrayList<Category> mCategories;

    CategoryListListener mCallback;

    ListView listView;

    public interface CategoryListListener {
        public void onOpenTopicList(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mCategories = new ArrayList<Category>();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
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
                Toast.makeText(getActivity(), "Ошибка сервера", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Error Response, have no data from server");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onOpenTopicList(position);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainSlidingActivity) activity).onSectionAttached(2);
        mTitle = "Категории";

        try {
            mCallback = (CategoryListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    private class TopicAdapter extends ArrayAdapter<Category> {
        public TopicAdapter(ArrayList<Category> topics) {
            super(getActivity(), 0, topics);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_topic, null);
            }
            Category c = (Category) mCategories.get(position);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.list_item_titleTextView);
            titleTextView.setText(c.getTitle());


            return convertView;
        }
    }

}



