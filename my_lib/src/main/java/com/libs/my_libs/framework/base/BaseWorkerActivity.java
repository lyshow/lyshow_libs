package com.libs.my_libs.framework.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 描述:可处理耗时操作的activity
 */
public class BaseWorkerActivity extends BaseActivity implements IWorkerActivity {

	private HandlerThread mHandlerThread;

	private BackgroundHandler mBackgroundHandler;

	// 后台Handler
	private static class BackgroundHandler extends Handler {

		private final WeakReference<BaseWorkerActivity> mActivityReference;

		BackgroundHandler(BaseWorkerActivity activity, Looper looper) {
			super(looper);
			mActivityReference = new WeakReference<BaseWorkerActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mActivityReference.get() != null) {
				mActivityReference.get().handleBackgroundMessage(msg);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandlerThread = new HandlerThread("activity worker:" + getClass().getSimpleName());
		mHandlerThread.start();
		mBackgroundHandler = new BackgroundHandler(this, mHandlerThread.getLooper());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBackgroundHandler != null && mBackgroundHandler.getLooper() != null) {
			mBackgroundHandler.getLooper().quit();
		}
	}

	/**
	 * 处理后台操作
	 */
	public void handleBackgroundMessage(Message msg) {
	}

	/**
	 * 发送后台操作
	 * 
	 * @param msg
	 */
	protected void sendBackgroundMessage(Message msg) {
		mBackgroundHandler.sendMessage(msg);
	}

	protected void sendBackgroundMessageDelayed(Message msg, long delay) {
		mBackgroundHandler.sendMessageDelayed(msg, delay);
	}

	/**
	 * 发送后台操作
	 * 
	 * @param what
	 */
	protected void sendEmptyBackgroundMessage(int what) {
		mBackgroundHandler.sendEmptyMessage(what);
	}

	protected void sendEmptyBackgroundMessageDelayed(int what, long delay) {
		mBackgroundHandler.sendEmptyMessageDelayed(what, delay);
	}

	protected void removeBackgroundMessages(int what) {
		mBackgroundHandler.removeMessages(what);
	}

	protected Message obtainBackgroundMessage() {
		return mBackgroundHandler.obtainMessage();
	}

}
