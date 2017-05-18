package com.framgia.users.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.framgia.users.model.ConstantModel;
import com.framgia.users.model.Users;

/**
 * UserDaoImpl.java
 * 
 * @version 16/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Repository("userDao")
public class UserDAOImpl extends AbstractDao<Integer, Users> implements ConstantModel, UserDAO {

	// log
	private static final Logger logger = Logger.getLogger(UserDAOImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public Users findByUserName(String username) {

		logger.info("Search user by username.");
		List<Users> users = new ArrayList<Users>();

		users = getSession().createQuery("from Users where userName=:username and deleteFlag=:delFlg")
		        .setParameter("username", username).setParameter("delFlg", ConstantModel.DEL_FLG).list();

		if (users.size() > 0) {

			logger.info("End search user by username.");

			return users.get(0);
		} else {

			logger.error("Search user by username ERROR");

			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "finally" })
	@Override
	public List<Users> findByConditon(String txtName, int txtPermission) {

		logger.info("Search list user.");

		List<Users> queryList = new ArrayList<Users>();

		Session session = getOpenSession();

		try {
			String sql = "from Users where deleteFlag = :deleteFlag";
			if (null != txtName && StringUtils.isNotEmpty(txtName)) {
				sql = sql + " and userName like :userName";
			}

			if (txtPermission > 0) {
				sql = sql + " and permissionsId = :permissionsId";
			}
			Query query = session.createQuery(sql);

			query.setParameter("deleteFlag", ConstantModel.DEL_FLG);
			if (null != txtName && StringUtils.isNotEmpty(txtName)) {
				query.setParameter("userName", "%" + txtName + "%");
			}

			if (txtPermission > 0) {
				query.setParameter("permissionsId", txtPermission);
			}

			queryList = query.list();
			logger.info("Search list user end.");
		} catch (Exception e) {
			logger.error("Error search list user: ", e);
		} finally {
			return queryList;
		}

	}

	@SuppressWarnings("finally")
	@Override
	public Users findById(int idUser) {
		logger.info("Search user by is.");

		Users user = new Users();

		Session session = getOpenSession();

		try {
			String sql = "from Users where deleteFlag = :deleteFlag AND userId = :idUser";

			Query query = session.createQuery(sql);

			query.setParameter("deleteFlag", ConstantModel.DEL_FLG);

			query.setParameter("idUser", idUser);

			if (query.list().size() > 0) {
				user = (Users) query.list().get(0);
			}
			logger.info("Search user by id end.");

		} catch (Exception e) {
			logger.error("Error search user by id: ", e);
		} finally {
			return user;
		}

	}

	@Override
	public void updateUser(Users user) {
		getSession().saveOrUpdate(user);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public Users findToUpdate(int idUser, String email, String username, String password, String token) {
		logger.info("Search user to update");
		Criteria crit = getSession().createCriteria(Users.class);
		crit.add(Restrictions.eq("deleteFlag", ConstantModel.DEL_FLG));

		if (StringUtils.isNotBlank(String.valueOf(idUser)) && idUser > 0) {
			crit.add(Restrictions.eq("userId", idUser));
		}

		if (StringUtils.isNotBlank(username)) {
			crit.add(Restrictions.eq("userName", username));
		}

		if (StringUtils.isNotBlank(email)) {
			crit.add(Restrictions.eq("email", email));
		}

		if (StringUtils.isNotBlank(password)) {
			crit.add(Restrictions.eq("passWord", password));
		}

		if (StringUtils.isNotBlank(token)) {
			crit.add(Restrictions.eq("tokenResetPassword", token));
		}

		crit.setLockMode(LockMode.UPGRADE);

		List<Users> items = crit.list();

		if (items != null && items.size() > 0) {
			return items.get(0);
		}
		return null;
	}
	
	@Override
	public void insertUser(Users users) {
		
		// Insert data into table Books
		getOpenSession().saveOrUpdate(users);
		logger.info("Insert success.");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Users findUserName(String userName) {
		List<Users> users = new ArrayList<Users>();

		users = getOpenSession().createQuery("from Users where userName=:userName and deleteFlag=:delFlg")
				.setParameter("userName", userName)
				.setParameter("delFlg", ConstantModel.DEL_FLG).list();

		if (users.size() > 0) {

			return users.get(0);
		} else {

			return null;
		}
	}
}
