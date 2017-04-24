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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.framgia.users.bean.BeanAuthor;
import com.framgia.users.util.Constant;

/**
 * AuthorDAOImpl.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Repository
public class AuthorDAOImpl implements AuthorDAO {
	
	@Autowired
    @Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}
	
	private DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);

	@Override
	public Long findAuthorId(String authorsId, String deleteFlag) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(
			"select count(*) from Author author where author.authorsId=:authorsId and author.deleteFlag=:deleteFlag");
		query.setString("authorsId", authorsId);
		query.setString("deleteFlag", deleteFlag);
		
		return (Long)query.uniqueResult();
	}

	
	@Override
	public int addAuthor(List<BeanAuthor> listAuthor) {
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
			for (BeanAuthor beanAuthor : listAuthor) {
				String sql = "INSERT INTO Authors (Authors.authorsName, Authors.sex, Authors.email, Authors.description, "
						+ "Authors.phone, Authors.birthday, Authors.address,"
						+ "Authors.deleteFlag, Authors.dateCreate,Authors.userCreate, Authors.dateUpdate, "
						+ "Authors.userUpdate) values ('" + beanAuthor.getAuthorsName() + "', '" + beanAuthor.getSex()
						+ "', '" + beanAuthor.getEmail() + "', '" + beanAuthor.getDescription() + "', '"
						+ beanAuthor.getPhone() + "', '" + beanAuthor.getBirthday() + "', '" + beanAuthor.getAddress()
						+ "', '" + Constant.NOT_DELETE + "', '" + systemDate + "'," + "'" + nameUser + "', '"
						+ systemDate + "', '" + nameUser + "')";
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
