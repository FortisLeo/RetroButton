package com.rishitgoklani.retrobutton.RetroLayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.rishitgoklani.retrobutton.R;


public class RetroLayout extends CardView {

    private View root, rightShadow, bottomShadow;
    private CardView contentContainer;
    private long lastClicked = System.currentTimeMillis();
    private long duration = 90L;
    private RetroShimmerView shimmerView =null;
    private boolean animUpUnconsumed  = false;
    private STATE currState = STATE.RELEASED;
    private float space = getContext().getResources().getDimension(R.dimen.neopop_def_space);
    private float contentDisplacement = 2*space/3;
    private float shadowDisplacement = space/3;




    public RetroLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        root = LayoutInflater.from(context).inflate(R.layout.layout_retro_btn_default, this, true);
        contentContainer = root.findViewById(R.id.cv_content);
        rightShadow= root.findViewById(R.id.view_rightShadow);
        bottomShadow = root.findViewById(R.id.view_bottomShadow);
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0f);
        setClickable(true);



    }


    public boolean performClick(){
        int w = contentContainer.getWidth();
        int h = contentContainer.getHeight();


        if (shimmerView==null){
            shimmerView = new RetroShimmerView(getContext());
            shimmerView.setLayoutParams(new LayoutParams(w, h));
            shimmerView.setElevation(12f);

            contentContainer.addView(shimmerView);
        }

        shimmerView.startAnim((float) w);
        return super.performClick();
    }

    @Override
    protected void onAttachedToWindow(){
        super.onAttachedToWindow();
        if (contentContainer.getChildCount()>0) return;

        View view = getChildAt(1);

        removeView(view);
        (contentContainer).addView(view);


        boolean isWidthMatchParent = getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT;

        if (isWidthMatchParent) {
            getChildAt(0).getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

            ((ConstraintLayout.LayoutParams) contentContainer.getLayoutParams()).setMargins(
                    0,
                    0,
                    (int) (getResources().getDimension(R.dimen.neopop_def_space))-5,
                    0
            );
            contentContainer.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            invalidate();

        }
    }

    private enum STATE {
        PRESSING, PRESSED, RELEASING, RELEASED
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (event==null) return false;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (currState!=STATE.RELEASED) return false;

                if (System.currentTimeMillis() - lastClicked < (duration*2) + 100L) return false;
                lastClicked = System.currentTimeMillis();
                animateDown();

                return true;

            case MotionEvent.ACTION_UP:
                if (currState == STATE.PRESSING){
                    animUpUnconsumed = true;
                    performClick();
                    
                } else if (currState == STATE.PRESSED) {
                    animateUp();
                    performClick();

                    
                }

                return true;
            case MotionEvent.ACTION_MOVE:
                if (event.getX()<0 || event.getX()>getWidth() || event.getY()<0 || event.getY()>getHeight()){
                    if (currState == STATE.PRESSING){
                        animUpUnconsumed = true;
                        
                    } else if (currState == STATE.PRESSING) {

                        animateUp();
                    }
                    return true;
                }
                break;

        }
        return false;
    }


    public void animateDown(){
        contentContainer.animate().translationXBy(contentDisplacement)
                .translationYBy(contentDisplacement)
                .setDuration(duration)
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        currState = STATE.PRESSING;
                    }
                })
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        currState = STATE.PRESSED;

                        if (animUpUnconsumed){
                            animateUp();
                        }
                    }
                }).start();

        rightShadow.animate().translationXBy(-shadowDisplacement).translationYBy(-shadowDisplacement)
                .setDuration(duration)
                .start();

        bottomShadow.animate().translationYBy(-shadowDisplacement).translationXBy(-shadowDisplacement)
                .setDuration(duration)
                .start();
    }

    public void animateUp() {
        contentContainer.animate().translationXBy(-contentDisplacement).translationYBy(-contentDisplacement)
                .setDuration(duration)
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        currState = STATE.RELEASING;
                    }
                }).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        currState = STATE.RELEASED;
                        animUpUnconsumed = false;
                    }
                }).start();

        rightShadow.animate().translationXBy(shadowDisplacement)
                .translationYBy(shadowDisplacement)
                .setDuration(duration)
                .start();
        bottomShadow.animate().translationXBy(shadowDisplacement)
                .translationYBy(shadowDisplacement)
                .setDuration(duration)
                .start();
    }


    private float toPx(float dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                Resources.getSystem().getDisplayMetrics()

        );
    }


}
