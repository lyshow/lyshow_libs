package com.libs.my_libs.framework.base;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 描述:可做耗时操作的后台服务
 */
public abstract class BaseWorkerService extends BaseService {

	private HandlerThread mHandlerThread;

	protected BackgroundHandler mBackgroundHandler;

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
		if (mHandlerThread.getLooper() != null) {
			mHandlerThread.getLooper().quit();
		}
	}

	/**
	 * 处理后台操作
	 */
	protected abstract void handleBackgroundMessage(Message msg);

	/**
	 * 发送后台操作
	 * 
	 * @param msg
	 */
	protected void sendBackgroundMessage(Message msg) {
		mBackgroundHandler.sendMessage(msg);
	}

	/**
	 * 发送后台操作
	 * 
	 * @param what
	 */
	protected void sendEmptyBackgroundMessage(int what) {
		mBackgroundHandler.sendEmptyMessage(what);
	}

	// 后台Handler
	private static class BackgroundHandler extends Handler {

		private final WeakReference<BaseWorkerService> mServiceReference;

		BackgroundHandler(BaseWorkerService service, Looper looper) {
			super(looper);
			mServiceReference = new WeakReference<BaseWorkerService>(service);
		}

		public WeakReference<BaseWorkerService> getServiceReference() {
			return mServiceReference;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (getServiceReference() != null && getServiceReference().get() != null) {
				getServiceReference().get().handleBackgroundMessage(msg);
			}
		}
	}
}
