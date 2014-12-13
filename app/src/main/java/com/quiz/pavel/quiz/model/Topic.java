package com.quiz.pavel.quiz.model;

/**
 * Created by pavelkozemirov on 11.12.14.
 */
public class Topic {

    private CharSequence mTitle;

    public Topic(String title){
        mTitle = title;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public void setTitle(CharSequence title) {
        this.mTitle = title;
    }
}
