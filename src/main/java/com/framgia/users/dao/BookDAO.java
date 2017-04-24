package com.framgia.users.dao;

import java.util.List;

import com.framgia.users.bean.BeanBook;
import com.framgia.users.model.BookDetail;

/**
 * BookDAO.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
public interface BookDAO {

	public int addBook(List<BeanBook> beanBooks);
	public List<BookDetail> listBook();
	public Long findBookId(String bookId, String deleteFlag);
}
