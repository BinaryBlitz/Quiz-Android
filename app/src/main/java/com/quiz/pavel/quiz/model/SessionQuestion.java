package com.quiz.pavel.quiz.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Created by pavelkozemirov on 13.12.14.
 */
public class SessionQuestion {

    private Question mQuestion;

    private UUID mIdQuestion;

    private int mAnswerMyPlayer;

    private int mAnswerOpponentPlayer;


    public int getPointMyPlayer(){
        return 15;
    }
    public int getPointOpponentPlayer(){
        return 15;
    }


    public Question getQuestion(){
        return mQuestion;
    }

    public SessionQuestion(String text){
        mQuestion = new Question(text);
    }


}
