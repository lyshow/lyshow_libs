package com.libs.my_libs.framework.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 描述:所有Fragment的父类,提供刷新UI的Handler
 */
public abstract class BaseFragment extends Fragment implements IFragment {

	protected Handler mUiHandler = new UiHandler(this);

	private static class UiHandler extends Handler {
		private final WeakReference<BaseFragment> mFragmentReference;

		public UiHandler(BaseFragment activity) {
			mFragmentReference = new WeakReference<BaseFragment>(activity);
		}

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mFragmentReference.get() != null) {
				mFragmentReference.get().handleUiMessage(msg);
			}
		};
	}

	private ArrayList<String> mActions = new ArrayList<String>();

	private BaseBroadcastReceiver mReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
			getActivity().registerReceiver(mReceiver, filter);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mReceiver != null) {
			getActivity().unregisterReceiver(mReceiver);
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

	@Override
	public void handleBroadcast(Context context, Intent intent) {
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

	/**
	 * 显示一个Toast类型的消息
	 * 
	 * @param msg
	 *            显示的消息
	 */
	public void showToast(final String msg) {
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	/**
	 * 显示{@link Toast}通知
	 * 
	 * @param strResId
	 *            字符串资源id
	 */
	public void showToast(final int strResId) {
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getActivity(), getResources().getString(strResId), Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	/**
	 * 隐藏软键盘
	 */
	protected void hideSoftInput(Context context) {
		InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		// manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
		if (getActivity().getCurrentFocus() != null) {
			manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

	/**
	 * 显示软键盘
	 */
	protected void showSoftInput() {
		InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

}
