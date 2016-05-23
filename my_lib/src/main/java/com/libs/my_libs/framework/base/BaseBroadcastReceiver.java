package com.libs.my_libs.framework.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 描述:所有广播接收器父类
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		LogUtil.d("action:" + intent.getAction());
	}

}
