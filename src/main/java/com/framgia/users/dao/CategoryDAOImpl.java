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

import com.framgia.users.bean.BeanCategory;
import com.framgia.users.model.Categories;
import com.framgia.users.util.Constant;

/**
 * CategoryDAOImpl.java
 * 
 * @version 19/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Repository
public class CategoryDAOImpl implements CategoryDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(CategoryDAOImpl.class);
	private DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);

	@Autowired
    @Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Categories> listCategory() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Categories> categoryList = session.createQuery("from Categories where deleteFlag = '0'").list();
		for(Categories p : categoryList){
			logger.info("Person List::"+p);
		}
		return categoryList;
	}
	
	@Override
	public Long findCategoryId(String categoryId, String categorySubId, String deleteFlag) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(
			"select count(*) from Categories categories where categories.categoriesId=:categoriesId and catSubId=:catSubId and categories.deleteFlag=:deleteFlag");
		query.setString("categoriesId", categoryId);
		query.setString("catSubId", categorySubId);
		query.setString("deleteFlag", deleteFlag);
		
		return (Long)query.uniqueResult();
	}
	
	@Override
	public Long findCatSubId(String catSubId, String deleteFlag) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(
			"select count(*) from Categories categories where categories.categoriesId=:categoriesId and categories.deleteFlag=:deleteFlag");
		query.setString("categoriesId", catSubId);
		query.setString("deleteFlag", deleteFlag);
		
		return (Long)query.uniqueResult();
	}
	
	@Override
	public Long findCategoryCode(String categoryCode, String deleteFlag) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(
			"select count(*) from Categories categories where categories.categoriesCode=:categoriesCode and categories.deleteFlag=:deleteFlag");
		query.setString("categoriesCode", categoryCode);
		query.setString("deleteFlag", deleteFlag);
		
		return (Long)query.uniqueResult();
	}
	
	@Override
	public int addCategories(List<BeanCategory> listCategory) {
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
			for (BeanCategory beanCategory : listCategory) {
				String sql = "INSERT INTO Categories (Categories.catSubId, Categories.categoriesCode, Categories.name, "
						+ "Categories.deleteFlag, Categories.dateCreate, Categories.userCreate, Categories.dateUpdate, "
						+ "Categories.userUpdate) values ('" + beanCategory.getCatSubId() + "', '" + beanCategory.getCategoriesCode() +
						"', '" + beanCategory.getName() + "', '" + Constant.NOT_DELETE + "', '" + systemDate + "'," + "'"
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
