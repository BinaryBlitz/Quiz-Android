package com.quiz.pavel.quiz.model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.quiz.pavel.quiz.R;

/**
 * Created by pavel on 19/03/15.
 */
public class MyButton extends Button{
    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private static final int[] STATE_MINE = {R.attr.state_my_answer};
    private static final int[] STATE_OPPONENTS = {R.attr.state_opponents_answer};

    private boolean mIsMine = false;
    private boolean mIsOpponents = false;

    public void setMineAnswer(boolean isFried) {mIsMine = isFried;}
    public void setOpponentsAnswer(boolean isBaked) {mIsOpponents= isBaked;}

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
        if (mIsMine) {
            mergeDrawableStates(drawableState, STATE_MINE);
        }
        if (mIsOpponents) {
            mergeDrawableStates(drawableState, STATE_OPPONENTS);
        }
        return drawableState;
    }

}
