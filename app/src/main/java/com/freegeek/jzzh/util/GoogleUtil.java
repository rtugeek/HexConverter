package com.freegeek.jzzh.util;

import com.google.android.gms.ads.AdRequest;

/**
 * @author Jack Fu <rtugeek@gmail.com>
 * @date 2017/10/10
 * @description
 */
public class GoogleUtil {

    public static AdRequest newAdRequest(){
        AdRequest adRequest = new AdRequest
                .Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("62D82C02CB4488300B42CADE2393F10C")
//                .addTestDevice("FDB0071175F992F0E734958D8ECE00B9")
                .build();
        return adRequest;
    }
}
