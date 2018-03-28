package com.freegeek.jzzh.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;

import com.freegeek.jzzh.R;
import com.freegeek.jzzh.util.Tools;

/**
 * @author Jack Fu <rtugeek@gmail.com>
 * @date 2017/09/20
 * @description
 * Virtual keyboard for inputting number, the radix decides <br/>
 * which buttons are available
 */
public class Keyboard extends FrameLayout {

    private Button[] mKeyboardBtn = new Button[20];
    private Animation mShow, mHide;
    private OnKeyboardListener mKeyboardListener;

    public Keyboard(@NonNull Context context) {
        super(context);
        addView(context);
    }

    public Keyboard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addView(context);
    }

    public Keyboard(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Keyboard(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        addView(context);
    }

    public void addView(Context context){

        mShow = AnimationUtils.loadAnimation(context, R.anim.bottom_in);
        mHide = AnimationUtils.loadAnimation(context, R.anim.bottom_out);

        mHide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mShow.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        View keyboardView = LayoutInflater.from(context).inflate(R.layout.keyboard,null);
        addView(keyboardView);

        //init input buttons
        for(int i = 0; i < mKeyboardBtn.length; i++){
            mKeyboardBtn[i] = keyboardView.findViewById(Tools.getIdByResourceName("input_btn_" + i));
            if(i == 18){
                mKeyboardBtn[i].setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startAnimation(mHide);
                    }
                });
            }else{
                mKeyboardBtn[i].setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;
                        if(mKeyboardListener !=null){
                            mKeyboardListener.onKeyboardClickListener(button,button.getText().toString());
                        }
                    }
                });

                mKeyboardBtn[i].setOnLongClickListener(new OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View view) {
                        Button button = (Button) view;
                        if(mKeyboardListener !=null){
                            mKeyboardListener.onKeyboardLongClickListener(button,button.getText().toString());
                        }
                        return false;
                    }

                });
            }
        }
    }

    public void show(){
        if(isShowing()){ return; }
        startAnimation(mShow);
    };

    public void hide(){
        startAnimation(mHide);
    }

    /**
     * set up the specified radix and update status of input buttons,
     * if the button's radix greater then the specified radix, it will be disabled
     * @param radix
     */
    public void setRadix(int radix){
        if(radix > 16){ radix = 16; }
        if(radix < 2){ radix = 2; }
        for(int i = 2;i < 16;i++){
            if(Integer.parseInt(mKeyboardBtn[i].getTag().toString()) >= radix){
                mKeyboardBtn[i].setTextColor(getResources().getColor(R.color.input_disenable_color));
                mKeyboardBtn[i].setEnabled(false);
            }else{
                mKeyboardBtn[i].setEnabled(true);
                mKeyboardBtn[i].setTextColor(getResources().getColor(R.color.colorTextPrimary));
            }
        }
    }

    public boolean isShowing(){
        return getVisibility() == VISIBLE;
    }


    public OnKeyboardListener getKeyboardListener() {
        return mKeyboardListener;
    }

    public void setOnKeyboardListener(OnKeyboardListener listener) {
        this.mKeyboardListener = listener;
    }

    public interface OnKeyboardListener {
        void onKeyboardClickListener(Button button, String value);
        void onKeyboardLongClickListener(Button button, String value);
    }
}
