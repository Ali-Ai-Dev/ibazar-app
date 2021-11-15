package com.tnt.ibazaar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.rey.material.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Act_Support extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;

    private Act_Support mAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__support);
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

        LinearLayout layout_late_delivery = (LinearLayout) findViewById(R.id.layout_late_delivery);
        layout_late_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mAct, Act_Message_Support.class);
                intent.putExtra("title", getString(R.string.late_delivery));
                mAct.startActivity(intent);
            }
        });

        LinearLayout layout_delivery_problem = (LinearLayout) findViewById(R.id.layout_delivery_problem);
        layout_delivery_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mAct, Act_Message_Support.class);
                intent.putExtra("title", getString(R.string.problem_with_delivery));
                mAct.startActivity(intent);
            }
        });
        LinearLayout layout_order_price = (LinearLayout) findViewById(R.id.layout_order_price);

        layout_order_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mAct, Act_Message_Support.class);
                intent.putExtra("title", getString(R.string.order_price_problem));
                mAct.startActivity(intent);
            }
        });
        LinearLayout layout_app_problem = (LinearLayout) findViewById(R.id.layout_app_problem);
        layout_app_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mAct, Act_Message_Support.class);
                intent.putExtra("title", getString(R.string.app_problem));
                mAct.startActivity(intent);
            }
        });

        Button btn_call = (Button) findViewById(R.id.btn_call);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + "09123456789";
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);

//                Intent intent=new Intent(mAct,)
            }
        });
    }

}
