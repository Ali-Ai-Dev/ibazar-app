package com.luseen.spacenavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.widget.RelativeLayout;

@SuppressLint({"ViewConstructor"})
class BezierView extends RelativeLayout {
    private int backgroundColor;
    private int bezierHeight;
    private int bezierWidth;
    private Context context;
    private Paint paint = new Paint(1);
    private Path path = new Path();

    BezierView(Context context, int backgroundColor) {
        super(context);
        this.context = context;
        this.backgroundColor = backgroundColor;
        this.paint.setAntiAlias(true);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setBackgroundColor(ContextCompat.getColor(this.context, R.color.space_transparent));
    }

    protected void onDraw(Canvas canvas) {
        this.path.reset();
        this.path.moveTo(0.0f, (float) this.bezierHeight);
        this.path.cubicTo((float) (this.bezierWidth / 4), (float) this.bezierHeight, (float) (this.bezierWidth / 4), 0.0f, (float) (this.bezierWidth / 2), 0.0f);
        this.path.cubicTo((float) ((this.bezierWidth / 4) * 3), 0.0f, (float) ((this.bezierWidth / 4) * 3), (float) this.bezierHeight, (float) this.bezierWidth, (float) this.bezierHeight);
        this.paint.setStyle(Style.FILL);
        this.paint.setColor(this.backgroundColor);
        canvas.drawPath(this.path, this.paint);
        this.paint.setStyle(Style.STROKE);
        this.paint.setColor(Color.parseColor("#b8b8b8"));
        canvas.drawPath(this.path, this.paint);
    }

    void build(int bezierWidth, int bezierHeight) {
        this.bezierWidth = bezierWidth;
        this.bezierHeight = bezierHeight;
    }

    void changeBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        invalidate();
    }
}
