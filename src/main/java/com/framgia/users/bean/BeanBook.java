package com.framgia.users.bean;

/**
 * BeanBook.java description infor data insert table Book
 * 
 * @version 22/04/2017
 * @author phan.van.hieu@framgia.com
 */
public class BeanBook {
	private String bookCode;
	private String categoriesId;
	private int publishersId;
	private String name;
	private Float price;
	private String statusBook;
	private Integer numberBook;
	private Integer numberPage;
	
	public BeanBook() {
	}
	
	public String getBookCode() {
		return bookCode;
	}
	public void setBookCode(String bookCode) {
		this.bookCode = bookCode;
	}
	public String getCategoriesId() {
		return categoriesId;
	}
	public void setCategoriesId(String categoriesId) {
		this.categoriesId = categoriesId;
	}
	public int getPublishersId() {
		return publishersId;
	}
	public void setPublishersId(int publishersId) {
		this.publishersId = publishersId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getStatusBook() {
		return statusBook;
	}
	public void setStatusBook(String statusBook) {
		this.statusBook = statusBook;
	}
	public Integer getNumberBook() {
		return numberBook;
	}
	public void setNumberBook(Integer numberBook) {
		this.numberBook = numberBook;
	}
	public Integer getNumberPage() {
		return numberPage;
	}
	public void setNumberPage(Integer numberPage) {
		this.numberPage = numberPage;
	}
	
}
