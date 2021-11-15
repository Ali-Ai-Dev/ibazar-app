package com.tnt.ibazaar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rey.material.widget.Button;

public class Act_Report extends AppCompatActivity {

    private Act_Report mAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__report);

        mAct = this;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Log.e("h", "" + height + " , w: " + width);
        LinearLayout cardView = (LinearLayout) findViewById(R.id.root);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
                cardView.getLayoutParams();
        params.width = (int) (width * 0.9);
        params.height = (int) (height * 0.9);
        cardView.setLayoutParams(params);

        setFinishOnTouchOutside(false);

        final AppCompatRadioButton rbtn1 =
                (AppCompatRadioButton) findViewById(R.id.rbtn1);
        final AppCompatRadioButton rbtn2 =
                (AppCompatRadioButton) findViewById(R.id.rbtn2);
        final AppCompatRadioButton rbtn3 =
                (AppCompatRadioButton) findViewById(R.id.rbtn3);
        final AppCompatRadioButton rbtn4 =
                (AppCompatRadioButton) findViewById(R.id.rbtn4);

        rbtn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbtn2.setChecked(false);
                    rbtn3.setChecked(false);
                    rbtn4.setChecked(false);
                }
            }
        });
        rbtn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbtn1.setChecked(false);
                    rbtn3.setChecked(false);
                    rbtn4.setChecked(false);
                }
            }
        });
        rbtn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbtn2.setChecked(false);
                    rbtn1.setChecked(false);
                    rbtn4.setChecked(false);
                }
            }
        });
        rbtn4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbtn2.setChecked(false);
                    rbtn3.setChecked(false);
                    rbtn1.setChecked(false);

                    try {
                        finish();
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out_left);

                        startActivity(new Intent(mAct, Act_Other_Problems.class));
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out_left);
                    } catch (Exception e) {
                        Log.e("eror", " " + e.getMessage());
                    }

                }
            }
        });
        TextView txt1 = (TextView) findViewById(R.id.txt1);
        TextView txt2 = (TextView) findViewById(R.id.txt2);
        TextView txt3 = (TextView) findViewById(R.id.txt3);
        TextView txt4 = (TextView) findViewById(R.id.txt4);
        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbtn1.setChecked(true);
            }
        });
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbtn2.setChecked(true);
            }
        });
        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbtn3.setChecked(true);
            }
        });
        txt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbtn4.setChecked(true);
            }
        });

        Button btn_report = (Button) findViewById(R.id.btn_report);
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    finish();
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out_left);
                } catch (Exception e) {
                    Log.e("eror", " " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        startActivity(new Intent(mAct, Act_Other_Problems.class));
//        try {
//            mAct.overridePendingTransition(R.anim.slide_in, R.anim.slide_in);
//        } catch (Exception e) {
//            Log.e("eror", " " + e.getMessage());
//        }
    }
}
