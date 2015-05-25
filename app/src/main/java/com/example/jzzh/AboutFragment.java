package com.example.jzzh;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class AboutFragment extends Fragment{

	
	public static AboutFragment newInstance(){
		AboutFragment fragment = new AboutFragment();
		
		return fragment;
	}


	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View aboutFragment = inflater.inflate(R.layout.fragmen_about, container, false);
        ImageView rate =(ImageView)aboutFragment.findViewById(R.id.img_rate);
        aboutFragment.findViewById(R.id.txt_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/rtugeek/Hex-converter"; // web address
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                ComponentName cn = new ComponentName("com.qihoo.appstore",
                        "com.qihoo.appstore.activities.SearchDistributionActivity");
                intent.setComponent(cn);
                intent.setData(Uri.parse("market://details?id=com.example.jzzh"));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), R.string.no360, Toast.LENGTH_SHORT).show();
                }
            }
        });


		return aboutFragment;
	}

	
}
