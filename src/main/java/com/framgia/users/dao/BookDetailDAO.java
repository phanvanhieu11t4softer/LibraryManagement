package com.framgia.users.dao;

import java.util.List;

import com.framgia.users.model.BookDetail;

/**
 * BookDetailDAO.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
public interface BookDetailDAO {

	// Insert data into table BookDetails
	public void insertBookDetail(BookDetail bookDetail);

	// Find Author Name
	public List<BookDetail> findAuthorName(int id);
}
