package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.PlayerProfile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

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
        listView.setFooterDividersEnabled(true);
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
                                name = response.getJSONObject(i).getString("username");
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


    private class MyAdapter extends ArrayAdapter<PlayerProfile> {

        public MyAdapter(FragmentActivity activity, int simple_list_item_1, ArrayList<PlayerProfile> crimes){
            super(getActivity(), 0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_user, null);
            }

            PlayerProfile c = (PlayerProfile)list.get(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.list_item_titleTextView);
            titleTextView.setText(c.getName());

            ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView_photo);

            if (list.get(position).getAvatarUrl() == null || list.get(position).getAvatarUrl().equals("null")){
                Picasso.with(getActivity())
                        .load(R.drawable.catty)
                        .into(imageView);
            }

            Picasso.with(getActivity())
                    .load(Mine.URL_photo + list.get(position).getAvatarUrl())
                    .placeholder(R.drawable.catty)
                    .into(imageView);

            return convertView;
        }
    }


}