package com.framgia.users.service;

import java.util.List;

import com.framgia.users.bean.BeanAuthor;

/**
 * AuthorService.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
public interface AuthorService {

	public Long findAuthorId(String authorsId, String deleteFlag);
	public int addAuthor(List<BeanAuthor> listAuthor);
	
}
