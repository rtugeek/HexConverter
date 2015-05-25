package com.example.jzzh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class DocFragment extends Fragment{
	
	public static DocFragment newInstance(){
		DocFragment fragment = new DocFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View docView = inflater.inflate(R.layout.fragment_doc, container, false);

		WebView wv = (WebView)docView.findViewById(R.id.webView1);
		wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		wv.getSettings().setSupportZoom(true);
		wv.getSettings().setBuiltInZoomControls(true);
		wv.loadUrl("file:///android_asset/help.htm");
										
		return docView;
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void onResume() {
		super.onResume();
	}


	
}
