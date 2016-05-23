package com.libs.my_libs.framework.db;

import android.provider.BaseColumns;

/**
 * 描述:基本字段
 */
public interface AppBaseColumns extends BaseColumns {

	/**
	 * 创建时间
	 */
	public static final String CREATE_AT = "createAt";

	/**
	 * 修改时间
	 */
	public static final String MODIFIED_AT = "modifiedAt";

}
