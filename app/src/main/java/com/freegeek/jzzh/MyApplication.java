package com.freegeek.jzzh;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

/*********************************************************************************
 Created by Android Studio.
 *Author:          Jack Fu
 *Version:         1.0
 *Date;            10/9/17 10:44 PM
 *Description:     
 **********************************************************************************/
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, getString(R.string.banner_ad_app_id));
    }


}
