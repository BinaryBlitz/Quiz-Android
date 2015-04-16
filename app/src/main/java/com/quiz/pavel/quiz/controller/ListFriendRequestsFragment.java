package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.*;
import android.widget.*;

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
import com.quiz.pavel.quiz.model.PlayerRating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;

/**
 * Created by pavelkozemirov on 11.11.14.
 */
public class ListFriendRequestsFragment extends MyFragment {

    private final static String TAG = "ListFriendRequestsFragment";
    private ArrayList<String> mInts;
    private View mItem;
    private int mIconNumber;

    ListView listView;
    PlayerProfile mPlayerProfile;

    ArrayList<PlayerProfile> list = new ArrayList<PlayerProfile>();

    public ListFriendRequestsFragment(PlayerProfile p) {
        mPlayerProfile = p;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        mTitle = mPlayerProfile.getName();
        super.onAttach(activity);
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_friend_list, parent, false);

        listView = (ListView)v.findViewById(R.id.list_friends);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        list.clear();
        JsonArrayRequest arRequest = new JsonArrayRequest(Mine.URL
                + "/players/" + mPlayerProfile.getId() + "/friends?token="
                + Mine.getInstance(getActivity()).getToken(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            int id  = 0;
                            String name = "";
                            String url = "";
                            try {
                                id = response.getJSONObject(i).getInt("id");
                                name = response.getJSONObject(i).getString("name");
                                url = response.getJSONObject(i).getString("avatar_url");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            list.add(new PlayerProfile(getActivity(), id, name, url));
                        }

                        MyAdapter arrayAdapter = new MyAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
                        listView.setAdapter(arrayAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(arRequest);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMyFragmentListenerCallback.openProfileFragment(list.get(position));
            }
        });

        return v;
    }





//
//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id){
//
//
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//
//        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Mine.URL +
//                "/friendships?friend_id=" + list.get(position).getId() + "&token=" +
//                Mine.getInstance(getActivity()).getToken(),
//                new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Toast.makeText(getActivity(), "added", Toast.LENGTH_SHORT).show();
//            }
//        }
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
//    }

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