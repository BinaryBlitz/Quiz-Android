package com.quiz.pavel.quiz.model;

import java.util.UUID;

/**
 * Created by pavelkozemirov on 11.12.14.
 */
public class Question {

    private UUID mId;
    private String mText;
    private String[] mAnswers;
    private int mCorrectAnswer;

    public Question(){

        mText = "What is your Name?";
        for (int i = 0; i < 4; i++) {
            mAnswers[i] = "name No: " + i + 1;
        }
        mCorrectAnswer = 3;
    }

    public Question(String text){

        mText = text;
        mAnswers = new String[4];
        for (int i = 0; i < 4; i++) {
            mAnswers[i] = "name No: " + (i + 1);
        }
        mCorrectAnswer = 3;
    }

    public String[] getAnswers() {
        return mAnswers;
    }

    public String getText() {
        return mText;
    }

//
//    public Question(String text, int answer){
//        mText = text;
//        mCorrectAnswer = answer;
//        mVariants = new String[4];
//        mVariants[0] = "aaa";
//        mVariants[1] = "bbb";
//        mVariants[2] = "ccc";
//        mVariants[3] = "ddd";
//    }





}
