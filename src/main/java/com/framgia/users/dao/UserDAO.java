package com.framgia.users.dao;

import java.util.List;

import com.framgia.users.model.Users;

/**
 * UserDao.java
 * 
 * @version 16/04/2017
 * @author phan.van.hieu@framgia.com
 * 
 *         Update: vu.thi.tran.van@framgia.com 17/04/2017
 */
public interface UserDAO {

	// Search user with function login _ HieuPV
	Users findByUserName(String username);

	// Search user with input text search
	List<Users> findByConditon(String txtName, int txtPermission);

	// search user find by id by from
	Users findById(int idUser);

	// update information user by from
	void updateUser(Users uses);

	// Insert data into table Users
	void insertUser(Users users);

	// Search user name
	Users findUserName(String username);

	// Get user for update
	Users findToUpdate(int idUser, String email, String username, String password, String token);
}
