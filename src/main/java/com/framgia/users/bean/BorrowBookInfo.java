package com.framgia.users.bean;

public class BorrowBookInfo {
	private int bookId;
	private String nameBook;
	private String authorName;
	private String publisherName;
	private String categoryName;
	
	private String bookCode;
	private String numberBook;
	private String numberRest;
	
	public BorrowBookInfo() {
	}
	
	
	public String getBookCode() {
		return bookCode;
	}

	public void setBookCode(String bookCode) {
		this.bookCode = bookCode;
	}

	public String getNumberBook() {
		return numberBook;
	}

	public void setNumberBook(String numberBook) {
		this.numberBook = numberBook;
	}

	public String getNumberRest() {
		return numberRest;
	}

	public void setNumberRest(String numberRest) {
		this.numberRest = numberRest;
	}

	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getNameBook() {
		return nameBook;
	}
	public void setNameBook(String nameBook) {
		this.nameBook = nameBook;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getPublisherName() {
		return publisherName;
	}
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	

}
