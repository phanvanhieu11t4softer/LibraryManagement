package com.framgia.users.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.framgia.users.model.Users;


/**
 * UserDaoImpl.java
 * 
 * @version 16/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public Users findByUserName(String username) {

		List<Users> users = new ArrayList<Users>();

		users = sessionFactory.getCurrentSession().createQuery("from Users where userName=? and deleteFlag=0").setParameter(0, username)
				.list();

		if (users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}
		
	}

}
