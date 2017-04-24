package com.framgia.users.service;

import java.util.List;

import com.framgia.users.bean.BeanBook;
import com.framgia.users.model.BookDetail;

/**
 * BookService.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
public interface BookService {

	public int addBook(List<BeanBook> beanBook);
	public Long findBookId(String bookId, String deleteFlag);
	public List<BookDetail> listBook();
	
}
