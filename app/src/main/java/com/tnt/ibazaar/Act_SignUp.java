package com.tnt.ibazaar;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.rey.material.app.Dialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import models.ServerResponse;
import network.NetworkConnection;
import network.webconnection.WebConnection;
import views.countrypicker.Country;
import views.countrypicker.CountryPicker;
import views.countrypicker.CountryPickerListener;

public class Act_SignUp extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.txt_country_name)
    TextView txt_country_name;

    @BindView(R.id.img_country_flag)
    ImageView img_country_flag;

    @BindView(R.id.et_country_code)
    EditText et_country_code;

    @BindView(R.id.et_phone)
    EditText et_phone;

    @BindView(R.id.btn_sign_up)
    Button btn_sign_up;

    @BindView(R.id.layout_choose_country)
    LinearLayout layout_choose_country;

    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;

    private Act_SignUp mAct;

    private CountryPicker picker;
    private TextWatcher country_code_watcher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAct = this;
        ButterKnife.bind(this);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        country_code_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().isEmpty())
                    return;
                Country country = new Country();
                country.setDialCode(s.toString());
                int index = -1;
                //= Arrays.search(Country.COUNTRIES, country, new Country.DialCodeComparator())
                for (int i = 0; i < Country.COUNTRIES.length; i++) {
                    if (Country.COUNTRIES[i].getDialCode().equals(s.toString())) {
                        index = i;
                        break;
                    }
                }
                Log.e("index", "" + index);
                if (index >= 0) {
                    country = Country.COUNTRIES[index];
                    txt_country_name.setText(country.getCode() + " - " + country.getName());
                    img_country_flag.setImageResource(country.getFlag());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        et_country_code.addTextChangedListener(country_code_watcher);

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().startsWith("0")) {
                    Toast.makeText(mAct, R.string.enter_phone_without_zero, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        picker =
                CountryPicker.newInstance(getResources().getString(R.string.ChooseCountry));  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                txt_country_name.setText(code + " - " + name);
                et_country_code.removeTextChangedListener(country_code_watcher);
                et_country_code.setText(dialCode);
                et_country_code.addTextChangedListener(country_code_watcher);
                img_country_flag.setImageResource(flagDrawableResID);
                picker.dismiss();
                et_phone.requestFocus();
            }
        });
        layout_choose_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });

        Country country = new Country();
        country.setCode(Application.getCountryCode());
        int index = Arrays.binarySearch(Country.COUNTRIES, country, new Country.ISOCodeComparator());
        if (index >= 0) {
            country = Country.COUNTRIES[index];
            txt_country_name.setText(country.getCode() + " - " + country.getName());
            et_country_code.removeTextChangedListener(country_code_watcher);
            et_country_code.setText(country.getDialCode());
            et_country_code.addTextChangedListener(country_code_watcher);
            img_country_flag.setImageResource(country.getFlag());
        }

        et_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btn_sign_up.performClick();
                }
                return false;
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Phone = et_phone.getText().toString().trim();
                final String Prefix = et_country_code.getText().toString().trim();
                if (TextUtils.isEmpty(Phone)) {
                    Toast.makeText(mAct, R.string.enter_phone_number, Toast.LENGTH_LONG).show();
                } else {
                    final SweetAlertDialog wDialog = new SweetAlertDialog(mAct, SweetAlertDialog.WARNING_TYPE);
                    wDialog.setTitleText("+" + Prefix + Phone)
                            .setContentText(getString(R.string.phone_correct))
                            .setConfirmText(" " + getString(R.string.yes) + " ")
                            .setCancelText(getString(R.string.edit))
                            .setConfirmClickListener(
                                    new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            wDialog.dismiss();
                                            //       new GetVerificationCode(Prefix, Phone).execute();
                                            if (!checkPermissions())
                                                requestPermissions(mAct, 96);
                                            else {
                                                signUp(Phone, et_country_code.getText().toString());
                                            }
//                                          startActivity(new Intent(mAct, Act_Verify.class));
                                        }
                                    })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    wDialog.dismiss();
                                    et_phone.requestFocus();
                                }
                            }).show();
                }
            }
        });
        et_phone.setCompoundDrawablesWithIntrinsicBounds(
                null, null, new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_phone)
                        .color(Color.GRAY)
                        .sizeDp(20), null);
        et_phone.requestFocus();
    }

    private void signUp(String phone, String countryCode) {

        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("countryCode", countryCode)
                .appendQueryParameter("mobileNumber", phone);
        Application.showProgressDialog(mAct);
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
                    Log.e("signUp error", " " + e.getMessage());
                }
                if (response == null) {
                    Toast.makeText(mAct, R.string.not_connected_check_internet, Toast.LENGTH_SHORT).show();
                } else if (response.getStatus() == 100) {
                    Intent i = new Intent(mAct, Act_Verify.class);
                    i.putExtra("mobile", et_phone.getText().toString());
                    i.putExtra("country_code", et_country_code.getText().toString());
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(mAct, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).connect(builder, "signUpOrSignInCustomer", "POST", 0);
    }

    private void requestPermissions(Activity mAct, int reqCode) {
        if (Build.VERSION.SDK_INT < 23)
            return;
        String[] permissions = new String[]{
                Manifest.permission.RECEIVE_SMS
        };

//        String[] neededPermissions=new String[];
        ArrayList<String> neededPermissions = new ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(mAct,
                    perm)
                    != PackageManager.PERMISSION_GRANTED)
                neededPermissions.add(perm);
        }
        if (neededPermissions.size() == 0)
            return;

        String[] strings = new String[neededPermissions.size()];
        for (int i = 0; i < neededPermissions.size(); i++) {
            strings[i] = neededPermissions.get(i);
        }
        ActivityCompat.requestPermissions(mAct, strings, reqCode);
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        String[] permissions = new String[]{
                Manifest.permission.RECEIVE_SMS
        };

//        String[] neededPermissions=new String[];
        ArrayList<String> neededPermissions = new ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(mAct,
                    perm)
                    != PackageManager.PERMISSION_GRANTED)
                neededPermissions.add(perm);
        }
        if (neededPermissions.size() == 0)
            return true;
//        else {
//            for (String s : neededPermissions) {
//                Log.e("neededPermissions", "" + s);
//            }
//        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        signUp(et_phone.getText().toString().trim()
                , et_country_code.getText().toString());
    }

}
