package com.quiz.pavel.quiz.model;

/**
 * Created by pavelkozemirov on 20.12.14.
 */
public class Answer {

    public String mText;
    public boolean mIsCorrect;

    public Answer(String text, boolean isCorrect){
        mText = text;
        mIsCorrect = isCorrect;
    }

}
