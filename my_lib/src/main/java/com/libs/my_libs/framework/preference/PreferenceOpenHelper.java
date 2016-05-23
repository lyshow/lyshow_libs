package com.libs.my_libs.framework.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 首选项设置操作类，所有设置的父类
 */
public abstract class PreferenceOpenHelper {

	private Context context;

	private String name;

	private int mode;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            　上下文
	 * @param prefname
	 *            　pref文件名
	 */
	public PreferenceOpenHelper(Context context, String prefname) {
		this.context = context;
		this.name = prefname;
		this.mode = Context.MODE_PRIVATE;
	}

	private SharedPreferences getSharedPreferences() {
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp;
	}

	protected Context getContext() {
		return context;
	}

	protected String getString(String key, String defaultValue) {
		return getSharedPreferences().getString(key, defaultValue);
	}

	protected int getInt(String key, int defaultValue) {
		return getSharedPreferences().getInt(key, defaultValue);
	}

	protected long getLong(String key, long defaultValue) {
		return getSharedPreferences().getLong(key, defaultValue);
	}

	protected boolean getBoolean(String key, boolean defaultValue) {
		return getSharedPreferences().getBoolean(key, defaultValue);
	}

	protected float getFloat(String key, float defaultValue) {
		return getSharedPreferences().getFloat(key, defaultValue);
	}

	protected boolean putString(String key, String value) {
		return getSharedPreferences().edit().putString(key, value).commit();
	}

	protected boolean putInt(String key, int value) {
		return getSharedPreferences().edit().putInt(key, value).commit();
	}

	protected boolean putLong(String key, long value) {
		return getSharedPreferences().edit().putLong(key, value).commit();
	}

	protected boolean putBoolean(String key, boolean value) {
		return getSharedPreferences().edit().putBoolean(key, value).commit();
	}

	protected boolean putFloat(String key, float value) {
		return getSharedPreferences().edit().putFloat(key, value).commit();
	}

	protected Editor getEditor() {
		return getSharedPreferences().edit();
	}

	protected boolean contains(String key) {
		return getSharedPreferences().contains(key);
	}

	/*************** 支持资源ID ******************/

	protected String getString(int resId, String defValue) {
		return getString(context.getString(resId), defValue);
	}

	protected int getInt(int resId, int defValue) {
		return getInt(context.getString(resId), defValue);
	}

	protected float getFloat(int resId, float defValue) {
		return getFloat(context.getString(resId), defValue);
	}

	protected boolean getBoolean(int resId, boolean defValue) {
		return getBoolean(context.getString(resId), defValue);
	}

	protected long getLong(int resId, long defValue) {
		return getLong(context.getString(resId), defValue);
	}

	protected boolean putInt(int resId, int value) {
		return putInt(context.getString(resId), value);
	}

	protected boolean putFloat(int resId, float value) {
		return putFloat(context.getString(resId), value);
	}

	protected boolean putBoolean(int resId, boolean value) {
		return putBoolean(context.getString(resId), value);
	}

	protected boolean putString(int resId, String value) {
		return putString(context.getString(resId), value);
	}

	protected boolean putLong(int resId, long value) {
		return putLong(context.getString(resId), value);
	}

	protected boolean removeKey(String key) {
		return getEditor().remove(key).commit();
	}

}
