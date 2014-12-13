package com.quiz.pavel.quiz.model;

import java.util.UUID;

/**
 * Created by pavelkozemirov on 11.12.14.
 */
public class Question {

    private String mText;

    private UUID mIdQuestion;

    private String[] mVariants;

    private int mCorrectAnswer;

    public Question(){
        mText = "What is your Name?";
        mCorrectAnswer = 3;
    }


    public Question(String text, int answer){
        mText = text;
        mCorrectAnswer = answer;
        mVariants = new String[4];
        mVariants[0] = "aaa";
        mVariants[1] = "bbb";
        mVariants[2] = "ccc";
        mVariants[3] = "ddd";
    }

    public String[] getVariants(){
        return mVariants;
    }


    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }


}
