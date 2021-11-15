package com.luseen.spacenavigation;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.Behavior;
import android.support.design.widget.Snackbar.SnackbarLayout;
import android.util.AttributeSet;
import android.view.View;

class SpaceNavigationViewBehavior<V extends View> extends Behavior<V> {
    public SpaceNavigationViewBehavior(Context context, AttributeSet attrs) {
    }

    public boolean layoutDependsOn(CoordinatorLayout parent, V v, View dependency) {
        return dependency instanceof SnackbarLayout;
    }

    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        child.setTranslationY(Math.min(0.0f, dependency.getTranslationY() - ((float) dependency.getHeight())));
        return true;
    }

    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == 2 || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    public void onNestedScroll(CoordinatorLayout coordinatorLayout, V child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0) {
            Utils.makeTranslationYAnimation(child, (float) child.getHeight());
        } else if (dyConsumed < 0) {
            Utils.makeTranslationYAnimation(child, 0.0f);
        }
    }
}
