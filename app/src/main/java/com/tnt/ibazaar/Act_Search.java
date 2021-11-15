package com.tnt.ibazaar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.cjj.sva.JJSearchView;
import com.cjj.sva.anim.controller.JJChangeArrowController;
import com.google.android.gms.maps.model.LatLng;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.entypo_typeface_library.Entypo;
import com.mikepenz.foundation_icons_typeface_library.FoundationIcons;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import tools.CallBack;
import views.LoadMapDialog;

public class Act_Search extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.jjsv)
    JJSearchView jjsv;

    @BindView(R.id.et_search)
    AppCompatEditText et_search;

    @BindView(R.id.img_clear)
    ImageView img_clear;

    @BindView(R.id.img_new)
    ImageView img_new;

    @BindView(R.id.img_most_liked)
    ImageView img_most_liked;

    @BindView(R.id.img_topest)
    ImageView img_topest;

    @BindView(R.id.img_nearest)
    ImageView img_nearest;

    @BindView(R.id.img_most_stared)
    ImageView img_most_stared;

    private Act_Search mAct;

    private LatLng latLng = null;
    private int type = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__search);
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

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });

        jjsv.setController(new JJChangeArrowController());

        jjsv.setOnClickListener(new View.OnClickListener() {
            private boolean flag = false;

            @Override
            public void onClick(View v) {
                if (!flag)
                    jjsv.startAnim();
                else jjsv.resetAnim();
                flag = !flag;
            }
        });

        img_nearest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (latLng == null)
                    LoadMapDialog.loadMap(mAct, new CallBack() {
                        @Override
                        public void callback(LatLng latLng2) {
                            //get brands
                            latLng = latLng2;
                            img_nearest.setImageDrawable(new IconicsDrawable(mAct,
                                    GoogleMaterial.Icon.gmd_gps_fixed)
                                    .color(Color.parseColor("#0BA44A"))
                                    .sizeDp(20));
                        }
                    },latLng);
                else {
                    latLng = null;
                    img_nearest.setImageDrawable(new IconicsDrawable(mAct,
                            GoogleMaterial.Icon.gmd_gps_fixed)
                            .color(Color.BLACK)
                            .sizeDp(20));
                }
            }
        });

        img_topest.setImageDrawable(new IconicsDrawable(mAct,
                Entypo.Icon.ent_medal)
                .color(Color.BLACK)
                .sizeDp(20));

        img_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColors(1);
                type = (1);

            }
        });

        img_most_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColors(2);
                type = (2);
            }
        });

        img_topest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColors(3);
                type = (3);
            }
        });

        img_most_stared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColors(4);
                type = 4;
            }
        });
    }

    private void changeColors(int i) {
        if (i == type)
            return;

        switch (i) {
            case 1:
                img_new.setImageDrawable(new IconicsDrawable(mAct,
                        FoundationIcons.Icon.fou_burst_new)
                        .color(Color.parseColor("#5FBDE8"))
                        .sizeDp(20));
                break;
            case 2:
                img_most_liked.setImageDrawable(new IconicsDrawable(mAct,
                        CommunityMaterial.Icon.cmd_heart)
                        .color(Color.parseColor("#F42630"))
                        .sizeDp(20));
                break;
            case 3:
                img_topest.setImageDrawable(new IconicsDrawable(mAct,
                        Entypo.Icon.ent_medal)
                        .color(Color.parseColor("#FF6600"))
                        .sizeDp(20));
                break;
            default:
                img_most_stared.setImageDrawable(new IconicsDrawable(mAct,
                        GoogleMaterial.Icon.gmd_star)
                        .color(Color.parseColor("#FFE391"))
                        .sizeDp(20));
        }

        switch (type) {
            case 1:
                img_new.setImageDrawable(new IconicsDrawable(mAct,
                        FoundationIcons.Icon.fou_burst_new)
                        .color(Color.BLACK)
                        .sizeDp(20));
                break;
            case 2:
                img_most_liked.setImageDrawable(new IconicsDrawable(mAct,
                        CommunityMaterial.Icon.cmd_heart)
                        .color(Color.BLACK)
                        .sizeDp(20));
                break;
            case 3:
                img_topest.setImageDrawable(new IconicsDrawable(mAct,
                        Entypo.Icon.ent_medal)
                        .color(Color.BLACK)
                        .sizeDp(20));
                break;
            default:
                img_most_stared.setImageDrawable(new IconicsDrawable(mAct,
                        GoogleMaterial.Icon.gmd_star)
                        .color(Color.BLACK)
                        .sizeDp(20));
        }

    }

}
