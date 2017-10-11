package com.freegeek.jzzh;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.freegeek.jzzh.util.GoogleUtil;
import com.google.android.gms.ads.AdView;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AdView  adView= (AdView) findViewById(R.id.adView);
        adView.loadAd(GoogleUtil.newAdRequest());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linear_code:
                String url = "https://github.com/rtugeek/HexConverter";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;
            case R.id.linear_feedback:
                Intent data=new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:rtugeek@gmail.com"));
                data.putExtra(Intent.EXTRA_SUBJECT, "进制转换反馈");
                data.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(data);
                break;
        }
    }
}
