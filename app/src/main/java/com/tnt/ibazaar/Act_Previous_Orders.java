package com.tnt.ibazaar;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.rey.material.app.Dialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Act_Previous_Orders extends AppCompatActivity {

    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;

    @BindView(R.id.layout_no_orders)
    LinearLayout layout_no_orders;

    @BindView(R.id.img_no_orders)
    ImageView img_no_orders;

    private Act_Previous_Orders mAct;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__previous__orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAct = this;

        ButterKnife.bind(mAct);
//        showDialog();
//        TCPSocketInterface.getPreviousOrders(mAct);

        layout_no_orders.setVisibility(View.VISIBLE);
        img_no_orders.setImageDrawable(new IconicsDrawable(mAct,
                GoogleMaterial.Icon.gmd_shopping_cart)
                .color(Color.GRAY)
                .sizeDp(30));
    }

    private void showDialog() {
        mDialog = new Dialog(mAct);
        View view = getLayoutInflater().inflate(R.layout.dialog_please_wait, null);
        mDialog.setContentView(view);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    finish();
                }
                return false;
            }
        });
        mDialog.setCancelable(false);
//        if (last_shop_id == 0)
        mDialog.show();
//        else {
//            ProgressView progressView = (ProgressView) findViewById(R.id.progress);
//            progressView.setVisibility(View.VISIBLE);
//        }
    }

    private void dismissDialog() {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
    }
}
