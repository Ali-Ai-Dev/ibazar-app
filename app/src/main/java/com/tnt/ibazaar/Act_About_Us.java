package com.tnt.ibazaar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.context.IconicsLayoutInflater;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Act_About_Us extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.txt_about_us)
    TextView txt_about_us;

    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;

    private Act_About_Us mAct;
    private int h = 701;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__about__us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAct = this;
        ButterKnife.bind(mAct);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        collapse(txt_about_us);
    }

    private void collapse(final TextView view) {

        view.getLayoutParams().height = 0;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                Log.e("as", "" + interpolatedTime);
                view.getLayoutParams().height =
                        (int) (h * interpolatedTime);
                view.requestLayout();

                if (interpolatedTime == 0) {
                    view.setTextColor(Color.BLACK);
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        int d = (int) (h / getResources().getDisplayMetrics().density) * 3;

        a.setDuration(d);
        view.startAnimation(a);

    }

}
