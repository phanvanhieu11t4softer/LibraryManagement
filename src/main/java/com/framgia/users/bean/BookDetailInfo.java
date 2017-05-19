package com.framgia.users.bean;

public class BookDetailInfo {
	private int bookDetailId;
	private int authorsId;
	private String authorsName;
	private String sex;
	private String email;
	private String description;
	private String phone;
	private String birthday;
	private String address;

	public BookDetailInfo() {
	}

	public BookDetailInfo(int bookDetailId, int authorsId, String authorsName, String sex, String email,
			String description, String phone, String birthday, String address, BookInfo book) {
		super();
		this.bookDetailId = bookDetailId;
		this.authorsId = authorsId;
		this.authorsName = authorsName;
		this.sex = sex;
		this.email = email;
		this.description = description;
		this.phone = phone;
		this.birthday = birthday;
		this.address = address;
		this.book = book;
	}

	private AuthorInfo author;
	private BookInfo book;

	public BookDetailInfo(int bookDetailId, AuthorInfo author, BookInfo book) {
		super();
		this.bookDetailId = bookDetailId;
		this.author = author;
		this.book = book;
	}
	
	public BookInfo getBook() {
		return book;
	}

	public void setBook(BookInfo book) {
		this.book = book;
	}

	public int getBookDetailId() {
		return bookDetailId;
	}

	public void setBookDetailId(int bookDetailId) {
		this.bookDetailId = bookDetailId;
	}

	public int getAuthorsId() {
		return authorsId;
	}

	public void setAuthorsId(int authorsId) {
		this.authorsId = authorsId;
	}

	public String getAuthorsName() {
		return authorsName;
	}

	public void setAuthorsName(String authorsName) {
		this.authorsName = authorsName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
