package com.luseen.spacenavigation;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.View;
import android.widget.ImageView;

class Utils {
    Utils() {
    }

    static void changeImageViewTint(ImageView imageView, int color) {
        imageView.setColorFilter(color);
    }

    static void changeViewVisibilityGone(View view) {
        if (view != null && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    static void changeViewVisibilityVisible(View view) {
        if (view != null && view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    static void changeImageViewTintWithAnimation(final ImageView image, int fromColor, int toColor) {
        ValueAnimator imageTintChangeAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(fromColor), Integer.valueOf(toColor)});
        imageTintChangeAnimation.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animator) {
                image.setColorFilter(((Integer) animator.getAnimatedValue()).intValue());
            }
        });
        imageTintChangeAnimation.setDuration(150);
        imageTintChangeAnimation.start();
    }

    static void makeTranslationYAnimation(View view, float value) {
        view.animate().translationY(value).setDuration(150).start();
    }

    @TargetApi(21)
    static RippleDrawable getPressedColorRippleDrawable(int normalColor, int pressedColor) {
        return new RippleDrawable(getPressedColorSelector(normalColor, pressedColor), new ColorDrawable(normalColor), null);
    }

    private static ColorStateList getPressedColorSelector(int normalColor, int pressedColor) {
        int[][] iArr = new int[4][];
        iArr[0] = new int[]{16842919};
        iArr[1] = new int[]{16842908};
        iArr[2] = new int[]{16843518};
        iArr[3] = new int[0];
        return new ColorStateList(iArr, new int[]{pressedColor, pressedColor, pressedColor, normalColor});
    }
}
