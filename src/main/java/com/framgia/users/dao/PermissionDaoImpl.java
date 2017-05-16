package com.framgia.users.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.framgia.users.model.Book;
import com.framgia.users.model.ConstantModel;
import com.framgia.users.model.Permissions;

/**
 * ManagementUsersController.java
 * 
 * @version 19/04/2017
 * @author vu.thi.tran.van@framgia.com
 */
@Repository("permissionDao")
public class PermissionDaoImpl extends AbstractDao<Integer, Permissions> implements ConstantModel, PermissionDao {

	// log
	private static final Logger logger = Logger.getLogger(PermissionDaoImpl.class);

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Permissions> findByDelFlg() {
		logger.info("Search list permission.");

		Session session = getOpenSession();
		try {
			Query query = session.createQuery("from Permissions where deleteFlag = :deleteFlag");
			query.setParameter("deleteFlag", ConstantModel.DEL_FLG);
			logger.info("Search list permission end.");

			return query.list();
		} catch (Exception e) {
			logger.error("Error search list permission: " + e.getMessage());

			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Permissions findPermissionId(String permissionId) {
		List<Permissions> permissions = new ArrayList<Permissions>();

		permissions = getOpenSession().createQuery("from Permissions where permissionsId=:permissionsId and deleteFlag=:delFlg")
				.setParameter("permissionsId", Integer.parseInt(permissionId))
				.setParameter("delFlg", ConstantModel.DEL_FLG).list();

		if (permissions.size() > 0) {

			return permissions.get(0);
		} else {

			return null;
		}
	}
	
	@Override
	public void insertPermission(Permissions permissions) {
		
		// Insert data into table Books
		getOpenSession().saveOrUpdate(permissions);
		logger.info("Insert success.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Permissions findPermissionName(String permissionName) {
		List<Permissions> permissions = new ArrayList<Permissions>();

		permissions = getOpenSession().createQuery("from Permissions where permissionName=:permissionName and deleteFlag=:delFlg")
				.setParameter("permissionName", permissionName)
				.setParameter("delFlg", ConstantModel.DEL_FLG).list();

		if (permissions.size() > 0) {

			return permissions.get(0);
		} else {

			return null;
		}
	}
}
