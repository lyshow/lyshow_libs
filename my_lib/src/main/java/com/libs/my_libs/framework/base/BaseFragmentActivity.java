package com.libs.my_libs.framework.base;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.umeng.analytics.MobclickAgent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * 描述:抽象FragmentActivity，提供刷新UI的Handler
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements IActivity {

	private Handler mUiHandler = new UiHandler(this);

	private static class UiHandler extends Handler {
		private final WeakReference<BaseFragmentActivity> mActivityReference;

		public UiHandler(BaseFragmentActivity activity) {
			mActivityReference = new WeakReference<BaseFragmentActivity>(activity);
		}

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mActivityReference.get() != null) {
				mActivityReference.get().handleUiMessage(msg);
			}
		};
	}

	private ArrayList<String> mActions = new ArrayList<String>();

	private BaseBroadcastReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setupActions(mActions);
		if (mActions != null && mActions.size() > 0) {
			IntentFilter filter = new IntentFilter();
			for (String action : mActions) {
				filter.addAction(action);
			}
			mReceiver = new BaseBroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					super.onReceive(context, intent);
					handleBroadcast(context, intent);
				}
			};
			registerReceiver(mReceiver, filter);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
	}

	@Override
	public void setupActions(ArrayList<String> actions) {
	}

	/**
	 * 处理更新UI任务
	 * 
	 * @param msg
	 */
	public void handleUiMessage(Message msg) {
	}

	public void handleBroadcast(Context context, Intent intent) {
	}

	/**
	 * 发送UI更新操作
	 * 
	 * @param msg
	 */
	public void sendUiMessage(Message msg) {
		mUiHandler.sendMessage(msg);
	}

	public void sendUiMessageDelayed(Message msg, long delayMillis) {
		mUiHandler.sendMessageDelayed(msg, delayMillis);
	}

	/**
	 * 发送UI更新操作
	 * 
	 * @param what
	 */
	public void sendEmptyUiMessage(int what) {
		mUiHandler.sendEmptyMessage(what);
	}

	public void sendEmptyUiMessageDelayed(int what, long delayMillis) {
		mUiHandler.sendEmptyMessageDelayed(what, delayMillis);
	}

	public void removeUiMessages(int what) {
		mUiHandler.removeMessages(what);
	}

	protected Message obtainUiMessage() {
		return mUiHandler.obtainMessage();
	}

	/**
	 * 显示一个Toast类型的消息
	 * 
	 * @param msg
	 *            显示的消息
	 */
	public void showToast(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseFragmentActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 显示{@link Toast}通知
	 * 
	 * @param strResId
	 *            字符串资源id
	 */
	public void showToast(final int strResId) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseFragmentActivity.this, getResources().getString(strResId), Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	/**
	 * 隐藏软键盘
	 */
	protected void hideSoftInput(Context context) {
		InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		// manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
		if (getCurrentFocus() != null) {
			manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

	/**
	 * 显示软键盘
	 */
	protected void showSoftInput() {
		InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// 友盟统计
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// 友盟统计
		MobclickAgent.onPause(this);
	}
}
