package com.framgia.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framgia.users.bean.BeanBookDetail;
import com.framgia.users.dao.BookDetailDAO;

/**
 * BookDetailServiceImpl.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Service
public class BookDetailServiceImpl implements BookDetailService {
	
	@ Autowired
	BookDetailDAO bookDetailDAO;

	@Override
	@Transactional
	public int addBookDetail(List<BeanBookDetail> beanBookDetail) {
		return this.bookDetailDAO.addBookDetail(beanBookDetail);
	}

}
