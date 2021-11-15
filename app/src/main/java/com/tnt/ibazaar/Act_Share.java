package com.tnt.ibazaar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Act_Share extends AppCompatActivity {

    @BindView(R.id.txt_toolbar_title)
    TextView txt_toolbar_title;
    private Act_Share mAct;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),
                new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__share);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAct = this;
        ButterKnife.bind(mAct);
        SharedPreferences
                prefs = PreferenceManager.getDefaultSharedPreferences(mAct);

        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        TextView txt_gift_message = (TextView) findViewById(R.id.txt_gift_message);
        String x = String.format(Locale.ENGLISH, "با اشتراک کد زیر با دوستان و آشنایان خود، مبلغ " +
                "%,d تومان اعتبار از ما هدیه بگیرید.", prefs.getInt("intro_amount", 0));

        txt_gift_message.setText(x);


        TextView txt_introduction_code = (TextView) findViewById(R.id.introduction_code);

        String intro_code = prefs.getString("introductionCode", "");
        intro_code = getString(R.string.your_intro_code) + " " + intro_code;
        txt_introduction_code.setText(intro_code.toUpperCase(Locale.ENGLISH));

        ImageView img_telegram = (ImageView) findViewById(R.id.img_telegram);
        final String message = getString(R.string.share_comments) + "\n" + intro_code +
                "\nwww.app.url.com";
        img_telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMessageTelegram(message);
            }
        });

        ImageView img_email = (ImageView) findViewById(R.id.img_email);
        img_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailto = "mailto:" +
                        "?" + "" +
                        "subject=" + Uri.encode(getString(R.string.app_name)) +
                        "&body=" + Uri.encode(message);

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));

                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    Log.e("error", " " + e.getMessage());
                }
            }
        });

        ImageView img_sms = (ImageView) findViewById(R.id.img_sms);
        img_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                sendIntent.putExtra("sms_body", message);
                startActivity(sendIntent);
            }
        });

        ImageView img_share = (ImageView) findViewById(R.id.img_share);
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
//                intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(intent, "اشتراک"));
            }
        });

    }

    void intentMessageTelegram(String msg) {
        final String telegram = "org.telegram.messenger";
        final String telegramX = "org.thunderdog.challegram";
        final String mobogram = "com.hanista.mobogram";
        final boolean isTelegramInstalled = isAppAvailable(telegram);
        final boolean isTelegramXInstalled = isAppAvailable(telegramX);
        final boolean isMobogramInstalled = isAppAvailable(mobogram);
        if (isTelegramXInstalled) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            myIntent.setPackage(telegram);
            myIntent.putExtra(Intent.EXTRA_TEXT, msg);//
            startActivity(Intent.createChooser(myIntent, "Share with"));
        } else if (isTelegramInstalled) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            myIntent.setPackage(telegram);
            myIntent.putExtra(Intent.EXTRA_TEXT, msg);//
            startActivity(Intent.createChooser(myIntent, "Share with"));
        } else if (isMobogramInstalled) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            myIntent.setPackage(mobogram);
            myIntent.putExtra(Intent.EXTRA_TEXT, msg);//
            startActivity(Intent.createChooser(myIntent, "Share with"));
        } else {
            Toast.makeText(mAct, R.string.telegram_not_installed, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isAppAvailable(String appName) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}
