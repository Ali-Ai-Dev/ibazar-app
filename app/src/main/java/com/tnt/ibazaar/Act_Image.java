package com.tnt.ibazaar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.squareup.picasso.Picasso;

import views.TouchImageView;

public class Act_Image extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__image);
        Act_Image mAct = this;
        String url = null;
        try {
            url = getIntent().getStringExtra("url");
        } catch (Exception e) {
            Log.e("image error", "" + e.getMessage());
        }
        if (url == null || url.isEmpty())
            finish();

        TouchImageView img = (TouchImageView) findViewById(R.id.img);
        Picasso.with(mAct)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .into(img);
    }
}
