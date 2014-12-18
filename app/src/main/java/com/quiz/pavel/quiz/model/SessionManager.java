package com.quiz.pavel.quiz.model;

import java.util.UUID;

/**
 * Created by pavelkozemirov on 18.12.14.
 */
public class SessionManager {

    private Session mSession;


    public SessionManager(){
        mSession = new Session();
    }

    public static SessionManager newInstance(){
        SessionManager ob = new SessionManager();
        return ob;
    }



    public Question getCurrentQuestion(){
        return mSession.getSessionQuestion().getQuestion();
    }


}
