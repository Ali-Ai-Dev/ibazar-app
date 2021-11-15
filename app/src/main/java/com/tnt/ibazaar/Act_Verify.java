package com.tnt.ibazaar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.rey.material.widget.Button;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.ServerResponse;
import network.NetworkConnection;
import network.webconnection.WebConnection;

public class Act_Verify extends AppCompatActivity {

    @BindView(R.id.et_code1)
    EditText et_code1;
    @BindView(R.id.et_code2)
    EditText et_code2;
    @BindView(R.id.et_code3)
    EditText et_code3;
    @BindView(R.id.et_code4)
    EditText et_code4;
    @BindView(R.id.et_code5)
    EditText et_code5;

    @BindView(R.id.btn_verify)
    Button btn_verify;

    @BindView(R.id.txt_resend)
    TextView txt_resend;

    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;

    @BindView(R.id.img_back)
    ImageView img_back;
    BroadcastReceiver verificationCodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final Bundle bundle = intent.getExtras();
            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    if (pdusObj != null) {
                        for (Object aPdusObj : pdusObj) {

                            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);

                            String senderNum = currentMessage.getDisplayOriginatingAddress();
                            String message = currentMessage.getDisplayMessageBody();

                            Log.e("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                            if (message.startsWith("به آی بازار خوش آمدید. کد تایید شما")) {

                                String[] split = message.split(":");
                                Log.e("p1", split[0] + " " + split[0].length());
                                Log.e("p2", split[1] + " " + split[1].trim().length());

                                if (split.length > 1) {
                                    Log.e("sta", "rt");
                                    String verification_code = split[1].trim();
                                    if (verification_code.trim().length() == 5) {
                                        et_code1.setText(verification_code.charAt(0) + "");
                                        et_code2.setText(verification_code.charAt(1) + "");
                                        et_code3.setText(verification_code.charAt(2) + "");
                                        et_code4.setText(verification_code.charAt(3) + "");
                                        et_code5.setText(verification_code.charAt(4) + "");
//                                        btn_verify.performClick();
                                    }
                                }
                            }

                        } // end for loop
                    }
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver " + e);
            }
        }
    };
    private Act_Verify mAct;
    private boolean resend = false;
    private String mobile = "";
    private String country_code = "";

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(verificationCodeReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
//        registerReceiver(mReceiver, new IntentFilter(BROADCAST_FILTER_Act_Verify));
//        registerReceiver(disconnect, new IntentFilter(BROADCAST_FILTER_All));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(verificationCodeReceiver);
//        unregisterReceiver(mReceiver);
//        unregisterReceiver(disconnect);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__verify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAct = this;

        ButterKnife.bind(mAct);

        try {

            mobile = getIntent().getExtras().getString("mobile", "");
            country_code = getIntent().getExtras().getString("country_code", "");
        } catch (Exception e) {
            Log.e("error", "" + e.getMessage());
            finish();
        } finally {
            if (mobile.isEmpty() || country_code.isEmpty())
                finish();
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final CountDownTimer timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txt_resend.setText(getString(R.string.resend_code) + "(" +
                        millisUntilFinished / 1000 + ")");
                resend = false;
            }

            @Override
            public void onFinish() {
                txt_resend.setText(getString(R.string.resend_code));
                resend = true;
            }
        };
        timer.start();

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!et_code1.getText().toString().isEmpty() &&
                        !et_code2.getText().toString().isEmpty() &&
                        !et_code3.getText().toString().isEmpty() &&
                        !et_code4.getText().toString().isEmpty() &&
                        !et_code5.getText().toString().isEmpty()) {
                    String code = et_code1.getText().toString() +
                            et_code2.getText().toString() +
                            et_code3.getText().toString() +
                            et_code4.getText().toString() +
                            et_code5.getText().toString();

                    checkVerificationCode(code, country_code, mobile);
                }
            }
        });

        txt_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resend) {
                    signUp(mobile, country_code);
                    resend = false;
                    timer.start();
                }
            }
        });

        et_code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_code1.getText().toString().length() == 1)
                    et_code2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 1)
                    et_code3.requestFocus();
                else if (s.toString().isEmpty()) {
                    et_code1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 1)
                    et_code4.requestFocus();
                else if (s.toString().isEmpty()) {
                    et_code2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 1)
                    et_code5.requestFocus();
                else if (s.toString().isEmpty()) {
                    et_code3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 1)
                    btn_verify.performClick();
                else if (s.toString().isEmpty()) {
                    et_code4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkVerificationCode(String code, String country_code, final String mobile) {
        final Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("countryCode", "" + country_code)
                .appendQueryParameter("mobileNumber", "" + mobile)
                .appendQueryParameter("verifyCode", "" + code);
        Application.showProgressDialog(mAct);

        new WebConnection(new WebConnection.ConnectionResponse() {

            @Override
            public void connectionFinish(String result, String urlCanConnect) {
                Application.dismissProgressDialog();
                ServerResponse response;

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    response = new ServerResponse();
                    response.setStatus(jsonObject.getInt("Status"));
                    response.setMessage(jsonObject.getString("MSG"));
                    String name = "";
                    String family = "";
                    String reagent = "";
                    String image = "";
                    if (response.getStatus() == 100) {
                        try {
                            JSONObject data = jsonObject.getJSONObject("Data");
                            Application.setCustomerId(data.getString("CustomerId"));
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Application.getContext());
                            SharedPreferences.Editor mEditor = prefs.edit();
                            String customer_id = data.getString("CustomerId");
                            mEditor.putString("customer_id", customer_id);

                            if (data.has("Name")) {
                                name = Application.NormalizeString(data.getString("Name"));
                            }
                            if (data.has("Family")) {
                                family = Application.NormalizeString(data.getString("Family"));
                            }
                            if (data.has("ReagentID")) {
                                reagent = Application.NormalizeString(data.getString("ReagentID"));
                            }
                            if (data.has("Image"))
                                image = Application.NormalizeString(data.getString("Image"));

                            Application.setUserAccountParentId(data.getInt("userAccountParentId"));
                            mEditor.putInt("userAccountParentId", Application.getUserAccountParentId());
                            mEditor.putString(customer_id + "name", "");

                            mEditor.apply();
                        } catch (Exception e) {
                            Log.e("VerificationCode error", " " + e.getMessage());
                        }

                        if (name.isEmpty() || family.isEmpty()) {
//                    Intent i = new Intent(mAct, Act_Sign_Up_Completion.class);
                            Intent i = new Intent(mAct, Act_Edit_Profile.class);
                            i.putExtra("mobile", mobile);
                            i.putExtra("name", name);
                            i.putExtra("family", family);
                            i.putExtra("reagent", reagent);
                            i.putExtra("image", image);
                            startActivity(i);
                        } else {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
                            SharedPreferences.Editor mEditor = prefs.edit();

                            mEditor.putString(Application.getCustomerId() + "mobile", mobile);
                            mEditor.putString(Application.getCustomerId() + "name", name);
                            mEditor.putString(Application.getCustomerId() + "family", family);
                            mEditor.putString(Application.getCustomerId() + "image", image);
                            mEditor.commit();
                        }
                        finish();
                    } else {
                        Toast.makeText(mAct, response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("VerificationCode error", " " + e.getMessage());
                }
            }
        }).connect(builder, "verifyCustomer", "POST", 0);
    }

    private void signUp(final String phone, final String countryCode) {

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
                } else if (response.getStatus() != 100) {
                    Toast.makeText(mAct, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).connect(builder, "signUpOrSignInCustomer", "POST", 0);
    }

}
