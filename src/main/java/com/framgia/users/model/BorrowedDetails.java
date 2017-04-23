package com.framgia.users.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Users.java description table Users
 * 
 * @version 26/04/2017
 * @author vu.thi.tran.van@framgia.com
 */
@Entity
@Table(name = "Users", catalog = "Library")
public class BorrowedDetails {

	private Integer borrowedDetailId;
	private Borroweds borrowed;
	private Book book;
	private String status;
    private String deleteFlag;
    private Date dateCreate;
    private String userCreate;
    private Date dateUpdate;
    private String userUpdate;

	public BorrowedDetails() {
	}

	public BorrowedDetails(Integer borrowedDetailId, Borroweds borrowed, Book book, String status, String deleteFlag,
			Date dateCreate, String userCreate, Date dateUpdate, String userUpdate) {
		super();
		this.borrowedDetailId = borrowedDetailId;
		this.borrowed = borrowed;
		this.book = book;
		this.status = status;
		this.deleteFlag = deleteFlag;
		this.dateCreate = dateCreate;
		this.userCreate = userCreate;
		this.dateUpdate = dateUpdate;
		this.userUpdate = userUpdate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "borrowedDetailId", unique = true, nullable = false)
	public Integer getBorrowedDetailId() {
		return borrowedDetailId;
	}

	public void setBorrowedDetailId(Integer borrowedDetailId) {
		this.borrowedDetailId = borrowedDetailId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "borrowedId", nullable = false)
	public Borroweds getBorrowed() {
		return borrowed;
	}

	public void setBorrowed(Borroweds borrowed) {
		this.borrowed = borrowed;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bookId", nullable = false)
	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	@Column(name = "passWord", nullable = false, length = 1)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Column(name = "deleteFlag", nullable = false, length = 1)
	public String getDeleteFlag() {
		return deleteFlag;
	}
	
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateCreate", nullable = false)
	public Date getDateCreate() {
		return dateCreate;
	}
	
	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}
	
	@Column(name = "userCreate", nullable = false, length = 30)
	public String getUserCreate() {
		return userCreate;
	}
	
	public void setUserCreate(String userCreate) {
		this.userCreate = userCreate;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateUpdate", nullable = false)
	public Date getDateUpdate() {
		return dateUpdate;
	}
	
	public void setDateUpdate(Date dateUpdate) {
		this.dateUpdate = dateUpdate;
	}
	
	@Column(name = "userUpdate", nullable = false, length = 30)
	public String getUserUpdate() {
		return userUpdate;
	}
	public void setUserUpdate(String userUpdate) {
		this.userUpdate = userUpdate;
	}

}
