package com.luseen.spacenavigation;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;

public class CentreButton extends FloatingActionButton {
    public CentreButton(Context context) {
        super(context);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);
        if (!result) {
            if (ev.getAction() == 1) {
                cancelLongPress();
            }
            setPressed(false);
        }
        return result;
    }
}
