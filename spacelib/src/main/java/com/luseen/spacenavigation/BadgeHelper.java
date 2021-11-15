package com.luseen.spacenavigation;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

class BadgeHelper {

    static class C08141 extends ViewPropertyAnimatorListenerAdapter {
        C08141() {
        }

        public void onAnimationEnd(View view) {
            Utils.changeViewVisibilityVisible(view);
        }
    }

    static class C08152 extends ViewPropertyAnimatorListenerAdapter {
        C08152() {
        }

        public void onAnimationEnd(View view) {
            Utils.changeViewVisibilityGone(view);
        }
    }

    BadgeHelper() {
    }

    static void showBadge(RelativeLayout view, BadgeItem badgeItem, boolean shouldShowBadgeWithNinePlus) {
        Utils.changeViewVisibilityVisible(view);
        TextView badgeTextView = (TextView) view.findViewById(R.id.badge_text_view);
        if (shouldShowBadgeWithNinePlus) {
            badgeTextView.setText(badgeItem.getBadgeText());
        } else {
            badgeTextView.setText(badgeItem.getFullBadgeText());
        }
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        //ViewCompat.animate(view).setDuration(200).scaleX(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT).scaleY(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT).setListener(new C08141()).start();
    }

    static void hideBadge(View view) {
        ViewCompat.animate(view).setDuration(200).scaleX(0.0f).scaleY(0.0f).setListener(new C08152()).start();
    }

    static void forceShowBadge(RelativeLayout view, BadgeItem badgeItem, boolean shouldShowBadgeWithNinePlus) {
        Utils.changeViewVisibilityVisible(view);
        if (VERSION.SDK_INT >= 16) {
            view.setBackground(makeShapeDrawable(badgeItem.getBadgeColor()));
        } else {
            view.setBackgroundDrawable(makeShapeDrawable(badgeItem.getBadgeColor()));
        }
        TextView badgeTextView = (TextView) view.findViewById(R.id.badge_text_view);
        if (shouldShowBadgeWithNinePlus) {
            badgeTextView.setText(badgeItem.getBadgeText());
        } else {
            badgeTextView.setText(badgeItem.getFullBadgeText());
        }
    }

    static ShapeDrawable makeShapeDrawable(int color) {
        ShapeDrawable badgeBackground = new ShapeDrawable(new OvalShape());
        badgeBackground.setIntrinsicWidth(10);
        badgeBackground.setIntrinsicHeight(10);
        badgeBackground.getPaint().setColor(color);
        return badgeBackground;
    }
}
