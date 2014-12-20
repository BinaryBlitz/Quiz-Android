package com.quiz.pavel.quiz.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Created by pavelkozemirov on 13.12.14.
 */
public class SessionQuestion {

    private Question mQuestion;

    public Question getQuestion(){
        return mQuestion;
    }

    public SessionQuestion(String text){
        mQuestion = new Question(text);

        for (int i = 0; i < 4; i++) {
            if( mQuestion.getAnswers()[i].mIsCorrect == true){
                mCorrectAnswer = i;
            }
        }

    }
    public int mCorrectAnswer;





}
