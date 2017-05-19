package com.framgia.users.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.framgia.users.model.Author;
import com.framgia.users.model.ConstantModel;

/**
 * AuthorDAOImpl.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Repository
public class AuthorDAOImpl extends AbstractDao<Integer, Author> implements ConstantModel, AuthorDAO {
	
	// log
	private static final Logger logger = Logger.getLogger(BookDAOImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Author findAuthorId(String authorsId) {
		List<Author> authors = new ArrayList<Author>();

		authors = getOpenSession().createQuery("from Author where authorsId=:authorsId and deleteFlag=:delFlg")
				.setParameter("authorsId", Integer.parseInt(authorsId))
				.setParameter("delFlg", ConstantModel.DEL_FLG).list();

		if (authors.size() > 0) {

			return authors.get(0);
		} else {

			return null;
		}
	}
	
	@Override
	public void insertAuthor(Author author) {
		
		// Insert data into table Books
		getOpenSession().saveOrUpdate(author);
		logger.info("Insert success.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Author> listAuthor() {
		List<Author> authorList = getSession().createQuery("from Author where deleteFlag = '0'").list();
		return authorList;
	}

}
