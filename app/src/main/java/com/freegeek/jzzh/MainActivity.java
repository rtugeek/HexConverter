package com.freegeek.jzzh;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.freegeek.jzzh.view.Keyboard;
import com.freegeek.jzzh.view.RadixEditText;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AlertDialog radixPickerDialog;
    private TextView mTvLabelMisc;
    private RadixEditText et_misc;
    private RadixEditText mCurrentEditText;
    private Keyboard mKeyboard;
    private ClipboardManager clipboardManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initView() {
        mTvLabelMisc = (TextView) findViewById(R.id.tv_label_misc);
        mCurrentEditText = (RadixEditText) findViewById(R.id.et_misc);
        mKeyboard = (Keyboard) findViewById(R.id.keyboard);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        final String radix[] = {"3", "4", "5", "6", "7", "8", "9", "11", "12", "13", "14", "15"};
        radixPickerDialog = new AlertDialog.Builder(this).setItems(radix, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mTvLabelMisc.setText(radix[i]);
                mCurrentEditText.setRadix(Integer.parseInt(radix[i]));
            }
        }).setNegativeButton(R.string.cancel, null).create();

        mTvLabelMisc.setOnClickListener(this);
        mKeyboard.setVisibility(View.GONE);
        mKeyboard.setOnKeyboardListener(new Keyboard.OnKeyboardListener() {
            @Override
            public void onKeyboardClickListener(Button button, String value) {
                if(value.equalsIgnoreCase("DEL")){
                    String data =  mCurrentEditText.getText().toString();
                    if(data.trim().length() != 0){
                        mCurrentEditText.setText(data.substring(0, data.length() - 1));
                        mCurrentEditText.setSelection(mCurrentEditText.getText().length());
                    }
                }else if(value.toUpperCase().equals("COPY")){
                    clipboardManager.setText(mCurrentEditText.getText().toString().replaceAll(" ", ""));
                    Toast.makeText(MainActivity.this, getString(R.string.done), Toast.LENGTH_SHORT).show();
                }else if(value.equals(".")){
                    if(!mCurrentEditText.getText().toString().contains(".")) mCurrentEditText.append(value);
                }else{
                    mCurrentEditText.append(value);
                }

            }

            @Override
            public void onKeyboardLongClickListener(Button button, String value) {
                if(value.equalsIgnoreCase("DEL")){
                    mCurrentEditText.setText("");
                }
            }
        });

        for (RadixEditText radixEditText : RadixEditText.getRadixEditTexts()) {
            radixEditText.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_UP:
                            RadixEditText radixEditText = (RadixEditText) v;
                            mKeyboard.setRadix(radixEditText.getRadix());
                            mKeyboard.show();
                            mCurrentEditText = radixEditText;
                            updateColor(radixEditText.getRadix());
                            break;
                    }
                    return false;
                }
            });
        }

    }

    /**
     * highlight focused edit text
     * @param radix
     */
    private void updateColor(int radix){
        for (RadixEditText radixEditText : RadixEditText.getRadixEditTexts()) {
            if(radixEditText.getRadix() == 10) continue;
            if(radixEditText.getRadix() == radix){
                int color = getResources().getColor(R.color.colorPrimary);
                radixEditText.setTextColor(color);
                radixEditText.setHintTextColor(color);
                continue;
            }
            radixEditText.setTextColor(0xFF616161);
            radixEditText.setHintTextColor(0xFFD4D4D4);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_label_misc:
                radixPickerDialog.show();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_about:
                startActivity(new Intent(this,AboutActivity.class));
                break;
            case R.id.menu_doc:
                startActivity(new Intent(this,DocActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == 4 && mKeyboard.isShowing()){
            mKeyboard.hide();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
