package com.tnt.ibazaar;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.ServerResponse;
import network.webconnection.WebConnection;

public class Act_Sign_Up_Completion extends AppCompatActivity {


    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.et_family)
    EditText et_family;

    @BindView(R.id.et_reagent_code)
    EditText et_reagent_code;

    @BindView(R.id.et_password_login)
    EditText et_password;

    @BindView(R.id.chk_show_password_login)
    CheckBox chk_show_password;

    @BindView(R.id.txt_show_password_login)
    TextView txt_show_password;

    @BindView(R.id.btn_ok)
    Button btn_ok;

    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;

    private Act_Sign_Up_Completion mAct;

    private String mobile;

    private String name;
    private String family;
    private String reagent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__sign__up__completion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAct = this;
        ButterKnife.bind(mAct);
        try {
            mobile = getIntent().getStringExtra("mobile");
            name = getIntent().getStringExtra("name");
            family = getIntent().getStringExtra("family");
            reagent = getIntent().getStringExtra("reagent");

        } catch (Exception e) {
            finish();
        }
        et_name.setText(name);
        et_family.setText(family);
        et_reagent_code.setText(reagent);

        chk_show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                et_password.setSelection(et_password.getText().length());
            }
        });

        txt_show_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chk_show_password.setChecked(!chk_show_password.isChecked());
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (et_password.getText().toString().length() < 6) {
//                    Toast.makeText(mAct, R.string.pass_less_than_6_chars, Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (!et_reagent_code.getText().toString().isEmpty() &&
                        et_reagent_code.getText().toString().length() != 5) {
                    Toast.makeText(mAct, R.string.introducer_code_not_valid, Toast.LENGTH_SHORT).show();
                    return;
                }
//                showDialog();
                sign_up_completion(et_name.getText().toString(),
                        et_family.getText().toString(),
                        et_reagent_code.getText().toString(), "");
            }
        });


    }

    private void sign_up_completion(String name, String family,
                                    String reagent, String image) {
        Application.showProgressDialog(mAct);
        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("customerId", "" + Application.getCustomerId())
                .appendQueryParameter("customerName", name)
                .appendQueryParameter("customerFamily", family);
        if (reagent != null && !reagent.isEmpty()) {
            builder.appendQueryParameter("customerReagent", reagent);
        }
        if (image != null && !image.isEmpty()) {
            builder.appendQueryParameter("customerImage", image);
        }
        new WebConnection(new WebConnection.ConnectionResponse() {
            @Override
            public void connectionFinish(String result, String urlCanConnect) {
                Application.dismissProgressDialog();
                ServerResponse response = null;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    response = new ServerResponse();
                    response.setStatus(jsonObject.getInt("Status"));
                    response.setMessage(jsonObject.getString("MSG"));
                } catch (Exception e) {
                    Log.e("signUpCompletion error", " " + e.getMessage());
                }

                if (response == null) {
                    Toast.makeText(mAct, R.string.not_connected_check_internet, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getStatus() == 100) {
                    SharedPreferences prefs =
                            PreferenceManager.getDefaultSharedPreferences(mAct);
                    SharedPreferences.Editor mEditor = prefs.edit();
                    mEditor.putString(Application.getCustomerId() + "mobile", mobile);
                    if (!et_name.getText().toString().isEmpty())
                        mEditor.putString(Application.getCustomerId() + "name",
                                et_name.getText().toString());
                    mEditor.apply();
//                startActivity(new Intent(mAct, Act_Main.class));
                    finish();
                } else {
                    Toast.makeText(mAct, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).connect(builder, "editProfile", "POST",0);
    }

}
