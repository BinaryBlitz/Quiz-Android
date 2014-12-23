package com.quiz.pavel.quiz.model;

import java.util.Random;
import java.util.UUID;

/**
 * Created by pavelkozemirov on 18.12.14.
 */
public class SessionManager {

    public Session mSession;

    private SessionQuestion mCurrentSessionQuestion;


    public SessionManager(){
        mSession = new Session();

    }

    public boolean amIWinner(){
        if(mSession.pointsMine > mSession.pointsOpponent){
            return true;                                                                                //BULLSHIT
        }
        return false;
    }


    public static SessionManager newInstance(){
        SessionManager ob = new SessionManager();
        return ob;
    }



    public void answerMine(int answer, int time){
        if(answer == mCurrentSessionQuestion.mCorrectAnswer){
            mSession.addPointsMe(150 - 10 * time);
        }


    }
    public void answerOpponent(int answer, int time){

        if(answer == mCurrentSessionQuestion.mCorrectAnswer){
            mSession.addPointsOpponent(150 - 10 * time);
        }
    }



    public Question getCurrentQuestion(){
        return mCurrentSessionQuestion.getQuestion();
    }



    // gets false if cannot carry on game = have not questions
    public boolean newRound(){
        if(mSession.mSessionQuestions.isEmpty()){
            return false;
        } else {
            mCurrentSessionQuestion = mSession.getSessionQuestion();
            return true;
        }
    }


}
