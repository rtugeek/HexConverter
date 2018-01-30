package com.freegeek.jzzh;

import android.app.Application;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
/**
 * @author Jack Fu <rtugeek@gmail.com>
 * @date 2017/09/17
 * @description
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, getString(R.string.banner_ad_app_id));
    }


}
