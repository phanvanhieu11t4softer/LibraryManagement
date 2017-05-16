package com.framgia.users.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.framgia.users.model.Book;
import com.framgia.users.model.ConstantModel;
import com.framgia.util.DateUtil;

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

	@Override
	public boolean update(Book mBook) {
		try {

			logger.info("Update book.");
			
			Criteria crit = getSession().createCriteria(Book.class);

			crit.add(Restrictions.eq("bookId", mBook.getBookId()));

			// Here is updated code
			ScrollableResults items = crit.scroll();

			while (items.next()) {
				Book mUpdBook = (Book) items.get(0);

				mUpdBook.setUserUpdate(mBook.getUserUpdate());
				mUpdBook.setDateUpdate(DateUtil.getDateNow());
				mUpdBook.setNumberRest(mUpdBook.getNumberRest() + 1);
				mUpdBook.setNumberBorrowed(mUpdBook.getNumberBorrowed() - 1);
				
				getSession().saveOrUpdate(mUpdBook);
				logger.info("Update end.");
			}

			return true;

		} catch (Exception e) {
			logger.error("Error update Book: " + e.getMessage());

			return false;
		}
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
