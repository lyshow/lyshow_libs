package com.libs.my_libs.framework.http;


import com.libs.my_libs.framework.base.IEntity;

/**
 * 描述:通用http响应类
 */
public class HttpResponse implements IEntity {

	private static final long serialVersionUID = -1334485101032541334L;

	// 结果是否正确
	private boolean isOk;

	private boolean isResponse;

	public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}

	public boolean isResponse() {
		return this.isResponse;
	}

	public void setResponse(boolean isResponse) {
		this.isResponse = isResponse;
	}

}
