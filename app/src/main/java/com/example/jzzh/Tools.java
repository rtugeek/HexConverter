package com.example.jzzh;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import android.content.Context;

public class Tools {
	static BigInteger mBigData1, mBigData2;
	static String digths = "0123456789ABCDEF";

	/**
	 * @param num
	 * @param base
	 * @return
	 */
	public static String integerConvertTo10(BigInteger num, int base){
		String s = "";
		mBigData2 =new BigInteger(base+"");
		while(!num.toString().equals("0")){
			s =digths.charAt(num.remainder(mBigData2).intValue()) + s;
			num=num.divide(mBigData2);
		}
		if(s.equals(""))s="0";
		return s;
	}


	/**
	 * @param num
	 * @param fromHex
	 * @param toHex
	 * @return
	 */
	public static String integerConverter(String num, int fromHex, int toHex){
		if(toHex == fromHex){
			return num;
		}
		char[] chars = num.toCharArray();
		int len = chars.length;
		if(fromHex != 10){
			num = integerConverter(num, 10, toHex);
			return integerConvertTo10(new BigInteger(num + ""), fromHex);
		}else{
			mBigData1 =new BigInteger("0");
			for(int i = len - 1; i >=0; i--){
				mBigData2 =new BigDecimal(digths.indexOf(chars[i])*Math.pow(toHex, len - i - 1)).toBigInteger();
				mBigData1 = mBigData1.add(mBigData2);
			}
			return mBigData1.toString();
		}
	}


	/**
	 * Converting without integer part.
	 * @param data
	 * @param toHex
	 * @param fromHex
	 * @return
	 */
	public static String decimalConverter(String data, int toHex, int fromHex) {
		
		if(toHex == fromHex){
			return data;
		}
		if(toHex != 10){
			data = decimalConvertTo10(data, toHex);
		}
		char[] chars = digths.toCharArray();
		int wei =0,integer;
		BigDecimal bd=new BigDecimal("0." + data);
		String sum2 = "";
		while(!bd.toString().equals("0")){
			bd =bd.multiply(new BigDecimal(fromHex));
			integer=bd.intValue();
			sum2 += chars[integer];
			bd =new BigDecimal("0."+bd.toPlainString().split("\\.")[1]);
			wei++;
			if(wei > 9){
				break;
			}
		}
		int index = sum2.length();
		for(int i = sum2.length();i > 0;i--){
			int x = 0;
			try {
				x = Integer.parseInt(sum2.substring(i - 1 , i));
			} catch (Exception e) {
			}
			if(x == 0){
				index = i - 1;
			}else{
				break;
			}
		}
		sum2 = sum2.substring(0,index);
		if(sum2.length() == 0) sum2 = "0";
		return sum2;
	}

	/**
	 *
	 * @param data
	 * @param hex
	 * @return
	 */
	public static String decimalConvertTo10(String data, int hex) {
		
        char[] chars = data.toCharArray();
        BigDecimal sum=new BigDecimal("0");
        for(int i=0;i<data.length();i++){
        sum =sum.add(new BigDecimal(digths.indexOf(chars[i])*Math.pow(hex, -i-1)));
        }
        String[] ss=sum.toString().split("\\.");
        try {
            return  ss[1];
        }catch (Exception e){
            return  "0";
        }
	}
	
	public static int getIdByResourceName(Context c,String s){
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
	
}
