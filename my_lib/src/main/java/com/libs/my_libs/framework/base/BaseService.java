package com.libs.my_libs.framework.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 描述:服务基类
 */
public abstract class BaseService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
