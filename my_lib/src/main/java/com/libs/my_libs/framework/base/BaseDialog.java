package com.libs.my_libs.framework.base;

import android.app.Activity;
import android.app.Dialog;

/**
 * 描述:所有对话框基类
 */
public abstract class BaseDialog extends Dialog {

	public BaseDialog(Activity activity) {
		super(activity);
		setOwnerActivity(activity);
	}

	public BaseDialog(Activity activity, int theme) {
		super(activity, theme);
		setOwnerActivity(activity);
	}

}
