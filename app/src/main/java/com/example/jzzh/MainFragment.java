package com.example.jzzh;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Gallery.LayoutParams;
import android.widget.ScrollView;
import android.widget.Toast;

import com.rey.material.widget.Button;

public class MainFragment extends Fragment{
	private EditText[] editTexts = new EditText[5];
	private EditText focusingEditText;
	private int nowFocusHex = 10;
	private Button btnChooseHex,copy,paste;
	private Button[] hexBtn = new Button[12], keyboardBtn = new Button[19];
	private PopupWindow operateWindow, hexWindow;
	private MyTextWatcher textWatcher =new MyTextWatcher();
	private ClipboardManager clipboardManager;
	private ImageView[] clear = new ImageView[5];
	private Animation show,hide;
	private RelativeLayout inputView;
	private boolean isShow = false;
	private boolean isInit = true;
	private int rlHeight;
	private ScrollView scrollView;
	public static MainFragment newInstance(){
		MainFragment fragment = new MainFragment();
		
		return fragment;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.fragment_main, container, false);
		show = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_in);
		hide = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_out);
		show.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				for(int i = 0;i < 18; i++){
					keyboardBtn[i].setClickable(true);
				}
				isShow = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1,-2);
				layoutParams.addRule(RelativeLayout.ABOVE, inputView.getId());
				scrollView.setLayoutParams(layoutParams);
			}
		});


		hide.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				isShow = false;
				for (int i = 0; i < 18; i++) {
					keyboardBtn[i].setClickable(false);
				}
				if (!isInit) {
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
					layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
					layoutParams.height = rlHeight;
					scrollView.setLayoutParams(layoutParams);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (isInit) {
					rlHeight = scrollView.getHeight();
					isInit = false;
				}
			}
		});
		clipboardManager =(ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
		LayoutInflater li = getActivity().getLayoutInflater();

        //init operate window
		View operateView = li.inflate(R.layout.operate_window,null);
		copy = (Button)operateView.findViewById(R.id.btn_copy);
		paste = (Button)operateView.findViewById(R.id.btn_paste);
		operateWindow = new PopupWindow(operateView,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		operateWindow.setBackgroundDrawable(new ColorDrawable());
		operateWindow.setOutsideTouchable(false);
		operateWindow.setAnimationStyle(R.style.scalePopup);
		operateWindow.update();
        //init hex window
        final View hexView = li.inflate(R.layout.hex_window, null);
		hexWindow = new PopupWindow(hexView,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		hexWindow.setBackgroundDrawable(new ColorDrawable());
		hexWindow.setOutsideTouchable(false);
		hexWindow.setAnimationStyle(R.style.scalePopup);
		hexWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                editTexts[3].requestFocus();
                editTexts[4].requestFocus();
            }
        });

		scrollView = (ScrollView)mainView.findViewById(R.id.rl);
		btnChooseHex = (Button)mainView.findViewById(R.id.btn_more_hex);
		scrollView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isShow) inputView.startAnimation(hide);
			}
		});
        inputView =(RelativeLayout)mainView.findViewById(R.id.inputx);
        //init input buttons
		for(int i = 0;i < keyboardBtn.length; i++){
			keyboardBtn[i] = (Button)inputView.findViewById(Tools.getIdByResourceName(getActivity(), "input_btn_" + i));
			if(i == 17){
				keyboardBtn[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(focusingEditText != null){
							String ss =  focusingEditText.getText().toString();
							if(ss.trim().length() != 0){
								focusingEditText.setText(ss.substring(0, ss.length() - 1));
								focusingEditText.setSelection(focusingEditText.getText().length());
							}
						}
					}
				});
				keyboardBtn[i].setOnLongClickListener(new View.OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {

						focusingEditText.setText("");
						return false;
					}
				});
			}else if(i == 18){
				keyboardBtn[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						inputView.startAnimation(hide);
					}
				});
			}else{
				keyboardBtn[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Button bb = (Button)v;
						focusingEditText.append(bb.getText().toString());
					}
				});
			}
		}


        //init edit texts
		for(int i = 0;i < 5;i++){
			editTexts[i] = (EditText)mainView.findViewById(Tools.getIdByResourceName(getActivity(), "EditText" + i));
			editTexts[i].setOnFocusChangeListener(new focusL());
			editTexts[i].setInputType(InputType.TYPE_NULL);
			editTexts[i].setHorizontallyScrolling(false);
			editTexts[i].setSingleLine(false);
			editTexts[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if(!isShow){
						inputView.clearAnimation();
						inputView.startAnimation(show);
					}
				}
			});

			editTexts[i].setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {

					final EditText etx=(EditText)v;
					etx.requestFocus();
					operateWindow.showAsDropDown(v);
					copy.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							clipboardManager.setText(etx.getText().toString().replaceAll(" ", ""));
							Toast.makeText(getActivity(), getString(R.string.done), Toast.LENGTH_SHORT).show();
						}
					});
					paste.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if(clipboardManager.hasText()){
								final String s= clipboardManager.getText().toString().replaceAll(" ", "");
								String sss;
								String ss="0123456789ABCDEF.";
								for(int i = 0;i<s.length();i++){
									sss = s.substring(i,i+1);
									if(ss.indexOf(sss)!=-1 && s.split("\\.").length < 3){
										if(ss.indexOf(sss) >= Integer.parseInt(etx.getTag().toString()) && ss.indexOf(sss) !=16){
											Toast.makeText(getActivity(), getString(R.string.clipbaord1)+s, Toast.LENGTH_LONG).show();
											break;
										}else{
											if(i == s.length() - 1){
												etx.setText(s);
											}
										}
									}else{
										Toast.makeText(getActivity(), getString(R.string.clipbaord2)+s, Toast.LENGTH_LONG).show();
										break ;
									}
								}
							}else{
								Toast.makeText(getActivity(), getString(R.string.clipbaord3), Toast.LENGTH_SHORT).show();
							}

						}
					});
					return false;
				}
			});
		}


        //init clear buttons
		for(int i = 0;i < 5; i++){
			clear[i] = (ImageView)mainView.findViewById(Tools.getIdByResourceName(getActivity(), "clear" + i));
			clear[i].setVisibility(View.GONE);
			clear[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					editTexts[Integer.parseInt(v.getTag().toString())].setText("");
				}
			});
		}
        //init hex buttons
		for(int i = 0;i < 12; i++){
			hexBtn[i] = (Button)hexView.findViewById(Tools.getIdByResourceName(getActivity(), "hex_btn_" + (i + 1)));
			if(i == 11){
				hexBtn[i].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						hexWindow.dismiss();
					}
				});
			}else{
				hexBtn[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Button x = (Button) v;
						btnChooseHex.setText(getActivity().getString(R.string.down) + x.getText().toString());
						editTexts[4].setText("");
						editTexts[4].setTag(x.getText().toString());
					}
				});
			}

		}

		btnChooseHex.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                hexWindow.showAsDropDown(btnChooseHex);
            }
        });

		nowFocusHex = 10;
		inputView.startAnimation(hide);
		return mainView;
	}



	private class focusL implements View.OnFocusChangeListener {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {

			focusingEditText = (EditText)v;
			if(hasFocus) {
				nowFocusHex = Integer.parseInt(v.getTag().toString());
				focusingEditText.addTextChangedListener(textWatcher);
				hasPoint(focusingEditText.getText().toString());
			}else{
				focusingEditText.removeTextChangedListener(textWatcher);
			}

			for(int i = 0;i < 5;i++){
				if(editTexts[i].isFocused()){
					clear[i].setVisibility(View.VISIBLE);
				}else{
					clear[i].setVisibility(View.GONE);
				}
			}
			for(int i = 2;i < 16;i++){
				if(Integer.parseInt(keyboardBtn[i].getTag().toString()) >= Integer.parseInt(focusingEditText.getTag().toString())){
					keyboardBtn[i].setTextColor(getResources().getColor(R.color.input_disenable_color));
					keyboardBtn[i].setEnabled(false);
				}else{
					keyboardBtn[i].setEnabled(true);
					keyboardBtn[i].setTextColor(getResources().getColor(R.color.input_enable_color));
				}
			}
			if(!isShow){
				inputView.clearAnimation();
				inputView.startAnimation(show);
			}
		}

	}


	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}


	private class MyTextWatcher implements TextWatcher {
		int hex1;
		String[] ss;
		String result;
		@Override
		public void afterTextChanged(Editable s) {

			if(s.toString().trim().length() != 0){
				for(int i = 0;i < 5;i++){
					hex1 =Integer.parseInt(editTexts[i].getTag().toString());
					if(hex1 != nowFocusHex){
						if(s.toString().indexOf(".")==-1){
							result= Tools.integerConverter(s.toString(), hex1, nowFocusHex);
						}else{
							ss = s.toString().split("\\.");
							if(ss.length ==1 ){ // Determine whether there is a decimal point
								result= Tools.integerConverter(ss[0], hex1, nowFocusHex);
							}else{
								result= Tools.integerConverter(ss[0], hex1, nowFocusHex)+"."+ Tools.decimalConverter(ss[1], nowFocusHex, hex1);
							}
						}
						if(result.length()>16){
							editTexts[i].setMinLines(result.length() / 18 + 1);
						}else{
							editTexts[i].setMinLines(1);
						}
						editTexts[i].setText(formatData(result));
					}

				}

			}else{
				for(int i = 0;i < 5;i++){
					if(Integer.parseInt(editTexts[i].getTag().toString()) != nowFocusHex){
						editTexts[i].setText("");
						editTexts[i].setMinLines(1);
					}
				}
				keyboardBtn[16].setTextColor(getResources().getColor(R.color.input_disenable_color));
				keyboardBtn[16].setEnabled(false);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {

			if(s.toString().trim().length()==0){
				keyboardBtn[16].setTextColor(getResources().getColor(R.color.input_disenable_color));
				keyboardBtn[16].setEnabled(false);
			}
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {

			hasPoint(s.toString());
		}

	}

	/**is decimals
	 * @param s
	 */
	private void hasPoint(String s){
		if(s.indexOf(".")!=-1||s.length()==0){
			keyboardBtn[16].setTextColor(getResources().getColor(R.color.input_disenable_color));
			keyboardBtn[16].setEnabled(false);
		}else{
			keyboardBtn[16].setEnabled(true);
			keyboardBtn[16].setTextColor(getResources().getColor(R.color.input_enable_color));
		}
	}

	/**format data
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

}
