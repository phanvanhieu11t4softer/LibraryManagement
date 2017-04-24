package com.framgia.users.bean;

/**
 * BeanBookDetail.java description infor data insert table Book
 * 
 * @version 22/04/2017
 * @author phan.van.hieu@framgia.com
 */
public class BeanBookDetail {
	private int bookId;
	private int authorsId;
	
	public BeanBookDetail() {
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public int getAuthorsId() {
		return authorsId;
	}

	public void setAuthorsId(int authorsId) {
		this.authorsId = authorsId;
	}
}
