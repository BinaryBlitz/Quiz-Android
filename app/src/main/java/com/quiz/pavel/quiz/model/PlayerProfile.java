package com.quiz.pavel.quiz.model;

/**
 * Created by pavel on 07/03/15.
 */
public class PlayerProfile {

    private String mName;
    private int mId;

    private static int sId = 1;

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public PlayerProfile() {
        mId = sId++;
        mName = String.valueOf(sId);
    }

    public PlayerProfile(int id) {
        //TODO: find needed entiry from Mine and by id and instance
    }

}
