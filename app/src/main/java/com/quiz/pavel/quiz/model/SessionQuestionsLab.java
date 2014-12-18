package com.quiz.pavel.quiz.model;

import java.util.ArrayList;

/**
 * Created by pavelkozemirov on 18.12.14.
 */
public class SessionQuestionsLab {

    public SessionQuestionsLab(){

    }

    public static ArrayList<SessionQuestion> getSessionsQuestionArray(){

        ArrayList<SessionQuestion> list = new ArrayList<SessionQuestion>();

        for (int i = 0; i < 6; i++) {

            if( i % 2 == 0){
                list.add( new SessionQuestion("what is your name?") );
            } else {
                list.add( new SessionQuestion("what is his name?") );
            }
        }
        return list;
    }

}
