package com.framgia.users.dao;

import java.util.List;

import com.framgia.users.bean.BeanBookDetail;

/**
 * BookDetailDAO.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
public interface BookDetailDAO {

	public int addBookDetail(List<BeanBookDetail> beanBookDetail);
}
