package com.bruynhuis.zigzagdash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bruynhuis.galago.android.AbstractGameActivity;

public class MainActivity extends AbstractGameActivity {

    @Override
    protected void preload() {
        APP_PATH = "za.co.bruynhuis.cuberush.MainApplication";
        PLAYSTORE_URL = "https://play.google.com/store/apps/details?id=com.bruynhuis.zigzagdash";
        MOREAPPS_URL = "https://play.google.com/store/apps/developer?id=bruynhuis";

//        useSensor = true;

//        useMidiMusicTracks = true;
//
//        usePlayServices = true;
//
//        useAdmobInterstitials = true;
//        ADMOB_INTERSTITIALS_ID = "ca-app-pub-9553163517721646/5081836612";
//        useAdmob = true;
//        ADMOB_ID = "ca-app-pub-9553163517721646/6558569810";
//
//        useAnalytics = true;
//        ANALYTICS_TRACKER_ID = "UA-64908415-11";

//        splashPicID = R.drawable.splash;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void postLoad() {
    }

}
