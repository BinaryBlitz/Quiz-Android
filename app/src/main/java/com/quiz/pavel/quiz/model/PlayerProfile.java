package com.quiz.pavel.quiz.model;

import android.content.Context;

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

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }



}
