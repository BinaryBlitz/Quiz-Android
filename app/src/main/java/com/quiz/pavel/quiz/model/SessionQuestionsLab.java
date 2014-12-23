package com.quiz.pavel.quiz.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by pavelkozemirov on 18.12.14.
 */
public class SessionQuestionsLab {

    public SessionQuestionsLab(){

    }

    public static LinkedList<SessionQuestion> getSessionsQuestionArray(){

        LinkedList<SessionQuestion> list = new LinkedList<>();

        list.add( new SessionQuestion("what is my name?") );
        list.add( new SessionQuestion("what is your name?") );
        list.add( new SessionQuestion("what is his name?") );
        list.add( new SessionQuestion("what is her name?") );
        list.add( new SessionQuestion("what is cat's name?") );
        list.add( new SessionQuestion("what is dog's name?") );

        return list;
    }

}
