package com.libs.my_libs.framework.base;

import android.util.Log;

/**
 * 日志工具
 */
public class LogUtil {

	private static final String TAG = "Log";

	private static boolean isDebug = false;

	/**
	 * 是否处于调试模式
	 * 
	 * @param debug
	 */
	public static boolean isDebug() {
		return isDebug;
	}

	/**
	 * 设置是否调试模式
	 * 
	 * @param debug
	 */
	public static void setDebug(boolean debug) {
		isDebug = debug;
	}

	public static void d(String msg) {
		if (isDebug) {
			Log.d(TAG, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (isDebug) {
			Log.d(tag, msg);
		}
	}

	public static void e(String msg) {
		if (isDebug) {
			Log.e(TAG, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (isDebug) {
			Log.e(tag, msg);
		}
	}

}
