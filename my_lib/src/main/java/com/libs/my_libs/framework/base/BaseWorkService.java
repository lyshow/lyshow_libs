package com.libs.my_libs.framework.base;

import java.lang.ref.WeakReference;

import android.app.Service;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/**
 * 描述:可处理耗时操作的service
 */
public abstract class BaseWorkService extends Service {

	private HandlerThread mHandlerThread;

	private BackgroundHandler mBackgroundHandler;

	private Handler mUiHandler = new UiHandler(this);

	private static class UiHandler extends Handler {
		private final WeakReference<BaseWorkService> mServiceReference;

		public UiHandler(BaseWorkService activity) {
			mServiceReference = new WeakReference<BaseWorkService>(activity);
		}

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mServiceReference.get() != null) {
				mServiceReference.get().handleUiMessage(msg);
			}
		};
	}

	/**
	 * 处理更新UI任务
	 * 
	 * @param msg
	 */
	public void handleUiMessage(Message msg) {
	}

	/**
	 * 发送UI更新操作
	 * 
	 * @param msg
	 */
	protected void sendUiMessage(Message msg) {
		mUiHandler.sendMessage(msg);
	}

	protected void sendUiMessageDelayed(Message msg, long delayMillis) {
		mUiHandler.sendMessageDelayed(msg, delayMillis);
	}

	/**
	 * 发送UI更新操作
	 * 
	 * @param what
	 */
	protected void sendEmptyUiMessage(int what) {
		mUiHandler.sendEmptyMessage(what);
	}

	protected void sendEmptyUiMessageDelayed(int what, long delayMillis) {
		mUiHandler.sendEmptyMessageDelayed(what, delayMillis);
	}

	protected void removeUiMessage(int what) {
		mUiHandler.removeMessages(what);
	}

	protected Message obtainUiMessage() {
		return mUiHandler.obtainMessage();
	}

	// 后台Handler
	private static class BackgroundHandler extends Handler {

		private final WeakReference<BaseWorkService> mFragmentReference;

		BackgroundHandler(BaseWorkService fragment, Looper looper) {
			super(looper);
			mFragmentReference = new WeakReference<BaseWorkService>(fragment);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mFragmentReference.get() != null) {
				mFragmentReference.get().handleBackgroundMessage(msg);
			}
		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
		mHandlerThread = new HandlerThread("service worker:" + getClass().getSimpleName());
		mHandlerThread.start();
		mBackgroundHandler = new BackgroundHandler(this, mHandlerThread.getLooper());
	}

	@Override
	public void onDestroy() {
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
		if (mBackgroundHandler != null) {
			mBackgroundHandler.sendMessage(msg);
		}
	}

	protected void sendBackgroundMessageDelayed(Message msg, long delayMillis) {
		if (mBackgroundHandler != null) {
			mBackgroundHandler.sendMessageDelayed(msg, delayMillis);
		}
	}

	/**
	 * 发送后台操作
	 * 
	 * @param what
	 */
	protected void sendEmptyBackgroundMessage(int what) {
		if (mBackgroundHandler != null) {
			mBackgroundHandler.sendEmptyMessage(what);
		}
	}

	protected void sendEmptyBackgroundMessageDelayed(int what, long delayMillis) {
		if (mBackgroundHandler != null) {
			mBackgroundHandler.sendEmptyMessageDelayed(what, delayMillis);
		}
	}

	protected void removeBackgroundMessage(int what) {
		if (mBackgroundHandler != null) {
			mBackgroundHandler.removeMessages(what);
		}
	}

	protected Message obtainBackgroundMessage() {
		return mBackgroundHandler.obtainMessage();
	}

}
