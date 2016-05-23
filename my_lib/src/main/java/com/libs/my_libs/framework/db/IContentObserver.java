package com.libs.my_libs.framework.db;

/**
 * 描述:下载表变化监听接口
 */
public interface IContentObserver<TableEntity> {

	/**
	 * 增加
	 */
	int STATE_INSERT = 0X10;

	/**
	 * 更新
	 */
	int STATE_UPDATE = 0X11;

	/**
	 * 删除
	 */
	int STATE_DELETE = 0X12;

	/**
	 * 下载表变化监听
	 * 
	 * @param entity
	 * @param state
	 */
	void onChange(TableEntity entity, int state);
}
