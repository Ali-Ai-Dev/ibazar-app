package com.tnt.ibazaar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class Act_Intro extends AppIntro {

    private Act_Intro mAct;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act__intro);
        mAct = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
        try {

            Log.e("extras", String.valueOf(getIntent().getExtras() != null));
            for (String x : getIntent().getExtras().keySet()) {
                Log.e("x", x);
            }
            String message = getIntent().getExtras().getString("title");
            Log.e("message", message + " ");
            int code;
            if (message != null && message.equals("delivered")) {
                Intent i = new Intent(getBaseContext(), Act_Main.class);
                i.putExtra("track_delivery", false);
                i.putExtra("delivered", true);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(i);
                finish();
                return;
            }
        } catch (Exception e) {
            Log.e("errore", " " + e.getMessage());
        }
        if (prefs.getBoolean("show_intro", true)) {
            SharedPreferences.Editor mEditor = prefs.edit();
            mEditor.putBoolean("show_intro", false);
            mEditor.commit();
        } else {
            startActivity(new Intent(this, Act_Splash_Screen.class));
            finish();
            return;
        }
        addSlide(AppIntroFragment.newInstance("", getString(R.string.intro1), R.drawable.page1, Color.parseColor("#870B65")));
        addSlide(AppIntroFragment.newInstance("", getString(R.string.intro2), R.drawable.page2, Color.parseColor("#565656")));
        addSlide(AppIntroFragment.newInstance("", getString(R.string.intro3), R.drawable.page3, Color.parseColor("#3EAAD7")));
        addSlide(AppIntroFragment.newInstance("", getString(R.string.intro4), R.drawable.page4, Color.parseColor("#B47C1A")));
        addSlide(AppIntroFragment.newInstance("", getString(R.string.intro5), R.drawable.page5, Color.parseColor("#BB455E")));
        setSkipText("بلدم");

        setDoneText("بزن بریم");
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        startActivity(new Intent(this, Act_Splash_Screen.class));
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        startActivity(new Intent(this, Act_Splash_Screen.class));
        finish();
    }
}
