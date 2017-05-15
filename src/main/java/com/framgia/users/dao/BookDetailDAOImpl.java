package com.framgia.users.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.framgia.users.model.BookDetail;
import com.framgia.users.model.ConstantModel;

/**
 * BookDetailDAOImpl.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Repository
public class BookDetailDAOImpl extends AbstractDao<Integer, BookDetail> implements ConstantModel, BookDetailDAO {

	// log
	private static final Logger logger = Logger.getLogger(BookDAOImpl.class);

	@Override
	public void insertBookDetail(BookDetail bookDetail) {

		// Insert data into table Books
		getOpenSession().saveOrUpdate(bookDetail);
		logger.info("Insert success.");
	}

}
