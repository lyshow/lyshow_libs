package com.libs.my_libs.framework.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 描述:数据库辅助类
 */
public abstract class BaseDbHelper {

	private Context mContext;

	private DatabaseOpenHelper mDatabaseOpenHelper;

	public BaseDbHelper(Context context) {
		mContext = context;
		mDatabaseOpenHelper = new DatabaseOpenHelper(mContext, getDatabaseName(), getDatabaseVersion());
	}

	/**
	 * 上下文
	 * 
	 * @return
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * 获取可读数据库
	 * 
	 * @return
	 */
	public SQLiteDatabase getReadableDatabase() {
		return mDatabaseOpenHelper.getReadableDatabase();
	}

	/**
	 * 获取可读写数据库
	 * 
	 * @return
	 */
	public SQLiteDatabase getWritableDatabase() {
		return mDatabaseOpenHelper.getWritableDatabase();
	}

	/**
	 * 返回数据库名称
	 * 
	 * @return
	 */
	protected abstract String getDatabaseName();

	/**
	 * 返回数据库版本号
	 * 
	 * @return
	 */
	protected abstract int getDatabaseVersion();

	/**
	 * 数据库创建成功，可以在这个回调里去创建数据表
	 */
	protected abstract void onDatabaseCreate(SQLiteDatabase db);

	/**
	 * 数据库升级
	 * 
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	protected abstract void onDatabaseUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

	private class DatabaseOpenHelper extends SQLiteOpenHelper {

		public DatabaseOpenHelper(Context context, String databaseName, int databaseVersion) {
			super(context, databaseName, null, databaseVersion);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			onDatabaseCreate(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			onDatabaseUpgrade(db, oldVersion, newVersion);
		}

		// 4.0以上系统在数据库从高降到低时，会强制抛出异常，通过重写这个方法，可以解决问题
		@SuppressLint("Override")
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// 虽然没调用到，但要保留本函数
		}

	}
}
