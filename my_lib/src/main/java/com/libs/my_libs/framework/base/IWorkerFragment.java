package com.libs.my_libs.framework.base;

import android.os.Message;

/**
 * 描述:可后台操作的抽象Fragment接口
 */
public interface IWorkerFragment extends IFragment {

	/**
	 * 处理后台操作
	 * 
	 * @param msg
	 */
	void handleBackgroundMessage(Message msg);
}
