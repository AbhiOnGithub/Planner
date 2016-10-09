package com.james.planner.dialogs;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;

public class AnimatedDialog extends AppCompatDialog {

    public static final int TRANSITION_NONE = 0;
    public static final int TRANSITION_EXPAND = 1;
    public static final int TRANSITION_CIRCLE = 2;

    private int transition, clickedX, clickedY, clickedWidth, clickedHeight;
    private View rootView;

    public AnimatedDialog(Context context) {
        super(context);
    }

    public AnimatedDialog(Context context, int theme) {
        super(context, theme);
    }

    void animateView(View rootView) {
        this.rootView = rootView;

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                AnimatedDialog.this.rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if (transition == TRANSITION_EXPAND) {

                } else if (transition == TRANSITION_CIRCLE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ViewAnimationUtils.createCircularReveal(AnimatedDialog.this.rootView, clickedX, clickedY, 0, (float) Math.hypot(AnimatedDialog.this.rootView.getWidth(), AnimatedDialog.this.rootView.getHeight())).start();
                }
            }
        });
    }

    @Override
    public void dismiss() {
        if (transition == TRANSITION_CIRCLE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator animator = ViewAnimationUtils.createCircularReveal(AnimatedDialog.this.rootView, clickedX, clickedY, (float) Math.hypot(AnimatedDialog.this.rootView.getWidth(), AnimatedDialog.this.rootView.getHeight()), 0);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    AnimatedDialog.super.dismiss();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animator.start();
        } else super.dismiss();
    }

    public void setTransition(int transition, float x, float y, int width, int height) {
        this.transition = transition;
        clickedX = (int) x;
        clickedY = (int) y;
        clickedWidth = width;
        clickedHeight = height;
    }
}
