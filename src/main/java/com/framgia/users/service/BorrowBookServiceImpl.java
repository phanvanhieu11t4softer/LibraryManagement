package com.framgia.users.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framgia.users.bean.AuthorInfo;
import com.framgia.users.bean.BorrowBookInfo;
import com.framgia.users.bean.CategoryInfo;
import com.framgia.users.bean.PublisherInfo;
import com.framgia.users.dao.AuthorDAO;
import com.framgia.users.dao.BookDAO;
import com.framgia.users.dao.BookDetailDAO;
import com.framgia.users.dao.BorrowedBookDAO;
import com.framgia.users.dao.BorrowedDetailDAO;
import com.framgia.users.dao.CategoryDAO;
import com.framgia.users.dao.PublishersDAO;
import com.framgia.users.dao.UserDAO;
import com.framgia.users.model.Author;
import com.framgia.users.model.Book;
import com.framgia.users.model.BookDetail;
import com.framgia.users.model.BorrowedDetails;
import com.framgia.users.model.Borroweds;
import com.framgia.users.model.Categories;
import com.framgia.users.model.ConstantModel;
import com.framgia.users.model.Publishers;
import com.framgia.users.model.Users;
import com.framgia.util.DateUtil;

/**
 * BorrowBookServiceImpl.java
 * 
 * @version 17/05/2017
 * @author phan.van.hieu@framgia.com
 * 
 */
@Service("borrowBookService")
public class BorrowBookServiceImpl implements BorrowBookService {

	@Autowired
	private CategoryDAO categoryDAO;

	@Autowired
	private BookDAO bookDAO;

	@Autowired
	private AuthorDAO authorDAO;

	@Autowired
	private PublishersDAO publishersDAO;

	@Autowired
	private BookDetailDAO bookDetailDAO;

	@Autowired
	private BorrowedBookDAO borrowedBookDAO;

	@Autowired
	private BorrowedDetailDAO borrowedDetailDao;

	@Autowired
	private UserDAO userDAO;

	// log
	private static final Logger logger = Logger.getLogger(BorrowBookServiceImpl.class);

	@Transactional
	@Override
	public List<CategoryInfo> listCategory() {
		List<CategoryInfo> categoryInfoList = new ArrayList<CategoryInfo>();
		List<Categories> categories = categoryDAO.listCategory();

		for (Categories item : categories) {

			CategoryInfo categoryInfo = new CategoryInfo();

			if (item.getCatSubId() != null) {
				categoryInfo.setCatSubId(item.getCatSubId());
			}
			categoryInfo.setName(item.getName());
			categoryInfo.setCategoriesId(item.getcategoriesId());

			categoryInfoList.add(categoryInfo);

		}
		return categoryInfoList;
	}

	@Transactional
	@Override
	public List<AuthorInfo> listAuthor() {
		List<AuthorInfo> authorInfoList = new ArrayList<AuthorInfo>();
		List<Author> authors = authorDAO.listAuthor();

		for (Author item : authors) {

			AuthorInfo authorInfo = new AuthorInfo();

			authorInfo.setAuthorsId(item.getAuthorsId());
			authorInfo.setAuthorsName(item.getAuthorsName());

			authorInfoList.add(authorInfo);

		}
		return authorInfoList;
	}

	@Transactional
	@Override
	public List<PublisherInfo> listPublisher() {
		List<PublisherInfo> publisherInfoList = new ArrayList<PublisherInfo>();
		List<Publishers> publishers = publishersDAO.listPublisher();

		for (Publishers item : publishers) {

			PublisherInfo publisherInfo = new PublisherInfo();

			publisherInfo.setPublishersId(item.getPublishersId());
			publisherInfo.setPublishersName(item.getPublishersName());

			publisherInfoList.add(publisherInfo);

		}

		return publisherInfoList;
	}

	@Transactional
	@Override
	public List<BorrowBookInfo> findByBookWithCondition(String txtName, int txtCategoryId, int txtPublisherId,
			int txtAuthorId) {
		List<Object[]> bookList = bookDAO.findByConditon(txtName, txtCategoryId, txtPublisherId, txtAuthorId);

		List<BorrowBookInfo> bookInfo = new ArrayList<BorrowBookInfo>();

		for (Object[] aRow : bookList) {
			BorrowBookInfo book = new BorrowBookInfo();
			book.setBookId(Integer.parseInt(aRow[0].toString()));
			book.setNameBook(aRow[1].toString());
			book.setCategoryName(aRow[2].toString());
			book.setPublisherName(aRow[3].toString());
			book.setBookCode(aRow[4].toString());
			book.setNumberBook(aRow[5].toString());
			book.setNumberRest(aRow[6].toString());

			List<BookDetail> authorNameList = bookDetailDAO.findAuthorName(Integer.parseInt(aRow[0].toString()));
			String authorName = null;
			int i = 0;
			for (BookDetail bookDetail : authorNameList) {
				if (i == 0) {
					authorName = bookDetail.getAuthor().getAuthorsName();
				} else {
					authorName = authorName + ", " + bookDetail.getAuthor().getAuthorsName();
				}

				i++;
			}

			book.setAuthorName(authorName);
			System.out.println("ID BOOK  " + aRow[0].toString());

			bookInfo.add(book);
		}

		return bookInfo;
	}

	@Transactional
	@Override
	public BorrowBookInfo getBookInfo(int id) {
		BorrowBookInfo bookInfo = new BorrowBookInfo();
		Book book = bookDAO.findBookId(id);
		bookInfo.setBookId(id);
		bookInfo.setNameBook(book.getName());
		bookInfo.setCategoryName(book.getCategories().getName());
		bookInfo.setPublisherName(book.getPublishers().getPublishersName());

		List<BookDetail> bookId = bookDetailDAO.findAuthorName(id);

		String authorName = null;
		int i = 0;
		for (BookDetail aRow : bookId) {
			if (i == 0) {
				authorName = aRow.getAuthor().getAuthorsName();
			} else {
				authorName = authorName + ", " + aRow.getAuthor().getAuthorsName();
			}

			i++;
		}

		bookInfo.setAuthorName(authorName);

		return bookInfo;
	}

	@Transactional
	@Override
	public int createRequest(List<BorrowBookInfo> cart, String userName, String dateBorrow, String dateReturn) {
		Borroweds borrowRequest = new Borroweds();
		Date dateCreate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dateCreate = DateUtil.getDateNow();
		} catch (ParseException e) {
			logger.error("ERROR GET DATE: ", e);
		}
		Borroweds borrowed = borrowedBookDAO.getLastRecord();

		// Set data BorrowBook Code
		if (borrowed == null) {
			borrowRequest.setBorrowedCode("BB" + 1);
		} else {
			borrowRequest.setBorrowedCode("BB" + (borrowed.getBorrowedId() + 1));
		}
		Users users = userDAO.findByUserName(userName);

		Users user = new Users();
		user.setUserId(users.getUserId());
		borrowRequest.setUser(user);

		try {
			borrowRequest.setDIntendBorrowed(sdf.parse(dateBorrow));
			borrowRequest.setDIntendArrived(sdf.parse(dateReturn));
		} catch (ParseException e) {
			logger.info("DATE ERROR: " + e.getMessage());
		}

		borrowRequest.setStatus(ConstantModel.BOR_STATUS_REQUEST);
		borrowRequest.setUserCreate(userName);
		borrowRequest.setUserUpdate(userName);
		borrowRequest.setDateCreate(dateCreate);
		borrowRequest.setDateUpdate(dateCreate);
		borrowRequest.setDeleteFlag(ConstantModel.DEL_FLG);

		try {
			int borrowBookId = borrowedBookDAO.insertBorrowBook(borrowRequest);
			BorrowedDetails borrowedDetail = null;
			for (BorrowBookInfo borrowBookInfo : cart) {
				borrowedDetail = new BorrowedDetails();

				Book book = new Book();
				book.setBookId(borrowBookInfo.getBookId());
				borrowedDetail.setBook(book);
				
				Borroweds borrow = new Borroweds();
				borrow.setBorrowedId(borrowBookId);
				borrowedDetail.setBorroweds(borrow);

				borrowedDetail.setStatus(ConstantModel.BOR_DET_STATUS_WAIT);
				borrowedDetail.setUserCreate(userName);
				borrowedDetail.setUserUpdate(userName);
				borrowedDetail.setDateCreate(dateCreate);
				borrowedDetail.setDateUpdate(dateCreate);
				borrowedDetail.setDeleteFlag(ConstantModel.DEL_FLG);

				borrowedDetailDao.insertBorrowDetail(borrowedDetail);
			}
		} catch (Exception e) {
			logger.error("Error Insert Permissions: " + e.getMessage());

			return 0;
		}

		return 1;
	}

}
