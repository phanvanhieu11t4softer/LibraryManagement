package com.framgia.users.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
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

	@SuppressWarnings({ "unchecked", "finally" })
	@Override
	public List<BookDetail> findAuthorName(int id) {
		List<BookDetail> listResult = null;

		Session session = getSession();

		try {
			String sql = "from BookDetail where deleteFlag = :deleteFlag and bookId = :bookId";
			Query query = session.createQuery(sql);

			query.setParameter("deleteFlag", ConstantModel.DEL_FLG);
			query.setParameter("bookId", id);

			listResult = query.list();
			logger.info("Search list user end.");
		} catch (Exception e) {
			logger.error("Error search list user: " + e.getMessage());
		} finally {
			return listResult;

		}
	}

}
