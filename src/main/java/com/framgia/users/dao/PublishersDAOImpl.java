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

import com.framgia.users.bean.BeanPublishers;
import com.framgia.users.model.Publishers;
import com.framgia.users.util.Constant;

/**
 * PublishersDAOImpl.java
 * 
 * @version 19/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Repository
public class PublishersDAOImpl implements PublishersDAO {

	private static final Logger logger = LoggerFactory.getLogger(PublishersDAOImpl.class);
	
	private DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);

	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Publishers> listPublishers() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Publishers> publishersList = session.createQuery("from Publishers where deleteFlag = '0'").list();
		for (Publishers p : publishersList) {
			logger.info("Person List::" + p);
		}
		return publishersList;
	}
	
	@Override
	public Long findPublishersId(String id, String deleteFlag) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(
			"select count(*) from Publishers publishers where publishers.publishersId=:publishersId and publishers.deleteFlag=:deleteFlag");
		query.setString("publishersId", id);
		query.setString("deleteFlag", deleteFlag);
		
		return (Long)query.uniqueResult();
	}
	
	@Override
	public int addPublishers(List<BeanPublishers> listPublishers) {
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
			for (BeanPublishers beanPublishers : listPublishers) {
				String sql = "INSERT INTO Publishers (Publishers.publishersName, Publishers.phone, Publishers.email, Publishers.address, "
						+ "Publishers.deleteFlag, Publishers.dateCreate, Publishers.userCreate, Publishers.dateUpdate, "
						+ "Publishers.userUpdate) values ('" + beanPublishers.getPublishersName() + "', '" + beanPublishers.getPhone() +
						"', '" + beanPublishers.getEmail() + "','" + beanPublishers.getAddress() + "', '" + Constant.NOT_DELETE + "', '" + systemDate + "'," + "'"
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
