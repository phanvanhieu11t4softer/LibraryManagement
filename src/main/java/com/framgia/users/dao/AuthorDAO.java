package com.framgia.users.dao;

import java.util.List;

import com.framgia.users.bean.BeanAuthor;

/**
 * AuthorDAO.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
public interface AuthorDAO {

	public Long findAuthorId(String authorsId, String deleteFlag);
	public int addAuthor(List<BeanAuthor> listAuthor);
}
