package com.framgia.users.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.framgia.users.bean.BeanBookDetail;
import com.framgia.users.util.Constant;

/**
 * BookDetailDAOImpl.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Repository
public class BookDetailDAOImpl implements BookDetailDAO {
	

	@Autowired
    @Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}

	private DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
	
	@Override
	public int addBookDetail(List<BeanBookDetail> beanBookDetail) {
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
			for (BeanBookDetail beanBook : beanBookDetail) {
				String sql = "INSERT INTO BookDetails (BookDetails.bookId, BookDetails.authorsId, "
						+ "BookDetails.deleteFlag, BookDetails.dateCreate,BookDetails.userCreate, BookDetails.dateUpdate, "
						+ "BookDetails.userUpdate) values ('" + beanBook.getBookId() + "', '" + beanBook.getAuthorsId() +
						"', '" + Constant.NOT_DELETE + "', '" + systemDate + "'," + "'"
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
	
}
