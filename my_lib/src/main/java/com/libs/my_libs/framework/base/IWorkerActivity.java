package com.libs.my_libs.framework.base;

import android.os.Message;

/**
 * 描述:可后台操作的抽象Activity接口
 */
public interface IWorkerActivity extends IActivity {

	/**
	 * 处理后台操作
	 * 
	 * @param msg
	 */
	void handleBackgroundMessage(Message msg);
}
