package com.framgia.users.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.framgia.users.model.BorrowedDetails;

/**
 * BorrowedDetailDAOImpl.java
 * 
 * @version 18/05/2017
 * @author phan.van.hieu@framgia.com
 */
@Repository("borrowedDetailDao")
public class BorrowedDetailDAOImpl extends AbstractDao<Integer, BorrowedDetails> implements BorrowedDetailDAO {

	// log
	private static final Logger logger = Logger.getLogger(BorrowedDetailDAOImpl.class);

	@Override
	public void insertBorrowDetail(BorrowedDetails borroweds) {
		// Insert data into table Books
		getOpenSession().saveOrUpdate(borroweds);
		logger.info("Insert success.");
	}
}
