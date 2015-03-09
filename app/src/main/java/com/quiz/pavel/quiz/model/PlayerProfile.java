package com.quiz.pavel.quiz.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.quiz.pavel.quiz.controller.ProfileFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by pavel on 07/03/15.
 */
public class PlayerProfile {

    private String mName;
    private int mId;

    private boolean myProfile = false;

    public ArrayList<PlayerProfile> list;

    public PlayerProfile(Context c, int id, String name) {
        mId = id;
        mName = name;

        if( id == Mine.getInstance(c).getId()) {
            myProfile = true;
        }
    }

    public boolean isMe() {
        return myProfile;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public boolean isInMyFriends(ArrayList<PlayerProfile> list) {
        if(isMe()) {
            return false;
        }
        for (PlayerProfile playerProfile : list) {
            if(playerProfile.getId() == mId) {
                return true;
            }
        }
        return false;
    }



}
