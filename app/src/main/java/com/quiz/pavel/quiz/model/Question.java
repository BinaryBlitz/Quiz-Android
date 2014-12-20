package com.quiz.pavel.quiz.model;

import java.util.UUID;

/**
 * Created by pavelkozemirov on 11.12.14.
 */
public class Question {

    private UUID mId;
    private String mText;
    private Answer[] mAnswers;
    private int mCorrectAnswer;


    public Question(String text){

        mText = text;
        mAnswers = new Answer[4];
        for (int i = 0; i < 4; i++) {
            mAnswers[i] = new Answer("answer"+(i+1),false);
        }
        mAnswers[3].mIsCorrect = true;
    }

    public String[] getAnswersText() {
        String[] ar = new String[4];
        for (int i = 0; i < 4; i++) {
            ar[i] = mAnswers[i].mText;
        }
        return ar;
    }

    public Answer[] getAnswers(){
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
