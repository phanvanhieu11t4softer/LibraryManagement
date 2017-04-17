package com.framgia.users.dao;

import com.framgia.users.model.Users;

/**
 * UserDao.java
 * 
 * @version 16/04/2017
 * @author phan.van.hieu@framgia.com
 */
public interface UserDao {

    Users findByUserName(String username);

}
