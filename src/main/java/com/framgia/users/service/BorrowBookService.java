package com.framgia.users.service;

import java.util.List;

import com.framgia.users.bean.AuthorInfo;
import com.framgia.users.bean.BorrowBookInfo;
import com.framgia.users.bean.CategoryInfo;
import com.framgia.users.bean.PublisherInfo;

/**
 * BorrowBookService.java
 * 
 * @version 17/05/2017
 * @author phan.van.hieu@framgia.com
 * 
 */
public interface BorrowBookService {

	// Find list category
	List<CategoryInfo> listCategory();

	// Find conditon book
	List<BorrowBookInfo> findByBookWithCondition(String txtName, int txtCategoryId, int txtPublisherId,
			int txtAuthorId);
	
	// Find list Author
	List<AuthorInfo> listAuthor();

	// Find list Publisher
	List<PublisherInfo> listPublisher();

	// Get List Book
	BorrowBookInfo getBookInfo(int id);

	// 
	int createRequest(List<BorrowBookInfo> cart, String userName, String dateBorrow, String dateReturn);

}
