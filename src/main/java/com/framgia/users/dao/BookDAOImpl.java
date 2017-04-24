package com.framgia.users.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.framgia.users.bean.BeanBook;
import com.framgia.users.model.BookDetail;
import com.framgia.users.util.Constant;

/**
 * BookDAOImpl.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Repository
public class BookDAOImpl implements BookDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(BookDAOImpl.class);

	@Autowired
    @Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}

	private DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
	
	@Override
	public int addBook(List<BeanBook> beanBooks) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		int result = 0;
		
		// Get name login
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nameUser = auth.getName();
        
        // Get system date
        Date date = new Date();
        String systemDate = dateFormat.format(date);
		try {
			tx = session.beginTransaction();
			for (BeanBook beanBook : beanBooks) {
				String sql = "INSERT INTO Books (Books.bookCode, Books.categoriesId, Books.publishersId, "
						+ "Books.name, Books.price, Books.statusBook, Books.numberBook, Books.numberPage, "
						+ "Books.deleteFlag, Books.dateCreate,Books.userCreate, Books.dateUpdate, "
						+ "Books.userUpdate) values ('" + beanBook.getBookCode() + "', '" + beanBook.getCategoriesId()
						+ "', '" + beanBook.getPublishersId() + "'," + "'" + beanBook.getName() + "', '"
						+ beanBook.getPrice() + "', '" + beanBook.getStatusBook() + "', '" + beanBook.getNumberBook() + "', '"
						+ beanBook.getNumberPage() + "', '" + Constant.NOT_DELETE + "', '" + systemDate + "'," + "'"
						+ nameUser + "', '" + systemDate + "', '" + nameUser + "')";
				SQLQuery selectQuery = session.createSQLQuery(sql);
				result = selectQuery.executeUpdate();
				
				if (result == 0) {
					if (tx != null) {
						tx.rollback();
					}
					return result;
				}
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
				e.printStackTrace();
			}
		} finally {
			session.close();
		}
		return result;
	}
	
	@Override
	public Long findBookId(String bookId, String deleteFlag) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(
			"select count(*) from Book book where book.bookId=:bookId and book.deleteFlag=:deleteFlag");
		query.setString("bookId", bookId);
		query.setString("deleteFlag", deleteFlag);
		
		return (Long)query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BookDetail> listBook() {
		Session session = this.sessionFactory.getCurrentSession();
		List<BookDetail> bookList = session.createQuery("from BookDetail where deleteFlag = '0'").list();
		for(BookDetail p : bookList){
			logger.info("Person List::"+p);
		}
		return bookList;
	}

}
