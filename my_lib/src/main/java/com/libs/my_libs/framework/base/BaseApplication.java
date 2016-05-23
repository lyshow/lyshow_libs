
package com.libs.my_libs.framework.base;

import java.util.Hashtable;

import android.app.Application;
import android.content.Context;

/**
 * 描述:全局Application
 */
public abstract class BaseApplication extends Application {

    // 应用全局变量存储在这里
    private static Hashtable<String, Object> mAppParamsHolder;

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mAppParamsHolder = new Hashtable<String, Object>();
    }

    /**
     * 上下文
     * 
     * @return
     */
    public static Context getContext() {
        if (mContext == null) {
            throw new IllegalStateException("context is null.");
        }
        return mContext;
    }

    /**
     * 存储全局数据
     * 
     * @param key
     * @param value
     */
    public static void putValue(String key, Object value) {
        mAppParamsHolder.put(key, value);
    }

    /**
     * 获取全局数据
     * 
     * @param key
     * @return
     */
    public static Object getValue(String key) {
        return mAppParamsHolder.get(key);
    }

    /**
     * 是否已存放
     * 
     * @param key
     * @return
     */
    public static boolean containsKey(String key) {
        return mAppParamsHolder.containsKey(key);
    }

    public static void removeKey(String key) {
        mAppParamsHolder.remove(key);
    }

}
