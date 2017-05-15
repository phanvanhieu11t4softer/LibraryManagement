package com.framgia.users.dao;

import com.framgia.users.model.Book;

/**
 * BookDAO.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
public interface BookDAO {

	// Insert data into table Books
	public void insertBook(Book book);

	// Find by idBook
	public Book findBookId(String bookId);
	
	boolean update(Book mBook);
	

}
