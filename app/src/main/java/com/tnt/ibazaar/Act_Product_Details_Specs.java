package com.tnt.ibazaar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.Adapter_Specs;
import models.Specs;
import views.BadgeView;

public class Act_Product_Details_Specs extends AppCompatActivity {

    private Act_Product_Details_Specs mAct;

    private String specsString;
    private JSONArray specsJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__product__details__specs);
        mAct = this;

        try {
            specsString = getIntent().getStringExtra("specs");
            specsJson = new JSONArray(specsString);
        } catch (Exception e) {
            Log.e("error", " " + e.getMessage());
        }

        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView img_basket = (ImageView) findViewById(R.id.img_basket);
        img_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mAct, Act_Basket.class));
            }
        });

        RecyclerView list_specs = (RecyclerView) findViewById(R.id.list_specs);
        list_specs.setLayoutManager(new LinearLayoutManager(mAct));
        list_specs.setAdapter(new Adapter_Specs(mAct, getSpecsData()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageView img_basket = (ImageView) findViewById(R.id.img_basket);
        BadgeView badge = new BadgeView(mAct);
        badge.setTargetView(img_basket);
        badge.setBadgeCount(Application.getCountPaymentCartOrderItems());
        img_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mAct, Act_Basket.class));
            }
        });
    }

    private ArrayList<Specs> getSpecsData() {
        try {
            ArrayList<Specs> data = new ArrayList<>();
            for (int i = 0; i < specsJson.length(); i++) {
                JSONObject object = specsJson.getJSONObject(i);
                Specs specs = new Specs();
                specs.setName(object.getString("AttributeName"));
                specs.setValue(object.getString("value"));
                data.add(specs);
            }
            return data;
        } catch (Exception e) {
            Log.e("error", " " + e.getMessage());
        }
        return null;
    }
}
