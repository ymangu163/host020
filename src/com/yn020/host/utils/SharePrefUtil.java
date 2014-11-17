package com.yn020.host.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefUtil {
	private static String SP_NAME = "config";
	private static SharedPreferences sp;
	
	/**
	 * #保存字符串
	 ***/
	public static void saveString(Context ctx, String key, String value) {
		if (sp == null) {
			sp = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);			
		}
		sp.edit().putString(key, value).commit();
		
	}

	
	/**
	 * 获取字符值
	 */
	public static String getString(Context context, String key, String defValue) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		return sp.getString(key, defValue);
	}
	
	/**
	 * #清空缓存
	 ***/
	public static void clear(Context context) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);			
		}
		sp.edit().clear().commit();
	}

}
