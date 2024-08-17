package com.rishitgoklani.retrobutton.RetroLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;

public class RetroShimmerView extends View {

    private float dx = 0f;
    private float shimmerWidth = 0f;
    private boolean isAnimInProgress = false;
    private final RectF rectF = new RectF(0f, 0f, 0f, 0f);
    private final Paint paint = new Paint();


    public RetroShimmerView(Context context) {
        super(context);
        paint.setColor(Color.parseColor("#DCDCDC"));
    }

    public void startAnim(Float widthProvided){
        if (isAnimInProgress) return;
        isAnimInProgress = true;
        shimmerWidth = widthProvided/10;

        dx = 0f;

        ValueAnimator animator = ValueAnimator.ofFloat(-shimmerWidth, widthProvided).setDuration(1000L);

        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            dx = (float)  animation.getAnimatedValue();
            invalidate();
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimInProgress = false;
            }
        });
        animator.start();

    }


    @Override
    protected void onDraw(@NonNull Canvas canvas){
        super.onDraw(canvas);
        rectF.left = getLeft() + dx;
        rectF.top = getTop();
        rectF.right = getLeft() + dx + shimmerWidth;
        rectF.bottom = getTop() + getHeight();

        if (canvas != null) {
            canvas.drawRect(rectF, paint);
        }
    }




}
