package com.quiz.pavel.quiz.model;

/**
 * Created by pavelkozemirov on 20.12.14.
 */
public class Topic {

    static int i = 0;

    public String getTitle(){
        return "theme " + (i++);
    }

}
