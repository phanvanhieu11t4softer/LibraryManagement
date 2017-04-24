package com.framgia.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framgia.users.bean.BeanAuthor;
import com.framgia.users.dao.AuthorDAO;

/**
 * AuthorServiceImpl.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Service
public class AuthorServiceImpl implements AuthorService {
	
	@ Autowired
	AuthorDAO authorDAO;

	@Override
	@Transactional
	public Long findAuthorId(String bookId, String deleteFlag) {
		return this.authorDAO.findAuthorId(bookId, deleteFlag);
	}
	
	@Override
	@Transactional
	public int addAuthor(List<BeanAuthor> listAuthor) {
		return this.authorDAO.addAuthor(listAuthor);
	}

}
