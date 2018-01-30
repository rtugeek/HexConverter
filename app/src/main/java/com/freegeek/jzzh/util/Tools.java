package com.freegeek.jzzh.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;

import android.text.TextUtils;

import com.freegeek.jzzh.R;

/**
 * @author Jack Fu <rtugeek@gmail.com>
 * @date 2017/11/15
 * @description
 */
public class Tools {
	private static BigInteger mBigData, mIntegerOfRadix;
	private static String digits = "0123456789ABCDEF";

	/**
	 * @param data
	 * @param fromRadix
	 * @return
	 */
	public static String integerConvertTo10(BigInteger data, int fromRadix){
		String result = "";
		mIntegerOfRadix =new BigInteger(String.valueOf(fromRadix));
		while(!data.toString().equals("0")){
			result = digits.charAt(data.remainder(mIntegerOfRadix).intValue()) + result;
			data = data.divide(mIntegerOfRadix);
		}
		if(TextUtils.isEmpty(result)){
			result="0";
		}
		return result;
	}


	/**
	 * @see {@link java.lang.Integer#parseInt(String, int)}
	 * @param data
	 * @param toRadix
	 * @param fromRadix
	 * @return
	 */
	public static String integerConverter(String data, int toRadix, int fromRadix){
		if(fromRadix == toRadix){
			return data;
		}
		char[] chars = data.toCharArray();
		int len = chars.length;
		if(toRadix != 10){
			data = integerConverter(data, 10, fromRadix);
			return integerConvertTo10(new BigInteger(data), toRadix);
		}else{
			mBigData =new BigInteger("0");
			for(int i = len - 1; i >=0; i--){
				mIntegerOfRadix =new BigDecimal(digits.indexOf(chars[i])*Math.pow(fromRadix, len - i - 1)).toBigInteger();
				mBigData = mBigData.add(mIntegerOfRadix);
			}
			return mBigData.toString();
		}
	}


	/**
	 *
	 * @param data
	 * @param toRadix
	 * @param formRadix
	 * @return the decimals represented by the string argument in the specified radix.
	 * Supports maximum of 10 decimal places.
	 */
	public static String decimalsConverter(String data, int toRadix, int formRadix) {
		
		if(toRadix == formRadix){
			return data;
		}
		//if specified radix(toRadix) is not base 10, covert decimals in base 10
		if(formRadix != 10){
			data = decimalsConvertTo10(data, formRadix);
			if(toRadix == 10){
				return data;
			}
		}
		char[] chars = digits.toCharArray();
		int integer;
		BigDecimal bigDecimal=new BigDecimal("0." + data);
		String result = "";
		//covert decimals(base 10) in specified radix(toRadix)
		while(bigDecimal.compareTo(new BigDecimal("0")) != 0){
			bigDecimal =bigDecimal.multiply(new BigDecimal(toRadix));
			integer=bigDecimal.intValue();
			result += chars[integer];
			bigDecimal =new BigDecimal("0."+bigDecimal.toPlainString().split("\\.")[1]);
			//if length greater then 9(equal to 10) break
			if(result.length() > 9){
				break;
			}
		}

		if(result.length() == 0){
			result = "0";
		}
		return result;

	}

	/**
	 *
	 * @param data
	 * @param fromRadix
	 * @return decimal system fractional part of the given data in the specified radix
	 */
	public static String decimalsConvertTo10(String data, int fromRadix) {
		
        char[] chars = data.toCharArray();
        BigDecimal sum=new BigDecimal("0");
        for(int i=0;i<data.length();i++){
			int index = digits.indexOf(chars[i]);
			int power =  -i-1;
            sum =sum.add(new BigDecimal(index * Math.pow(fromRadix,power)));
        }
        String[] ss=sum.toString().split("\\.");
        try {
            return  ss[1];
        }catch (Exception e){
            return  "0";
        }
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static int getIdByResourceName(String s){
		int resourceId = 0;
        try {
        Field field = R.id.class.getField(s);
        field.setAccessible(true);
        try {
                resourceId = field.getInt(null);
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {}
        } catch (NoSuchFieldException e) {}
        return resourceId;
	}


	/**
	 *
	 * @param data
	 * @param radix
	 * @return weather the data's format match the given radix.
	 */
	public static boolean checkData(String data,int radix){
		data = data.replaceAll(" ", "");
		String digits=".0123456789ABCDEF";
		//can only contains one point
		if(data.split("\\.").length > 2){
			return false;
		}
		for(int i = 0;i<data.length();i++){
			char digit = data.charAt(i);
			int index = digits.indexOf(digit);
			if(index == -1 || index > radix ){
				return false;
			}
		}
		return true;
	}
}
