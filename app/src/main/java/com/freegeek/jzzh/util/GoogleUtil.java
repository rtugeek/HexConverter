package com.freegeek.jzzh.util;

import com.google.android.gms.ads.AdRequest;


/*********************************************************************************
 Created by Android Studio.
 *Author:          Jack Fu
 *Version:         1.0
 *Date;            10/10/17 3:50 PM
 *Description:     
 **********************************************************************************/
public class GoogleUtil {

    public static AdRequest newAdRequest(){
        AdRequest adRequest = new AdRequest
                .Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("FDB0071175F992F0E734958D8ECE00B9")
                .build();
        return adRequest;
    }
}
