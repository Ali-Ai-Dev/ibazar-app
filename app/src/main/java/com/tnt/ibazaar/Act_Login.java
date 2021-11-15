package com.tnt.ibazaar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class Act_Login extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.et_phone_number_login)
    EditText et_phone_number_login;

    @BindView(R.id.et_password_login)
    EditText et_password_login;

    @BindView(R.id.chk_show_password_login)
    CheckBox chk_show_password_login;

    @BindView(R.id.txt_show_password_login)
    TextView txt_show_password_login;

    @BindView(R.id.btn_register_login)
    Button btn_register_login;

    @BindView(R.id.txt_forgot_pass)
    TextView txt_forgot_pass;

    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;

    private Act_Login mAct;

    private Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__login);
        mAct = this;
        ButterKnife.bind(mAct);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Drawable img_phone =
                new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_call)
                        .color(Color.GRAY)
                        .sizeDp(20);
        et_phone_number_login.setCompoundDrawablesWithIntrinsicBounds(null, null, img_phone, null);

        chk_show_password_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    et_password_login.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    et_password_login.setTransformationMethod(PasswordTransformationMethod.getInstance());
                et_password_login.setSelection(et_password_login.getText().length());
            }
        });

        txt_show_password_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chk_show_password_login.setChecked(!chk_show_password_login.isChecked());
            }
        });

        txt_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_phone_number_login.getText().toString().isEmpty()) {
                    Toast.makeText(mAct, R.string.phone_not_entered, Toast.LENGTH_SHORT).show();
                    return;
                }
//                new SendCode(et_phone.getText().toString()).execute();
            }
        });

        dialog = new Dialog(mAct);
        View view = getLayoutInflater().inflate(R.layout.dialog_please_wait, null);
        dialog.setContentView(view);

        btn_register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login(user , pass);
//                TCPSocketInterface.login(mAct, et_phone_number_login.getText().toString(),
//                        et_password_login.getText().toString());
            }
        });
    }

//    private boolean containsNonDigitChar(String input) {
//        for (char c : input.toCharArray()) {
//            if (!Character.isDigit(c)) {
//                return true;
//            }
//        }
//        return false;
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(mAct, Act_Splash_Screen.class);
        intent.putExtra("exit", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}

