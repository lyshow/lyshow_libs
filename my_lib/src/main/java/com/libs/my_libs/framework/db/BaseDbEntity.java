package com.libs.my_libs.framework.db;

/**
 * 描述:数据库表对应的实体类
 */
public class BaseDbEntity {

	protected long id;

	protected long createAt;

	protected long modifiedAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(long createAt) {
		this.createAt = createAt;
	}

	public long getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(long modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

}
