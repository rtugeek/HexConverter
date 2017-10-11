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

/*********************************************************************************
 Created by Android Studio.
 *Author:          Jack Fu                                              <br/>
 *Version:         1.0                                                  <br/>
 *Date;            9/20/17 8:39 PM                                      <br/>
 *Description:     Virtual keyboard for inputting number, the radix decides <br/>
 * which buttons are available
 **********************************************************************************/
public class Keyboard extends FrameLayout {
    private Button[] keyboardBtn = new Button[20];
    private Animation show,hide;
    private OnKeyboardListener mKeyboardListener;
    private View mKeyboardView;
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

        show = AnimationUtils.loadAnimation(context, R.anim.bottom_in);
        hide = AnimationUtils.loadAnimation(context, R.anim.bottom_out);

        hide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mKeyboardView.setVisibility(GONE);
                setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mKeyboardView = LayoutInflater.from(context).inflate(R.layout.keyboard,null);
        addView(mKeyboardView);

        //init input buttons
        for(int i = 0;i < keyboardBtn.length; i++){
            keyboardBtn[i] = mKeyboardView.findViewById(Tools.getIdByResourceName("input_btn_" + i));
            if(i == 18){
                keyboardBtn[i].setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startAnimation(hide);
                    }
                });
            }else{
                keyboardBtn[i].setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;
                        if(mKeyboardListener !=null)
                            mKeyboardListener.onKeyboardClickListener(button,button.getText().toString());
                    }
                });

                keyboardBtn[i].setOnLongClickListener(new OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View view) {
                        Button button = (Button) view;
                        if(mKeyboardListener !=null)
                            mKeyboardListener.onKeyboardLongClickListener(button,button.getText().toString());
                        return false;
                    }

                });
            }
        }
    }

    public void show(){
        if(isShowing()) return;
        setVisibility(VISIBLE);
        mKeyboardView.setVisibility(VISIBLE);
        startAnimation(show);
    };

    public void hide(){
        startAnimation(hide);
    }

    /**
     * set up the specified radix and update status of input buttons,
     * if the button's radix greater then the specified radix, it will be disabled
     * @param radix
     */
    public void setRadix(int radix){
        if(radix > 16) radix = 16;
        if(radix < 2) radix = 2;
        for(int i = 2;i < 16;i++){
            if(Integer.parseInt(keyboardBtn[i].getTag().toString()) >= radix){
                keyboardBtn[i].setTextColor(getResources().getColor(R.color.input_disenable_color));
                keyboardBtn[i].setEnabled(false);
            }else{
                keyboardBtn[i].setEnabled(true);
                keyboardBtn[i].setTextColor(getResources().getColor(R.color.colorTextPrimary));
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
