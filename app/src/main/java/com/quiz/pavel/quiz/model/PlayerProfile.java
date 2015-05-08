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

    public String mAvatarUrl;

    public ArrayList<PlayerProfile> list;
    public ArrayList<Achievement> achievements;

    public PlayerProfile(Context c, int id, String name, String avatarUrl) {
        mId = id;
        mName = name;
        mAvatarUrl = avatarUrl;

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


    public String  getShortName() {
        if(mName.length() > 9) {
            return mName.substring(0,8).trim() + "...";
        }
        return mName;
    }

    public String getAvatarUrl() {
            return mAvatarUrl;
    }
}
