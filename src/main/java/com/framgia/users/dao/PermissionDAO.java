package com.framgia.users.dao;

import java.util.List;

import com.framgia.users.model.Permissions;

/**
 * ManagementUsersController.java
 * 
 * @version 19/04/2017
 * @author vu.thi.tran.van@framgia.com
 */
public interface PermissionDAO {
	
	// Search permission with status enable
	List<Permissions> findByDelFlg();

	// Find PermissionID
	Permissions findPermissionId(String permissionId);

	// Insert data into table permissions
	void insertPermission(Permissions permissions);

	// find permissionName
	Permissions findPermissionName(String permissionName);
}
