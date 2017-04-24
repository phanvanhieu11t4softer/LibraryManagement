package com.framgia.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framgia.users.bean.BeanBook;
import com.framgia.users.dao.BookDAO;
import com.framgia.users.model.BookDetail;

/**
 * BookServiceImpl.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Service
public class BookServiceImpl implements BookService {
	
	@ Autowired
	BookDAO bookDAO;

	@Override
	@Transactional
	public int addBook(List<BeanBook> beanBook) {
		return this.bookDAO.addBook(beanBook);
	}

	@Override
	@Transactional
	public List<BookDetail> listBook() {
		return this.bookDAO.listBook();
	}

	@Override
	@Transactional
	public Long findBookId(String bookId, String deleteFlag) {
		return this.bookDAO.findBookId(bookId, deleteFlag);
	}

}
