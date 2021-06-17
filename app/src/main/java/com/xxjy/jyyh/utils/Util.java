package com.xxjy.jyyh.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.xxjy.jyyh.base.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Util {
	
	private static final String TAG = "SDK_Sample.Util";
	
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static byte[] getHtmlByteArray(final String url) {
		 URL htmlUrl = null;     
		 InputStream inStream = null;     
		 try {         
			 htmlUrl = new URL(url);         
			 URLConnection connection = htmlUrl.openConnection();         
			 HttpURLConnection httpConnection = (HttpURLConnection)connection;         
			 int responseCode = httpConnection.getResponseCode();         
			 if(responseCode == HttpURLConnection.HTTP_OK){             
				 inStream = httpConnection.getInputStream();         
			  }     
			 } catch (MalformedURLException e) {               
				 e.printStackTrace();     
			 } catch (IOException e) {              
				e.printStackTrace();    
		  } 
		byte[] data = inputStreamToByte(inStream);

		return data;
	}
	
	public static byte[] inputStreamToByte(InputStream is) {
		try{
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static byte[] readFromFile(String fileName, int offset, int len) {
		if (fileName == null) {
			return null;
		}

		File file = new File(fileName);
		if (!file.exists()) {
			Log.i(TAG, "readFromFile: file not found");
			return null;
		}

		if (len == -1) {
			len = (int) file.length();
		}

		Log.d(TAG, "readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

		if(offset <0){
			Log.e(TAG, "readFromFile invalid offset:" + offset);
			return null;
		}
		if(len <=0 ){
			Log.e(TAG, "readFromFile invalid len:" + len);
			return null;
		}
		if(offset + len > (int) file.length()){
			Log.e(TAG, "readFromFile invalid file len:" + file.length());
			return null;
		}

		byte[] b = null;
		try {
			RandomAccessFile in = new RandomAccessFile(fileName, "r");
			b = new byte[len]; // 创建合适文件大小的数组
			in.seek(offset);
			in.readFully(b);
			in.close();

		} catch (Exception e) {
			Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
			e.printStackTrace();
		}
		return b;
	}
	


	public static int parseInt(final String string, final int def) {
		try {
			return (string == null || string.length() <= 0) ? def : Integer.parseInt(string);

		} catch (Exception e) {
		}
		return def;
	}
	/**
	 * 版本号比较
	 *
	 * @param version1
	 * @param version2
	 * @return 0代表相等，1代表version1大于version2，-1代表version1小于version2
	 */
	public static int compareVersion(String version1, String version2) {
		if (TextUtils.isEmpty(version1) || TextUtils.isEmpty(version2)) {
			return 0;
		}
		if (version1.equals(version2)) {
			return 0;
		}
		String[] version1Array = version1.split("\\.");
		String[] version2Array = version2.split("\\.");
		// 获取最小长度值
		int minLen = Math.min(version1Array.length, version2Array.length);
		int diff = 0;
		try {
			for (int i = 0; i < minLen; i++) {
				diff = Integer.parseInt(version1Array[i]) - Integer.parseInt(version2Array[i]);
				if (diff != 0) {
					break;
				}
			}
		} catch (Exception e) {
			return 0;
		}
		if (diff == 0) {
			diff = version1Array.length - version2Array.length;
		}

		if (diff > 0) {
			return 1;
		} else if (diff == 0) {
			return 0;
		} else {
			return -1;
		}
	}


	//版本号
	public static String getVersionName() {
		String appVersionName = AppUtils.getAppVersionName();
		return TextUtils.isDigitsOnly(appVersionName) ? "" : appVersionName;
	}
	public static String formatDouble(double d) {
		BigDecimal bg =BigDecimal.valueOf(d).setScale(2, RoundingMode.DOWN);
		double num = bg.doubleValue();
		if (Math.round(num) - num == 0) {
			return String.valueOf((long) num);
		}
		return String.valueOf(num);
	}
	/**
	 * 跳转拨打电话界面
	 *
	 * @param activity
	 * @param phoneNumber
	 * @return
	 */
	public static boolean toDialPhoneAct(Context activity, String phoneNumber) {
		if (TextUtils.isEmpty(phoneNumber)) {
			return false;
		}
		try {
			Uri phoneUri = Uri.parse("tel:" + phoneNumber);
			Intent intent = new Intent(Intent.ACTION_DIAL, phoneUri);
			if (intent.resolveActivity(activity.getPackageManager()) != null) {
				activity.startActivity(intent);
				return true;
			}
		} catch (Exception e) {
		}
		((BaseActivity)activity).showToastWarning("您的设备无法拨打电话");
		return false;
	}
}
