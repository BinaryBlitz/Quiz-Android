package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.PlayerProfile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pavel on 12/03/15.
 */
public class SearchFragment extends Fragment {

    private final static String TAG = "SearchFragment";
    ArrayList<PlayerProfile> list = new ArrayList<PlayerProfile>();

    public interface OnAddNewFragmentCallback {
        public void addFragmentProfile(PlayerProfile p);
        public void removeFragment();
        public void search();
    }


    @InjectView(R.id.listView_search_results) ListView mListView;
    @InjectView(R.id.searchView) SearchView mSearch;

    OnAddNewFragmentCallback mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_search, parent, false);
        ButterKnife.inject(this, v);

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRequest(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRequest(newText);
                return true;
            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlayerProfile playerProfile = new PlayerProfile(getActivity(), list.get(position).getId(),
                        list.get(position).getName());
                mCallback.addFragmentProfile(playerProfile);
            }
        });

        return v;
    }

    private void searchRequest(String query) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        Log.d(TAG, "query = " + query);

        JsonArrayRequest arRequest = new JsonArrayRequest(Mine.URL
                + "/players/search?query=" + query + "&token="
                + Mine.getInstance(getActivity()).getToken(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        list.clear();
                        Log.d(TAG, "response = " + response);
                        for (int i = 0; i < response.length(); i++) {

                            int id  = 0;
                            String name = "";
                            try {
                                id = response.getJSONObject(i).getInt("id");
                                name = response.getJSONObject(i).getString("name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            list.add(new PlayerProfile(getActivity(), id, name));
                        }
                        MyAdapter arrayAdapter = new MyAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
                        mListView.setAdapter(arrayAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(arRequest);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnAddNewFragmentCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search_list, menu);
    }





//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id){
//
//
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//
////        JSONObject params = new JSONObject();
////
////        try {
////            JSONObject par = new JSONObject();
////            par.put("topic_id", mTopicId);
////
////            params.put("lobby", par);
////
////            params.put("token", Mine.getInstance(getActivity()).getToken());
////
////        } catch (JSONException e) {
////            Log.d(TAG,"Problem with parsing json(Intent)");
////        }
//
//        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Mine.URL +
//                "/friendships?friend_id=" + list.get(position).getId() + "&token=" +
//                Mine.getInstance(getActivity()).getToken(), null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Toast.makeText(getActivity(), "added", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                , new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json");
//                params.put("Accept", "application/json");
//                return params;
//            }
//        };
//
//        queue.add(stringRequest);
//
//        getActivity().finish();
//
////        Intent i = new Intent(getActivity(), CrimeActivity.class);
////
////        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
////
////        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, cr.getId());
////        startActivity(i);
//
//    }

//    private class MyAdapter extends ArrayAdapter<PlayerProfile> {
//        public MyAdapter(ArrayList<PlayerProfile> crimes){
//            super(getActivity(),0,crimes);
//        }
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent){
//
//            if (convertView == null) {
//                convertView = getActivity().getLayoutInflater()
//                        .inflate(R.layout.list_item_topic, null);
//            }
//            PlayerProfile c = (PlayerProfile)getListAdapter().getItem(position);
//
//            TextView titleTextView = (TextView)convertView.findViewById(R.id.list_item_titleTextView);
//            titleTextView.setText(c.getName());
//
////            TextView dateTextView = (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
////            dateTextView.setText(c.getDate().toString());
////
////            CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
////            solvedCheckBox.setChecked(c.isSolved());
//
//
//            return convertView;
//        }
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mCallback.removeFragment();
                break;
        }
        return true;
    }

    private class MyAdapter extends ArrayAdapter<PlayerProfile> {

        public MyAdapter(FragmentActivity activity, int simple_list_item_1, ArrayList<PlayerProfile> crimes){
            super(getActivity(), 0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_topic, null);
            }

            PlayerProfile c = (PlayerProfile)list.get(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.list_item_titleTextView);
            titleTextView.setText(c.getName());

            return convertView;
        }
    }

}
