package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import com.quiz.pavel.quiz.model.Topic;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by pavelkozemirov on 12.12.14.
 */
public class RCategoryListFragment extends ListFragment {
    private static final String TAG = "CategoryListFragment";

    private ArrayList<Category> mCategories;

    public static RCategoryListFragment newInstance() {
        Bundle args = new Bundle();
        RCategoryListFragment fragment = new RCategoryListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//          TODO: recomment if it is nessecary to download list of categories
//        mCategories = new ArrayList<Category>();
//
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//
//        JsonArrayRequest stringRequest = new JsonArrayRequest(Mine.URL + "/categories" + "?token=" +
//                Mine.getInstance(getActivity()).getToken(),
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Mine.getInstance(getActivity()).saveCatTopicJsonAr(getActivity(),response);
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                mCategories.add(new Category(response.getJSONObject(i)));
//                            } catch (JSONException e) {
//                                Log.d(TAG, "Error, JSONException");
//                            }
//                        }
//                        TopicAdapter adapter = new TopicAdapter(mCategories);
//                        setListAdapter(adapter);
//
//                        setRetainInstance(true);
//                    }
//                }
//                , new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "Ошибка сервера", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "Error Response, have no data from server");
//            }
//        });
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        mCategories = Mine.getInstance(getActivity()).loadCategoryAr(getActivity());

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
        TopicAdapter adapter = new TopicAdapter(mCategories);
        setListAdapter(adapter);

        setRetainInstance(true);
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
                        .inflate(R.layout.list_item_topic, null);
            }
            Category c = (Category) getListAdapter().getItem(position);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.list_item_titleTextView);
            titleTextView.setText(c.getTitle());


            return convertView;
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Category category = ((TopicAdapter) getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), RRatingActivity.class);
        i.putExtra("by_category", true);
        i.putExtra(RRatingActivity.EXTRA_NAME_OF_TOPIC, category.getTitle());

        i.putExtra("category_id", category.getId());

        startActivity(i);
    }

}



