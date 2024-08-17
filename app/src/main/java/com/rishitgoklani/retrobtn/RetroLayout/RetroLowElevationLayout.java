package com.rishitgoklani.retrobtn.RetroLayout;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.rishitgoklani.retrobtn.R;

public class RetroLowElevationLayout extends CardView {

    public RetroLowElevationLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        root = LayoutInflater.from(context).inflate(R.layout.layout_retro_btn_pressed, this, true);
        contentContainer = root.findViewById(R.id.cv_content);

        setCardElevation(0f);

    }

    private View root;
    private CardView contentContainer;

    @Override
    protected void onAttachedToWindow(){
        super.onAttachedToWindow();

        if (contentContainer.getChildCount()>0) return;

        if (getChildAt(1)!=null){
            View view = getChildAt(1);
            removeView(view);
            (contentContainer).addView(view);

        }

        boolean isWidthMatchParent = getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT;


        if (isWidthMatchParent){
            getChildAt(0).getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            ((ConstraintLayout.LayoutParams) contentContainer.getLayoutParams()).setMargins(
                    0,
                    0,
                    (int) (getResources().getDimension(R.dimen.neopop_def_space)-5),
                    0
            );

            contentContainer.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            invalidate();
        }
    }

    private float toPx(Number value){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value.floatValue(),
                Resources.getSystem().getDisplayMetrics()
        );
    }


}
