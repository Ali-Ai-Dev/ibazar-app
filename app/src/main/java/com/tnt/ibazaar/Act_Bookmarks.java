package com.tnt.ibazaar;

import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import adapters.Adapter_Pager_Bookmarks;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Act_Bookmarks extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager pager;

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.viewpagertab)
    SmartTabLayout viewPagerTab;

    private Act_Bookmarks mAct;

    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act__bookmarks);
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

        Adapter_Pager_Bookmarks adapter = new Adapter_Pager_Bookmarks(
                getSupportFragmentManager());
        pager.setAdapter(adapter);
        viewPagerTab.setViewPager(pager);
        pager.setCurrentItem(1);
    }

}
