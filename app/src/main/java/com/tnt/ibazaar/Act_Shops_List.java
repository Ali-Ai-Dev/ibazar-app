package com.tnt.ibazaar;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cjj.sva.JJSearchView;
import com.cjj.sva.anim.controller.JJChangeArrowController;
import com.google.android.gms.maps.model.LatLng;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.entypo_typeface_library.Entypo;
import com.mikepenz.foundation_icons_typeface_library.FoundationIcons;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.Adapter_Shops;
import butterknife.BindView;
import butterknife.ButterKnife;
import models.ServerResponse;
import models.shop.Shop;
import network.webconnection.WebConnection;
import tools.CallBack;
import views.BadgeView;
import views.LoadMapDialog;

public class Act_Shops_List extends AppCompatActivity {

    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.img_basket)
    ImageView img_basket;
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
    private Act_Shops_List mAct;
    private int category;
    private RecyclerView list_shops;
    private int type;
    private LatLng latLng;
    private int last_shop_position_request = 0;
    //    private Dialog mDialog;
    private int last_shop_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__shops);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAct = this;
        ButterKnife.bind(mAct);

        list_shops = (RecyclerView) findViewById(R.id.list_shops);
        list_shops.setLayoutManager(new LinearLayoutManager(mAct));

        try {
            category = getIntent().getExtras().getInt("category");
        } catch (Exception e) {
            finish();
        }

        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final JJSearchView jjsv = (JJSearchView) findViewById(R.id.jjsv);
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

        img_topest.setImageDrawable(new IconicsDrawable(mAct,
                Entypo.Icon.ent_medal)
                .color(Color.BLACK)
                .sizeDp(20));

        img_nearest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (latLng == null)
                    LoadMapDialog.loadMap(mAct, new CallBack() {
                        @Override
                        public void callback(LatLng latLng) {
                            last_shop_id = 0;
                            mAct.latLng = latLng;
                            img_nearest.setImageDrawable(new IconicsDrawable(mAct,
                                    GoogleMaterial.Icon.gmd_gps_fixed)
                                    .color(Color.parseColor("#0BA44A"))
                                    .sizeDp(20));
                            getShops(type);
                        }
                    }, latLng);
                else {
                    latLng = null;
                    img_nearest.setImageDrawable(new IconicsDrawable(mAct,
                            GoogleMaterial.Icon.gmd_gps_fixed)
                            .color(Color.BLACK)
                            .sizeDp(20));
                    getShops(type);
                }
            }
        });


        img_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                last_shop_id = 0;
                changeColors(1);
                getShops(1);

            }
        });

        img_most_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                last_shop_id = 0;
                changeColors(2);
                getShops(2);
            }
        });

        img_topest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                last_shop_id = 0;
                changeColors(3);
                getShops(3);
            }
        });

        img_most_stared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                last_shop_id = 0;
                changeColors(4);
                getShops(4);
            }
        });
        last_shop_id = 0;
//        new GetShops().execute();
        getShops(1);

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mAct, Act_Search.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void changeColors(int i) {
        if (i == type)
            return;
//        ImageView img_current;
//        ImageView img_next;

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

//    public void getNextShops(int position, final int last_shop_id) {
//        if (position > last_shop_position_request) {
////            Log.e("req", "ok");
//            this.last_shop_id = last_shop_id;
////            new GetShops().execute();
//            Application.showProgressDialog(mAct);
//            getShops(type);
//
//            last_shop_position_request = position;
//        } else {
////            Log.e("req", "dropped " + last_shop_position_request);
//        }
//    }

    private void getShops(int i) {
        type = i;

        Application.showProgressDialog(mAct);
        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("type", "" + type)
                .appendQueryParameter("categoryId", "" + category)
                .appendQueryParameter("distance", "5");
        if (last_shop_id != 0)
            builder.appendQueryParameter("lastShopId", "" + last_shop_id);

        if (latLng != null && latLng.latitude != 0 && latLng.longitude != 0) {
            builder.appendQueryParameter("lat", "" + latLng.latitude)
                    .appendQueryParameter("lng", "" + latLng.longitude);
        }

        Log.e("builder", builder.toString());

        new WebConnection(new WebConnection.ConnectionResponse() {
            @Override
            public void connectionFinish(String result, String urlCanConnect) {
                Log.e("finishing?", mAct.isFinishing() + "");
                Application.dismissProgressDialog();
                ServerResponse response = null;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    response = new ServerResponse();
                    response.setStatus(jsonObject.getInt("Status"));
                    response.setMessage(jsonObject.getString("MSG"));
                    if (response.getStatus() == 100) {
                        try {
                            if (jsonObject.has("Data")) {
                                JSONObject Data = jsonObject.getJSONObject("Data");
                                JSONArray jsonArray = Data.getJSONArray("Shops");
                                ArrayList<Shop> shops = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    Shop shop = new Shop();
                                    shop.setId(jsonObject.getInt("ID"));
                                    shop.setTitle(Application.NormalizeString(jsonObject.getString("Title")));
                                    shop.setAddress(Application.NormalizeString(jsonObject.getString("Address")));
                                    if (jsonObject.has("ImgName"))
                                        shop.setImg_main(Application.NormalizeString(jsonObject.getString("ImgName")));
                                    shop.setMedal(jsonObject.getInt("Medal"));
                                    shop.setLat(Application.NormalizeString(jsonObject.getString("Lat")));
                                    shop.setLng(Application.NormalizeString(jsonObject.getString("Lng")));
                                    shop.setWebSite(Application.NormalizeString(jsonObject.getString("WebSite")));
                                    shop.setEmail(Application.NormalizeString(jsonObject.getString("Email")));
                                    shop.setTell(Application.NormalizeString(jsonObject.getString("Tell")));
                                    shop.setScore((float) jsonObject.getDouble("Score"));
                                    shop.setVote_count(jsonObject.getInt("VoteCount"));
                                    shops.add(shop);
                                }
                                response.setData(shops);
                            } else {
                                response.setData(new ArrayList<>());
                            }
                        } catch (Exception e) {
                            Log.e("error", " " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    Log.e("getShops error", " " + e.getMessage());
                }

                ProgressView progressView = (ProgressView) findViewById(R.id.progress);
                progressView.setVisibility(View.GONE);
                if (response == null) {
                    if (last_shop_id == 0) {
                        Toast.makeText(mAct, R.string.error_connecting_to_server, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                } else if (response.getStatus() != 100) {
                    if (last_shop_id == 0) {
                        Toast.makeText(mAct, R.string.error_connecting_to_server, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    Snackbar snackbar = Snackbar.make(getCurrentFocus(),
                            response.getMessage(), Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getShops(type);
                        }
                    });
                    snackbar.getView().setBackgroundResource(R.color.accent);
                    snackbar.show();

                } else {

                    ArrayList<Shop> data = (ArrayList<Shop>) response.getData();
                    if (last_shop_id == 0)
                        list_shops.setAdapter(new Adapter_Shops(mAct, data));
                    else {
                        Adapter_Shops adapter_shops = (Adapter_Shops) list_shops.getAdapter();
                        adapter_shops.addData(data);
                    }
                }

            }
        }).connect(builder, "fetchShopListCategory", "GET", 0);
    }

}
