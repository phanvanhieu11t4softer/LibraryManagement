package com.framgia.users.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.framgia.users.model.Book;
import com.framgia.users.model.ConstantModel;

/**
 * BookDAOImpl.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Repository("bookDAO")
public class BookDAOImpl extends AbstractDao<Integer, Book> implements ConstantModel, BookDAO {
	
	// log
	private static final Logger logger = Logger.getLogger(BookDAOImpl.class);
	
	@Override
	public void insertBook(Book book) {
		
		// Insert data into table Books
		getOpenSession().saveOrUpdate(book);
		logger.info("Insert success.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Book findBookId(String bookId) {
		List<Book> book = new ArrayList<Book>();

		book = getOpenSession().createQuery("from Book where bookId=:bookId and deleteFlag=:delFlg")
				.setParameter("bookId", Integer.parseInt(bookId))
				.setParameter("delFlg", ConstantModel.DEL_FLG).list();

		if (book.size() > 0) {

			return book.get(0);
		} else {

			return null;
		}
	}
}
