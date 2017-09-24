package com.freegeek.jzzh.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.freegeek.jzzh.R;
import com.freegeek.jzzh.Tools;

import java.util.ArrayList;
import java.util.List;

/*********************************************************************************
 Created by Android Studio.
 *Author:          Jack Fu
 *Version:         1.0
 *Date;            9/19/17 3:20 PM
 *Description:     
 **********************************************************************************/
public class RadixEditText extends AppCompatEditText{
    private int mRadix;
    private static ClipboardManager mClipboardManager;
    private static List<RadixEditText> mRadixEditTexts = new ArrayList<>();
    /**
     * Save the RadixEditText which is focusing;
     */
    private static RadixEditText mFocusedEditText;
    public RadixEditText(Context context) {
        super(context);
        applyStyle(context,null,0,0);
    }

    public RadixEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyStyle(context,attrs,0,0);
    }

    public RadixEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyStyle(context,attrs,defStyleAttr,0);
    }


    public void applyStyle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadixEditText,defStyleAttr,defStyleRes);
        mRadix = typedArray.getInteger(R.styleable.RadixEditText_radix,10);
        mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        mRadixEditTexts.add(this);
        setInputType(InputType.TYPE_NULL);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mClipboardManager.hasText()){
                    final String clipboardString= mClipboardManager.getText().toString().replaceAll(" ", "");
                    if(TextUtils.isEmpty(clipboardString)){
                        Toast.makeText(getContext(),
                                getContext().getString(R.string.clipboard_3)+clipboardString,
                                Toast.LENGTH_LONG)
                                .show();
                        return true;
                    }
                    if(Tools.checkData(clipboardString,getRadix())){
                        setText(clipboardString);
                    }else{
                        Toast.makeText(getContext(),
                                getContext().getString(R.string.clipboard_1)+clipboardString,
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }else{
                    Toast.makeText(getContext(),
                            getContext().getString(R.string.clipboard_3), Toast.LENGTH_SHORT)
                            .show();
                }
                return true;
            }
        });

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = charSequence.toString();
                //Prevent the infinite loop.
                if(isFocused()){
                    //If input empty text, clear all EditText
                    if(TextUtils.isEmpty(input)){
                        for (RadixEditText mRadixEditText : mRadixEditTexts){
                            if(mRadixEditText.getRadix() != getRadix()) mRadixEditText.setText("");
                        }
                        return;
                    }
                    if (!Tools.checkData(input, getRadix())) {
                        Toast.makeText(getContext(), getContext().getString(R.string.clipboard_2) + charSequence.toString(), Toast.LENGTH_LONG).show();
                        setText("");
                        return;
                    }

                    for (RadixEditText mRadixEditText : mRadixEditTexts){
                        if(mRadixEditText.getRadix() != getRadix()){
                            String result = "";
                            String[] array = input.split("\\.");
                            int toRadix = mRadixEditText.getRadix();
                            int fromRadix = getRadix();
                            //if input data only contains integer
                            if(input.indexOf(".") == -1 || array.length == 1){
                                result= Tools.integerConverter(array[0], toRadix, fromRadix);
                            }else{
                                result= Tools.integerConverter(array[0], toRadix, fromRadix)
                                        +"."+ Tools.decimalsConverter(array[1], toRadix, fromRadix);
                            }
                            mRadixEditText.setText(formatData(result));
                        }
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mRadixEditTexts.contains(this)) mRadixEditTexts.remove(this);
    }

    /**
     * format data, insert whitespace into the given data every four character<br/>
     * eg.  111011001 -> 1110 1100 1
     * @param s
     * @return
     */
    private String formatData(String s){
        int length = s.length();
        StringBuffer stringBuffer = new StringBuffer(s);
        int i = 0;
        while (length > 4){
            length -= 4;
            stringBuffer.insert((i + 1) * 4 + i," ");
            i++;
        }
        return stringBuffer.toString();
    }

    public int getRadix() {
        return mRadix;
    }

    public void setRadix(int radix) {
        this.mRadix = radix;
    }

    public static List<RadixEditText> getRadixEditTexts() {
        return mRadixEditTexts;
    }

    public static RadixEditText getFocusedEditText() {
        return mFocusedEditText;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                mFocusedEditText = this;
                //remove all whitespace
                setText(getText().toString().replaceAll(" ",""));
                setSelection(getText().length());
                break;
        }
        return super.onTouchEvent(event);
    }
}
