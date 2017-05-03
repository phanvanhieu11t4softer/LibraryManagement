package com.framgia.users.bean;

public class PermissionInfo {
	private int permissionsId;
	private String permissionName;
	private String description;
	private String deleteFlag;
	private String dateCreate;
	private String userCreate;
	private String dateUpdate;
	private String userUpdate;

	public PermissionInfo() {
	}

	public PermissionInfo(int permissionsId, String permissionName, String description, String deleteFlag,
			String dateCreate, String userCreate, String dateUpdate, String userUpdate) {
		this.permissionsId = permissionsId;
		this.permissionName = permissionName;
		this.description = description;
		this.deleteFlag = deleteFlag;
		this.dateCreate = dateCreate;
		this.userCreate = userCreate;
		this.dateUpdate = dateUpdate;
		this.userUpdate = userUpdate;
	}

	public int getPermissionsId() {
		return permissionsId;
	}

	public void setPermissionsId(int permissionsId) {
		this.permissionsId = permissionsId;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(String dateCreate) {
		this.dateCreate = dateCreate;
	}

	public String getUserCreate() {
		return userCreate;
	}

	public void setUserCreate(String userCreate) {
		this.userCreate = userCreate;
	}

	public String getDateUpdate() {
		return dateUpdate;
	}

	public void setDateUpdate(String dateUpdate) {
		this.dateUpdate = dateUpdate;
	}

	public String getUserUpdate() {
		return userUpdate;
	}

	public void setUserUpdate(String userUpdate) {
		this.userUpdate = userUpdate;
	}

}
