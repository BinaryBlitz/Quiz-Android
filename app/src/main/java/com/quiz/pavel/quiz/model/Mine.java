package com.quiz.pavel.quiz.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by pavelkozemirov on 18.02.15.
 */
public class Mine {
    private static Mine sMine;

    private String mName;
    private String mEmail;
    private String mToken;
    private int mId;

    private Mine(JSONObject json) throws JSONException {
        mName = json.getString("name");
        mEmail = json.getString("email");
        mToken = json.getString("api_key");
        mId = json.getInt("id");

        //TODO: replace with preference store, and remove it
        try {
            IntentJSONSerializer.getInitialize().saveData(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Mine(){
        try {
            JSONObject json = IntentJSONSerializer.getInitialize().loadData();
            mName = json.getString("name");
            mEmail = json.getString("email");
            mToken = json.getString("api_key");
            mId = json.getInt("id");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static Mine getInstance(JSONObject json) throws JSONException {
        String name = json.getString("name");
        String email = json.getString("email");
        String token = json.getString("api_key");
        int id = json.getInt("id");
        if(sMine == null){
            sMine = new Mine(json);
        }
        return sMine;
    }

    public static Mine getInstance(){
        if(sMine == null){
            sMine = new Mine();
        }
        return sMine;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public boolean isSignIn(){
        return IntentJSONSerializer.getInitialize().hasAccount();
    }

    public void logOut(){
        JSONObject json = new JSONObject();
        try {
            json.put("login",false);
        } catch (JSONException e) {
        }

        try {
            IntentJSONSerializer.getInitialize().saveData(json);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
