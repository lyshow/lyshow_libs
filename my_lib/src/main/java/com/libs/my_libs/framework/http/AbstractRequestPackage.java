package com.libs.my_libs.framework.http;

import java.util.Hashtable;
import java.util.Set;

/**
 * 抽象请求包
 */
public abstract class AbstractRequestPackage implements RequestPackage {

	protected Hashtable<String, Object> mParams;

	@Override
	public String getGetRequestParams() {
		if (mParams != null && mParams.size() >= 0) {
			StringBuilder builder = new StringBuilder();
			final Set<String> keys = mParams.keySet();
			for (String key : keys) {
				builder.append(key).append("=").append(mParams.get(key)).append("&");
			}
			builder.deleteCharAt(builder.length() - 1);
			return builder.toString();
		}
		return "";
	}

	public Hashtable<String, Object> getParams() {
		return mParams;
	}

	public void setParams(Hashtable<String, Object> mParams) {
		this.mParams = mParams;
	}

	@Override
	public Hashtable<String, String> getRequestHeaders() {
		return null;
	}

	@Override
	public Hashtable<String, Object> getSettings() {
		return null;
	}
}
