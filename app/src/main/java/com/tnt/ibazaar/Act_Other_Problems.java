package com.tnt.ibazaar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class Act_Other_Problems extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__other__problems);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        CardView cardView = (CardView) findViewById(R.id.cardView);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
                cardView.getLayoutParams();
        params.width = (int) (width * 0.9);
        params.height = (int) (height * 0.9);
        cardView.setLayoutParams(params);

        setFinishOnTouchOutside(false);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out_right);
    }
}
