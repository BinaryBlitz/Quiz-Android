package com.quiz.pavel.quiz.controller;

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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Category;
import com.quiz.pavel.quiz.model.SessionQuestion;
import com.quiz.pavel.quiz.model.Topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pavelkozemirov on 12.12.14.
 */
public class CategoryListFragment extends ListFragment {
    private static final String TAG = "CategoryListFragment";

    private static final String URL = "https://protected-atoll-5061.herokuapp.com";


    private ArrayList<Category> mCategories;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //getActivity().setTitle(R.string.topics_title);
        mCategories = new ArrayList<Category>();
        Log.d(TAG, " Have begun downloading categories");

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        // Request a string response from the provided URL.
        // Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest(URL + "/categories",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                mCategories.add(new Category(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                Log.d(TAG, "Error, JSONException");
                            }
                        }
                        TopicAdapter adapter = new TopicAdapter(mCategories);
                        setListAdapter(adapter);

                        setRetainInstance(true);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Error Response, have no data from server" );
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        //TODO: save downloaded topics and categories


//
//        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//
//            }
//        });











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

    private class TopicAdapter extends ArrayAdapter<Category> {
        public TopicAdapter(ArrayList<Category> topics){
            super(getActivity(),0,topics);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_topic, null);
            }
            Category c = (Category)getListAdapter().getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());

            TextView titleTextView1 = (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            titleTextView1.setText(c.getTitleTopics());



            CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(false);


            return convertView;
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id){

        Category cr = ((TopicAdapter)getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), SingleFragmentActivity.class);

        startActivity(i);
    }

}


