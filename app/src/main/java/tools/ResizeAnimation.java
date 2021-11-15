package tools;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Omid on 3/11/2018.
 */

//public class ResizeAnimation extends Animation {
//    final int startWidth;
//    final int targetWidth;
//    View view;
//
//    public ResizeAnimation(View view, int targetWidth) {
//        this.view = view;
//        this.targetWidth = targetWidth;
//        startWidth = view.getWidth();
//    }
//
//    @Override
//    protected void applyTransformation(float interpolatedTime, Transformation t) {
//
//        int newWidth = (int) (startWidth + (targetWidth - startWidth) * interpolatedTime);
//        view.getLayoutParams().width = newWidth;
//        view.requestLayout();
//    }
//
//    @Override
//    public void initialize(int width, int height, int parentWidth, int parentHeight) {
//        super.initialize(width, height, parentWidth, parentHeight);
//    }
//
//    @Override
//    public boolean willChangeBounds() {
//        return true;
//    }
//}

public class ResizeAnimation extends Animation {
    private View mView;
    private float mToHeight;
    private float mFromHeight;

    private float mToWidth;
    private float mFromWidth;

    public ResizeAnimation(View v, float fromWidth, float fromHeight, float toWidth, float toHeight) {
        mToHeight = toHeight;
        mToWidth = toWidth;
        mFromHeight = fromHeight;
        mFromWidth = fromWidth;
        mView = v;
        setDuration(300);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float height =
                (mToHeight - mFromHeight) * interpolatedTime + mFromHeight;
        float width = (mToWidth - mFromWidth) * interpolatedTime + mFromWidth;
        ViewGroup.LayoutParams p = mView.getLayoutParams();
        p.height = (int) height;
        p.width = (int) width;
        mView.requestLayout();
    }
}