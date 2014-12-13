package com.quiz.pavel.quiz.model;

import java.util.ArrayList;
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

    public String getText(){
        return mQuestion.getText();
    }

    public Question getQuestion(){
        return mQuestion;
    }

    public SessionQuestion(){
        mQuestion = new Question("what is your name?", 2);

    }

    public  static ArrayList<SessionQuestion> generateSessionQuestions(){
        ArrayList<SessionQuestion> list = new ArrayList<SessionQuestion>();
        for (int i = 0; i < 6; i++) {

            list.add(new SessionQuestion());
        }
        return list;
    }



}
