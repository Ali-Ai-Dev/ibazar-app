package com.tnt.ibazaar;

import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Act_Change_Pass extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.chk_show_password_login)
    CheckBox chk_show_password_login;

    @BindView(R.id.txt_show_password_login)
    TextView txt_show_password_login;

    @BindView(R.id.btn_change_pass)
    Button btn_change_pass;

    private Act_Change_Pass mAct;

    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__change__pass);
        mAct = this;
        ButterKnife.bind(mAct);


    }

}
