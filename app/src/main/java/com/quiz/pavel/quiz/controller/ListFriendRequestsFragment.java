package com.quiz.pavel.quiz.controller;

import android.content.Intent;
import android.os.Bundle;
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

/**
 * Created by pavelkozemirov on 11.11.14.
 */
public class ListFriendRequestsFragment extends ListFragment {


    private final static String TAG = "ListFriendRequestsFragment";
    private ArrayList<String> mInts;
    private View mItem;
    private int mIconNumber;


    ArrayList<PlayerProfile> list = new ArrayList<PlayerProfile>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);



        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest arRequest = new JsonArrayRequest(Mine.URL
                + "/friendships/requests" + "?token="
                + Mine.getInstance(getActivity()).getToken(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        Log.d(TAG, "response: " + response.toString());



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

                            MyAdapter adapter = new MyAdapter(list);
                            setListAdapter(adapter);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(arRequest);




        setRetainInstance(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        ListView listView = (ListView)v.findViewById(android.R.id.list);


        return v;
    }






    @Override
    public void onListItemClick(ListView l, View v, int position, long id){


        RequestQueue queue = Volley.newRequestQueue(getActivity());

//        JSONObject params = new JSONObject();
//
//        try {
//            JSONObject par = new JSONObject();
//            par.put("topic_id", mTopicId);
//
//            params.put("lobby", par);
//
//            params.put("token", Mine.getInstance(getActivity()).getToken());
//
//        } catch (JSONException e) {
//            Log.d(TAG,"Problem with parsing json(Intent)");
//        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Mine.URL +
                "/friendships?friend_id=" + list.get(position).getId() + "&token=" +
                Mine.getInstance(getActivity()).getToken(), null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getActivity(), "added", Toast.LENGTH_SHORT).show();
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }
        };

        queue.add(stringRequest);

        getActivity().finish();

//        Intent i = new Intent(getActivity(), CrimeActivity.class);
//
//        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
//
//        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, cr.getId());
//        startActivity(i);

    }

    private class MyAdapter extends ArrayAdapter<PlayerProfile>{
        public MyAdapter(ArrayList<PlayerProfile> crimes){
            super(getActivity(),0,crimes);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_topic, null);
            }
            PlayerProfile c = (PlayerProfile)getListAdapter().getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.list_item_titleTextView);
            titleTextView.setText(c.getName());

//            TextView dateTextView = (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
//            dateTextView.setText(c.getDate().toString());
//
//            CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
//            solvedCheckBox.setChecked(c.isSolved());


            return convertView;
        }
    }


//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflator){
//        super.onCreateOptionsMenu(menu, inflator);
//        inflator.inflate(R.menu.fragment_crime_list, menu);
//        MenuItem itemIc = menu.findItem(R.id.menu_item_show_subtitle);
//        if ( (mIconNumber != 0)) {
//            itemIc.setIcon(mIconNumber);
//        }
//    }

//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case R.id.menu_item_new_crime:
//                Crime cr = new Crime();
//                CrimeLab.get(getActivity()).addCrime(cr);
//                Intent i = new Intent(getActivity(),CrimePagerActivity.class);
//                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, cr.getId());
//                startActivityForResult(i, 0);
//                return true;
//            case R.id.menu_item_show_subtitle:
//                mIconNumber = getRandomIcon(mIconNumber);
//                item.setIcon(mIconNumber);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
//        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item){
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
//        int position = info.position;
//        CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
//        Crime c = adapter.getItem(position);
//
//        switch (item.getItemId()){
//            case R.id.menu_item_delete_crime:
//                CrimeLab.get(getActivity()).deleteCrime(c);
//                adapter.notifyDataSetChanged();
//                return true;
//        }
//        return super.onContextItemSelected(item);
//    }

}