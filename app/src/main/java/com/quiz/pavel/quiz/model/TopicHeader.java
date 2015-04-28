package com.quiz.pavel.quiz.model;

/**
 * Created by pavel on 28/04/15.
 */
public class TopicHeader extends Topic{

    public int headerNumber;//0 - favorite, 1- poplar, 2- new, 3- challenge
    public int color;

    public TopicHeader(int n, int c) {
        super();
        headerNumber = n;
        color = c;
    }


}
