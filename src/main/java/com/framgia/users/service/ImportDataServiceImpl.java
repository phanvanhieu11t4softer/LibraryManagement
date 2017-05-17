package com.framgia.users.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.framgia.users.bean.ErrorInfo;
import com.framgia.users.bean.FileFormInfo;
import com.framgia.users.dao.AuthorDAO;
import com.framgia.users.dao.BookDAO;
import com.framgia.users.dao.BookDetailDAO;
import com.framgia.users.dao.CategoryDAO;
import com.framgia.users.dao.PermissionDAO;
import com.framgia.users.dao.PublishersDAO;
import com.framgia.users.dao.UserDAO;
import com.framgia.users.model.Author;
import com.framgia.users.model.Book;
import com.framgia.users.model.BookDetail;
import com.framgia.users.model.Categories;
import com.framgia.users.model.ConstantModel;
import com.framgia.users.model.Permissions;
import com.framgia.users.model.Publishers;
import com.framgia.users.model.Users;
import com.framgia.util.Constant;
import com.framgia.util.DateUtil;
import com.framgia.util.Helpers;
import com.opencsv.CSVReader;

/**
 * ImportDataServiceImpl.java
 * 
 * @version 03/05/2017
 * @author phan.van.hieu@framgia.com
 * 
 */
@Service("importDataService")
public class ImportDataServiceImpl implements ImportDataService {

	@Autowired
	private CategoryDAO categoryDAO;

	@Autowired
	private PublishersDAO publishersDAO;

	@Autowired
	private BookDAO bookDAO;

	@Autowired
	private AuthorDAO authorDAO;

	@Autowired
	private BookDetailDAO bookDetailDAO;

	@Autowired
	private PermissionDAO permissionDao;

	@Autowired
	private UserDAO userDao;

	@Autowired
	MessageSource messageSource;

	// log
	private static final Logger logger = Logger.getLogger(ImportDataServiceImpl.class);

	@Override
	public String checkFormatFile(FileFormInfo dataImportBean) {

		// Convert MultipartFile to File
		File fileImport = Helpers.convert(dataImportBean.getFileImport());
		CSVReader csvReader = null;
		List<String[]> listData = null;

		try {
			csvReader = new CSVReader(new FileReader(fileImport));
			listData = csvReader.readAll();
		} catch (FileNotFoundException e) {
			logger.error("ERROR FILE NOT FOUND: ", e);
		} catch (IOException e) {
			logger.error("ERROR IOEXCEPTION: ", e);
		}

		Iterator<String[]> iterator = listData.iterator();
		MultipartFile fileUpload = dataImportBean.getFileImport();
		String[] header;

		switch (dataImportBean.getNameTable()) {

		// Table Books
		case Constant.TABLE_BOOK:
			if (!Constant.NAME_FILE_DATA_BOOK.equals(fileUpload.getOriginalFilename())) {
				return messageSource.getMessage("file_incorrect", null, Locale.getDefault());
			}

			// Next line Header
			header = iterator.next();

			// Check format header File
			if (header.length != 8) {
				return messageSource.getMessage("file_format_incorrect", null, Locale.getDefault());
			} else if (!((Constant.BOOK_CODE.equals(header[0])) && (Constant.CATEGORIES_ID.equals(header[1])
					&& (Constant.PUBLISHERS_ID.equals(header[2])) && (Constant.NAME.equals(header[3]))
					&& (Constant.PRICE.equals(header[4])) && (Constant.STATUS_BOOK.equals(header[5]))
					&& (Constant.NUMBER_BOOK.equals(header[6])) && (Constant.NUMBER_PAGE.equals(header[7]))))) {
				return messageSource.getMessage("file_format_incorrect", null, Locale.getDefault());
			}

			// Check data file
			if (listData.size() == 0) {
				return messageSource.getMessage("file_empty", null, Locale.getDefault())
						.replace(Constant.DEFAULT_VALUE_MSG, fileUpload.getOriginalFilename());
			}
			break;

		// Table BookDetails
		case Constant.TABLE_BOOKDETAILS:
			if (!Constant.NAME_FILE_DATA_BOOK_DETAIL.equals(fileUpload.getOriginalFilename())) {
				return messageSource.getMessage("file_incorrect", null, Locale.getDefault());
			}

			// Next line Header
			header = iterator.next();

			if (header.length != 2) {
				return messageSource.getMessage("file_format_incorrect", null, Locale.getDefault());
			} else if (!(Constant.BOOK_ID.equals(header[0]) && Constant.AUTHOR_ID.equals(header[1]))) {
				return messageSource.getMessage("file_format_incorrect", null, Locale.getDefault());
			}

			if (listData.size() == 0) {
				return messageSource.getMessage("file_empty", null, Locale.getDefault())
						.replace(Constant.DEFAULT_VALUE_MSG, fileUpload.getOriginalFilename());
			}
			break;

		// Table TABLE_CATEGORIES
		case Constant.TABLE_CATEGORIES:
			if (!Constant.NAME_FILE_DATA_CATEGORY.equals(fileUpload.getOriginalFilename())) {
				return messageSource.getMessage("file_incorrect", null, Locale.getDefault());
			}

			// Next line Header
			header = iterator.next();

			if (header.length != 3) {
				return messageSource.getMessage("file_format_incorrect", null, Locale.getDefault());

			} else if (!(Constant.CAT_SUB_ID.equals(header[0]) && Constant.CATEGORIES_CODE.equals(header[1])
					&& Constant.CATEGORY_NAME.equals(header[2]))) {
				return messageSource.getMessage("file_format_incorrect", null, Locale.getDefault());
			}
			if (listData.size() == 0) {
				return messageSource.getMessage("file_empty", null, Locale.getDefault())
						.replace(Constant.DEFAULT_VALUE_MSG, fileUpload.getOriginalFilename());
			}

			// Table TABLE_AUTHORS
		case Constant.TABLE_AUTHORS:
			if (!Constant.NAME_FILE_DATA_AUTHOR.equals(fileUpload.getOriginalFilename())) {
				return messageSource.getMessage("file_incorrect", null, Locale.getDefault());
			}

			// Next line Header
			header = iterator.next();

			if (header.length != 7) {
				return messageSource.getMessage("file_format_incorrect", null, Locale.getDefault());

			} else if (!(Constant.AUTHORS_NAME.equals(header[0]) && Constant.SEX.equals(header[1])
					&& Constant.EMAIL.equals(header[2]) && Constant.DESCRIPTION.equals(header[3])
					&& Constant.PHONE.equals(header[4]) && Constant.BIRTH_DAY.equals(header[5])
					&& Constant.ADDRESS.equals(header[6]))) {

				return messageSource.getMessage("file_format_incorrect", null, Locale.getDefault());
			}
			if (listData.size() == 0) {
				return messageSource.getMessage("file_empty", null, Locale.getDefault())
						.replace(Constant.DEFAULT_VALUE_MSG, fileUpload.getOriginalFilename());
			}

			// Table TABLE_PUBLISHERS
		case Constant.TABLE_PUBLISHERS:
			if (!Constant.NAME_FILE_DATA_PUBLISHERS.equals(fileUpload.getOriginalFilename())) {
				return messageSource.getMessage("file_incorrect", null, Locale.getDefault());
			}

			// Next line Header
			header = iterator.next();

			// Check format header File
			if (header.length != 4) {
				return messageSource.getMessage("file_format_incorrect", null, Locale.getDefault());

			} else if (!(Constant.PUBLISHERS_NAME.equals(header[0]) && Constant.PHONE.equals(header[1])
					&& Constant.EMAIL.equals(header[2]) && Constant.ADDRESS.equals(header[3]))) {

				return messageSource.getMessage("file_format_incorrect", null, Locale.getDefault());
			}
			if (listData.size() == 0) {
				return messageSource.getMessage("file_empty", null, Locale.getDefault())
						.replace(Constant.DEFAULT_VALUE_MSG, fileUpload.getOriginalFilename());
			}

			// Table TABLE_PUBLISHERS
		case Constant.TABLE_USERS:
			if (!Constant.NAME_FILE_DATA_USERS.equals(fileUpload.getOriginalFilename())) {
				return messageSource.getMessage("file_incorrect", null, Locale.getDefault());
			}

			// Next line Header
			header = iterator.next();

			// Check format header File
			if (header.length != 9) {
				return messageSource.getMessage("file_format_incorrect", null, Locale.getDefault());

			} else if (!(Constant.PERMISSIONS_ID.equals(header[0]) && Constant.USER_NAME.equals(header[1])
					&& Constant.PASS_WORD.equals(header[2]) && Constant.BIRTH_DATE.equals(header[3])
					&& Constant.NAME.equals(header[4]) && Constant.ADDRESS.equals(header[5])
					&& Constant.PHONE.equals(header[6]) && Constant.SEX.equals(header[7])
					&& Constant.EMAIL.equals(header[8]))) {

				return messageSource.getMessage("file_format_incorrect", null, Locale.getDefault());
			}
			if (listData.size() == 0) {
				return messageSource.getMessage("file_empty", null, Locale.getDefault())
						.replace(Constant.DEFAULT_VALUE_MSG, fileUpload.getOriginalFilename());
			}

			// Table TABLE_PUBLISHERS
		case Constant.TABLE_PERMISSIONS:
			if (!Constant.NAME_FILE_DATA_PERMISSIONS.equals(fileUpload.getOriginalFilename())) {
				return messageSource.getMessage("file_incorrect", null, Locale.getDefault());
			}

			// Next line Header
			header = iterator.next();

			// Check format header File
			if (header.length != 2) {
				return messageSource.getMessage("file_format_incorrect", null, Locale.getDefault());

			} else if (!(Constant.PERMISSION_NAME.equals(header[0]) && Constant.DESCRIPTION.equals(header[1]))) {
				return messageSource.getMessage("file_format_incorrect", null, Locale.getDefault());
			}
			if (listData.size() == 0) {
				return messageSource.getMessage("file_empty", null, Locale.getDefault())
						.replace(Constant.DEFAULT_VALUE_MSG, fileUpload.getOriginalFilename());
			}
		}

		return null;

	}

	@Override
	public List<ErrorInfo> checkDataFile(FileFormInfo dataImportBean) {

		// Convert MultipartFile to File
		File fileImport = Helpers.convert(dataImportBean.getFileImport());
		List<ErrorInfo> listErrorLog = new ArrayList<ErrorInfo>();
		CSVReader csvReader = null;
		List<String[]> listData = null;

		try {
			csvReader = new CSVReader(new FileReader(fileImport));
			listData = csvReader.readAll();
		} catch (FileNotFoundException e) {
			logger.error("ERROR FILE NOT FOUND: ", e);
		} catch (IOException e) {
			logger.error("ERROR IOEXCEPTION: ", e);
		}

		Iterator<String[]> iterator = listData.iterator();

		int line = 2;
		ErrorInfo errorLog;
		switch (dataImportBean.getNameTable()) {

		// Table Books
		case Constant.TABLE_BOOK:
			iterator.next();
			while (iterator.hasNext()) {
				// Array Book information
				String[] book = iterator.next();

				if (book.length != 8) {
					// Gen errorLog
					errorLog = new ErrorInfo();

					// Set data list listErrorLog
					errorLog.setError(messageSource.getMessage("line_format_incorrect", null, Locale.getDefault()));
					errorLog.setNumberLine(line);
					listErrorLog.add(errorLog);
				} else {
					// Check null column Book Code
					if (Helpers.checkNullColumn(book[0], Constant.BOOK_CODE, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(book[0], Constant.BOOK_CODE, line));
					}

					// Check null column CategoriesID
					if (Helpers.checkNullColumn(book[1], Constant.CATEGORIES_ID, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(book[1], Constant.CATEGORIES_ID, line));
					} else {
						// Check Integer column CategoriesID
						if (Helpers.checkIntegerColumn(book[1], Constant.CATEGORIES_ID, line) != null) {
							listErrorLog.add(Helpers.checkIntegerColumn(book[1], Constant.CATEGORIES_ID, line));
						} else {
							// Check key id CategoryId
							if (categoryDAO.findCategoryId(book[1]) == null) {

								// Gen errorLog
								errorLog = new ErrorInfo();

								// Set data list listErrorLog
								errorLog.setColumn(Constant.CATEGORIES_ID);
								errorLog.setError(
										messageSource.getMessage("category_id_not_exist", null, Locale.getDefault()));
								errorLog.setNumberLine(line);
								listErrorLog.add(errorLog);
							}
						}
					}

					// Check null column PublishersID
					if (Helpers.checkNullColumn(book[2], Constant.PUBLISHERS_ID, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(book[2], Constant.PUBLISHERS_ID, line));
					} else {
						// Check format Integer column PUBLISHERS_ID
						if (Helpers.checkIntegerColumn(book[2], Constant.PUBLISHERS_ID, line) != null) {
							listErrorLog.add(Helpers.checkIntegerColumn(book[2], Constant.PUBLISHERS_ID, line));
						} else {
							// Check key id Publishers
							if (publishersDAO.findPublishersId(book[2]) == null) {

								// Gen errorLog
								errorLog = new ErrorInfo();

								// Set data list listErrorLog
								errorLog.setColumn(Constant.PUBLISHERS_ID);
								errorLog.setError(
										messageSource.getMessage("publishers_id_not_exist", null, Locale.getDefault()));
								errorLog.setNumberLine(line);
								listErrorLog.add(errorLog);
							}
						}
					}

					// Check null column StatusBook
					if (Helpers.checkNullColumn(book[5], Constant.STATUS_BOOK, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(book[5], Constant.STATUS_BOOK, line));
					} else {
						if (!ConstantModel.BOO_STATUS_GOOD.equals(book[5])
								&& !ConstantModel.BOO_STATUS_BAD.equals(book[5])) {
							// Gen errorLog
							errorLog = new ErrorInfo();

							// Set data list listErrorLog
							errorLog.setColumn(Constant.STATUS_BOOK);
							errorLog.setError(messageSource.getMessage("value_status_book", null, Locale.getDefault()));
							errorLog.setNumberLine(line);
							listErrorLog.add(errorLog);
						}
					}

					// Check format Float column PRICE
					if (!Helpers.isEmpty(book[4]) && Helpers.checkFloatColumn(book[4], Constant.PRICE, line) != null) {
						listErrorLog.add(Helpers.checkFloatColumn(book[4], Constant.PRICE, line));
					}

					// Check format Float column NUMBER_BOOK
					if (Helpers.checkNullColumn(book[6], Constant.NUMBER_BOOK, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(book[6], Constant.NUMBER_BOOK, line));
					} else {
						if (Helpers.checkIntegerColumn(book[6], Constant.NUMBER_BOOK, line) != null) {
							listErrorLog.add(Helpers.checkIntegerColumn(book[6], Constant.NUMBER_BOOK, line));
						}
					}

					// Check format Float column NUMBER_PAGE
					if (Helpers.checkNullColumn(book[7], Constant.NUMBER_PAGE, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(book[6], Constant.NUMBER_PAGE, line));
					} else {
						if (Helpers.checkIntegerColumn(book[7], Constant.NUMBER_PAGE, line) != null) {
							listErrorLog.add(Helpers.checkIntegerColumn(book[7], Constant.NUMBER_PAGE, line));
						}
					}

					// Check maxlength column BOOK_CODE
					if (Helpers.checkMaxLength(book[0], Constant.BOOK_CODE, line, 10) != null) {
						listErrorLog.add(Helpers.checkMaxLength(book[0], Constant.BOOK_CODE, line, 10));
					}

					// Check maxlength column NAME
					if (Helpers.checkMaxLength(book[2], Constant.NAME, line, 100) != null) {
						listErrorLog.add(Helpers.checkMaxLength(book[2], Constant.NAME, line, 100));
					}

				}

				line++;
			}
			// Table BookDetails
		case Constant.TABLE_BOOKDETAILS:
			iterator.next();
			while (iterator.hasNext()) {
				// Array BookDetail information
				String[] bookDetail = iterator.next();

				if (bookDetail.length != 2) {
					// Gen errorLog
					errorLog = new ErrorInfo();

					// Set data list listErrorLog
					errorLog.setError(Constant.ERROR_LINE_FORMAT);
					errorLog.setNumberLine(line);
					listErrorLog.add(errorLog);
				} else {
					// Check null column BOOK_ID
					if (Helpers.checkNullColumn(bookDetail[0], Constant.BOOK_ID, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(bookDetail[0], Constant.BOOK_ID, line));
					} else {
						// Check format Integer column BOOK_ID
						if (Helpers.checkIntegerColumn(bookDetail[0], Constant.BOOK_ID, line) != null) {
							listErrorLog.add(Helpers.checkIntegerColumn(bookDetail[0], Constant.BOOK_ID, line));
						} else {
							// Check key id Book
							if (bookDAO.findBookId(bookDetail[0]) == null) {

								// Gen errorLog
								errorLog = new ErrorInfo();

								// Set data list listErrorLog
								errorLog.setColumn(Constant.BOOK_ID);
								errorLog.setError(
										messageSource.getMessage("book_id_not_exist", null, Locale.getDefault()));
								errorLog.setNumberLine(line);
								listErrorLog.add(errorLog);
							}
						}
					}

					// Check null column AUTHOR_ID
					if (Helpers.checkNullColumn(bookDetail[1], Constant.AUTHOR_ID, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(bookDetail[1], Constant.AUTHOR_ID, line));
					} else {
						// Check format Integer column AUTHOR_ID
						if (Helpers.checkIntegerColumn(bookDetail[1], Constant.AUTHOR_ID, line) != null) {
							listErrorLog.add(Helpers.checkIntegerColumn(bookDetail[1], Constant.AUTHOR_ID, line));
						} else {
							// Check key id Book
							if (authorDAO.findAuthorId(bookDetail[1]) == null) {

								// Gen errorLog
								errorLog = new ErrorInfo();

								// Set data list listErrorLog
								errorLog.setColumn(Constant.AUTHOR_ID);
								errorLog.setError(
										messageSource.getMessage("author_id_not_exist", null, Locale.getDefault()));
								errorLog.setNumberLine(line);
								listErrorLog.add(errorLog);
							}
						}
					}
				}

				line++;
			}

			// Table TABLE_CATEGORIES
		case Constant.TABLE_CATEGORIES:
			iterator.next();
			while (iterator.hasNext()) {
				String[] category = iterator.next();

				if (category.length != 3) {
					// Gen errorLog
					errorLog = new ErrorInfo();

					// Set data list listErrorLog
					errorLog.setError(Constant.ERROR_LINE_FORMAT);
					errorLog.setNumberLine(line);
					listErrorLog.add(errorLog);
				} else {
					// Check null column CATEGORIES_CODE
					if (Helpers.checkNullColumn(category[1], Constant.CATEGORIES_CODE, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(category[1], Constant.CATEGORIES_CODE, line));
					} else {
						// Check maxlength column CATEGORIES_CODE
						if (Helpers.checkMaxLength(category[1], Constant.CATEGORIES_CODE, line, 10) != null) {
							listErrorLog.add(Helpers.checkMaxLength(category[1], Constant.CATEGORIES_CODE, line, 10));
						} else {
							// Check bookCode Category
							if (categoryDAO.findCategoryCode(category[1]) != null) {

								// Gen errorLog
								errorLog = new ErrorInfo();

								// Set data list listErrorLog
								errorLog.setColumn(Constant.CATEGORIES_CODE);
								errorLog.setError(Constant.ERROR_CATEGORY_CODE);
								errorLog.setNumberLine(line);
								listErrorLog.add(errorLog);
							}
						}
					}

					// Check null column AUTHOR_ID
					if (Helpers.checkNullColumn(category[2], Constant.CATEGORY_NAME, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(category[2], Constant.CATEGORY_NAME, line));
					} else {
						// Check maxlength column CATEGORY_NAME
						if (Helpers.checkMaxLength(category[2], Constant.CATEGORY_NAME, line, 100) != null) {
							listErrorLog.add(Helpers.checkMaxLength(category[2], Constant.CATEGORY_NAME, line, 100));
						}
					}

					// Check null column CAT_SUB_ID
					if (Helpers.checkNullColumn(category[0], Constant.CAT_SUB_ID, line) == null) {
						// Check format Integer column CAT_SUB_ID
						if (Helpers.checkIntegerColumn(category[0], Constant.CAT_SUB_ID, line) != null
								&& !Helpers.isEmpty(category[0])) {
							listErrorLog.add(Helpers.checkIntegerColumn(category[0], Constant.CAT_SUB_ID, line));
						} else {
							// Check key catSubId Category
							if (categoryDAO.findCategoryId(category[0]) == null) {

								// Gen errorLog
								errorLog = new ErrorInfo();

								// Set data list listErrorLog
								errorLog.setColumn(Constant.CAT_SUB_ID);
								errorLog.setError(Constant.ERROR_KEY_CAT_SUB_ID);
								errorLog.setNumberLine(line);
								listErrorLog.add(errorLog);
							}
						}
					}

				}

				line++;
			}

			// Table TABLE_CATEGORIES
		case Constant.TABLE_AUTHORS:
			iterator.next();
			while (iterator.hasNext()) {
				String[] author = iterator.next();
				if (author.length != 7) {
					// Gen errorLog
					errorLog = new ErrorInfo();

					// Set data list listErrorLog
					errorLog.setError(Constant.ERROR_LINE_FORMAT);
					errorLog.setNumberLine(line);
					listErrorLog.add(errorLog);
				} else {
					// Check null column AUTHORS_NAME
					if (Helpers.checkNullColumn(author[0], Constant.AUTHORS_NAME, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(author[0], Constant.AUTHORS_NAME, line));
					} else {
						// Check maxlength column AUTHORS_NAME
						if (Helpers.checkMaxLength(author[0], Constant.AUTHORS_NAME, line, 100) != null) {
							listErrorLog.add(Helpers.checkMaxLength(author[0], Constant.AUTHORS_NAME, line, 100));
						}
					}

					// Check null column SEX
					if (Helpers.checkNullColumn(author[1], Constant.SEX, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(author[1], Constant.SEX, line));
					} else {
						if (!Constant.DEFAULT_VALUE_0.equals(author[1])
								&& !Constant.DEFAULT_VALUE_1.equals(author[1])) {
							// Gen errorLog
							errorLog = new ErrorInfo();

							// Set data list listErrorLog
							errorLog.setColumn(Constant.SEX);
							errorLog.setError(Constant.ERROR_CHAR01.replace(Constant.DEFAULT_VALUE_MSG, Constant.SEX));
							errorLog.setNumberLine(line);
							listErrorLog.add(errorLog);
						}
					}

					// Check null column EMAIL
					if (Helpers.checkNullColumn(author[2], Constant.EMAIL, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(author[2], Constant.EMAIL, line));
					} else {
						// Check maxlength column EMAIL
						if (Helpers.checkMaxLength(author[2], Constant.EMAIL, line, 30) != null) {
							listErrorLog.add(Helpers.checkMaxLength(author[2], Constant.EMAIL, line, 30));
						}
					}

					// Check maxlength column phone
					if (Helpers.checkMaxLength(author[4], Constant.PHONE, line, 11) != null) {
						listErrorLog.add(Helpers.checkMaxLength(author[4], Constant.EMAIL, line, 11));
					}

					// Check maxlength column phone
					if (Helpers.checkMaxLength(author[6], Constant.ADDRESS, line, 100) != null) {
						listErrorLog.add(Helpers.checkMaxLength(author[6], Constant.ADDRESS, line, 100));
					}

					// Check format date column BirthDay
					if (!Helpers.isEmpty(author[5]) && !Helpers.isDateValid(author[5])) {
						// Gen errorLog
						errorLog = new ErrorInfo();

						// Set data list listErrorLog
						errorLog.setColumn(Constant.BIRTH_DAY);
						errorLog.setError(Constant.ERROR_DATE);
						errorLog.setNumberLine(line);
						listErrorLog.add(errorLog);
					}
				}

				line++;
			}

			// Table TABLE_PUBLISHERS
		case Constant.TABLE_PUBLISHERS:
			iterator.next();
			while (iterator.hasNext()) {
				String[] publishers = iterator.next();
				if (publishers.length != 4) {
					// Gen errorLog
					errorLog = new ErrorInfo();

					// Set data list listErrorLog
					errorLog.setError(Constant.ERROR_LINE_FORMAT);
					errorLog.setNumberLine(line);
					listErrorLog.add(errorLog);
				} else {
					// Check null column PUBLISHERS_NAME
					if (Helpers.checkNullColumn(publishers[0], Constant.PUBLISHERS_NAME, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(publishers[0], Constant.PUBLISHERS_NAME, line));
					} else {
						// Check maxlength column PUBLISHERS_NAME
						if (Helpers.checkMaxLength(publishers[0], Constant.PUBLISHERS_NAME, line, 100) != null) {
							listErrorLog
									.add(Helpers.checkMaxLength(publishers[0], Constant.PUBLISHERS_NAME, line, 100));
						}
					}

					// Check null column PUBLISHERS_NAME
					if (Helpers.checkNullColumn(publishers[1], Constant.PHONE, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(publishers[1], Constant.PHONE, line));
					} else {
						// Check maxlength column PHONE
						if (Helpers.checkMaxLength(publishers[1], Constant.PHONE, line, 11) != null) {
							listErrorLog.add(Helpers.checkMaxLength(publishers[1], Constant.PUBLISHERS_NAME, line, 11));
						}
					}

					// Check null column PUBLISHERS_NAME
					if (Helpers.checkNullColumn(publishers[2], Constant.EMAIL, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(publishers[2], Constant.EMAIL, line));
					} else {
						// Check maxlength column EMAIL
						if (Helpers.checkMaxLength(publishers[2], Constant.EMAIL, line, 30) != null) {
							listErrorLog.add(Helpers.checkMaxLength(publishers[2], Constant.PUBLISHERS_NAME, line, 30));
						}
					}

				}

				line++;
			}

			// Table TABLE_USERS
		case Constant.TABLE_USERS:
			iterator.next();
			while (iterator.hasNext()) {
				String[] user = iterator.next();
				if (user.length != 9) {
					// Gen errorLog
					errorLog = new ErrorInfo();

					// Set data list listErrorLog
					errorLog.setError(Constant.ERROR_LINE_FORMAT);
					errorLog.setNumberLine(line);
					listErrorLog.add(errorLog);
				} else {
					// Check null column PERMISSIONS_ID
					if (Helpers.checkNullColumn(user[0], Constant.PERMISSIONS_ID, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(user[0], Constant.PERMISSIONS_ID, line));
					} else {
						// Check format Integer column PERMISSIONS_ID
						if (Helpers.checkIntegerColumn(user[0], Constant.PERMISSIONS_ID, line) != null) {
							listErrorLog.add(Helpers.checkIntegerColumn(user[0], Constant.PERMISSIONS_ID, line));
						} else {
							// Check key id PERMISSIONS_ID
							if (permissionDao.findPermissionId(user[0]) == null) {

								// Gen errorLog
								errorLog = new ErrorInfo();

								// Set data list listErrorLog
								errorLog.setColumn(Constant.PERMISSIONS_ID);
								errorLog.setError(
										messageSource.getMessage("permission_id_not_exist", null, Locale.getDefault()));
								errorLog.setNumberLine(line);
								listErrorLog.add(errorLog);
							}
						}
					}

					// Check null column USER_NAME
					if (Helpers.checkNullColumn(user[1], Constant.USER_NAME, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(user[1], Constant.USER_NAME, line));
					} else {
						// Check maxlength column USER_NAME
						if (Helpers.checkMaxLength(user[1], Constant.USER_NAME, line, 30) != null) {
							listErrorLog.add(Helpers.checkMaxLength(user[1], Constant.USER_NAME, line, 30));
						} else {
							// Check key id PERMISSIONS_ID
							if (userDao.findUserName(user[1]) != null) {

								// Gen errorLog
								errorLog = new ErrorInfo();

								// Set data list listErrorLog
								errorLog.setColumn(Constant.USER_NAME);
								errorLog.setError(
										messageSource.getMessage("user_name_exist", null, Locale.getDefault()));
								errorLog.setNumberLine(line);
								listErrorLog.add(errorLog);
							}
						}
					}

					// Check null column PASS_WORD
					if (Helpers.checkNullColumn(user[2], Constant.PASS_WORD, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(user[2], Constant.PASS_WORD, line));
					}

					// Check null column BIRTH_DATE
					if (Helpers.checkNullColumn(user[3], Constant.BIRTH_DATE, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(user[3], Constant.BIRTH_DATE, line));
					} else {
						// Check format date column BIRTH_DATE
						if (!Helpers.isDateValid(user[3])) {
							// Gen errorLog
							errorLog = new ErrorInfo();

							// Set data list listErrorLog
							errorLog.setColumn(Constant.BIRTH_DATE);
							errorLog.setError(Constant.ERROR_DATE);
							errorLog.setNumberLine(line);
							listErrorLog.add(errorLog);
						}
					}

					// Check null column NAME
					if (Helpers.checkNullColumn(user[4], Constant.NAME, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(user[4], Constant.NAME, line));
					} else {
						// Check maxlength column USER_NAME
						if (Helpers.checkMaxLength(user[4], Constant.NAME, line, 100) != null) {
							listErrorLog.add(Helpers.checkMaxLength(user[4], Constant.NAME, line, 100));
						}
					}

					// Check maxlength column ADDRESS
					if (Helpers.checkMaxLength(user[5], Constant.ADDRESS, line, 100) != null) {
						listErrorLog.add(Helpers.checkMaxLength(user[5], Constant.ADDRESS, line, 100));
					}

					// Check maxlength column PHONE
					if (Helpers.checkMaxLength(user[6], Constant.PHONE, line, 11) != null) {
						listErrorLog.add(Helpers.checkMaxLength(user[6], Constant.PHONE, line, 11));
					}

					// Check null column SEX
					if (Helpers.checkNullColumn(user[7], Constant.SEX, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(user[7], Constant.SEX, line));
					} else {
						if (!Constant.DEFAULT_VALUE_0.equals(user[7]) && !Constant.DEFAULT_VALUE_1.equals(user[7])) {
							// Gen errorLog
							errorLog = new ErrorInfo();

							// Set data list listErrorLog
							errorLog.setColumn(Constant.SEX);
							errorLog.setError(Constant.ERROR_CHAR01.replace(Constant.DEFAULT_VALUE_MSG, Constant.SEX));
							errorLog.setNumberLine(line);
							listErrorLog.add(errorLog);
						}
					}

					// Check null column EMAIL
					if (Helpers.checkNullColumn(user[8], Constant.EMAIL, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(user[8], Constant.EMAIL, line));
					} else {
						// Check maxlength column EMAIL
						if (Helpers.checkMaxLength(user[8], Constant.EMAIL, line, 30) != null) {
							listErrorLog.add(Helpers.checkMaxLength(user[8], Constant.EMAIL, line, 30));
						}
					}

				}

				line++;
			}

			// Table TABLE_PERMISSIONS
		case Constant.TABLE_PERMISSIONS:
			iterator.next();
			while (iterator.hasNext()) {
				String[] permissions = iterator.next();
				if (permissions.length != 2) {
					// Gen errorLog
					errorLog = new ErrorInfo();

					// Set data list listErrorLog
					errorLog.setError(Constant.ERROR_LINE_FORMAT);
					errorLog.setNumberLine(line);
					listErrorLog.add(errorLog);
				} else {
					// Check null column PUBLISHERS_NAME
					if (Helpers.checkNullColumn(permissions[0], Constant.PERMISSION_NAME, line) != null) {
						listErrorLog.add(Helpers.checkNullColumn(permissions[0], Constant.PERMISSION_NAME, line));
					}else{
						// Check key PUBLISHERS_NAME
						if (permissionDao.findPermissionName(permissions[0]) != null) {

							// Gen errorLog
							errorLog = new ErrorInfo();

							// Set data list listErrorLog
							errorLog.setColumn(Constant.PERMISSION_NAME);
							errorLog.setError(
									messageSource.getMessage("permission_name_exist", null, Locale.getDefault()));
							errorLog.setNumberLine(line);
							listErrorLog.add(errorLog);
						}
					}
					// Check maxlength column PHONE
					if (Helpers.checkMaxLength(permissions[1], Constant.DESCRIPTION, line, 100) != null) {
						listErrorLog.add(Helpers.checkMaxLength(permissions[1], Constant.DESCRIPTION, line, 100));
					}

				}

				line++;
			}

		}
		return listErrorLog;
	}

	@Transactional
	@Override
	public int importData(FileFormInfo dataImportBean, String userName) {

		// Convert MultipartFile to File
		File fileImport = Helpers.convert(dataImportBean.getFileImport());
		CSVReader csvReader = null;
		List<String[]> listData = null;
		Date dateCreate = null;

		try {
			csvReader = new CSVReader(new FileReader(fileImport));
			listData = csvReader.readAll();
			dateCreate = DateUtil.getDateNow();
		} catch (FileNotFoundException e) {
			logger.error("ERROR FILE NOT FOUND: ", e);
		} catch (IOException e) {
			logger.error("ERROR IOEXCEPTION: ", e);
		} catch (ParseException e) {
			logger.error("ERROR GET DATE: ", e);
		}

		Iterator<String[]> iterator = listData.iterator();
		switch (dataImportBean.getNameTable()) {

		// Table Books
		case Constant.TABLE_BOOK:

			iterator.next();
			Book bookData = null;
			try {
				while (iterator.hasNext()) {
					String[] book = iterator.next();

					// Set data in bean Book
					bookData = new Book();
					bookData.setBookCode(book[0]);

					Categories cat = new Categories();
					cat.setcategoriesId(Integer.parseInt(book[1]));
					bookData.setCategories(cat);

					Publishers pub = new Publishers();
					pub.setPublishersId(Integer.parseInt(book[2]));
					bookData.setPublishers(pub);

					bookData.setName(book[3]);
					bookData.setPrice(Float.parseFloat(book[4]));
					bookData.setStatusBook(book[5]);
					bookData.setNumberBook(Integer.parseInt(book[6]));
					bookData.setNumberPage(Integer.parseInt(book[7]));
					bookData.setUserCreate(userName);
					bookData.setUserUpdate(userName);
					bookData.setDateCreate(dateCreate);
					bookData.setDateUpdate(dateCreate);
					bookData.setDeleteFlag(ConstantModel.DEL_FLG);

					bookDAO.insertBook(bookData);
				}
			} catch (Exception e) {
				logger.error("Error Insert Book: " + e.getMessage());

				return 0;
			}

			// Table Books
		case Constant.TABLE_BOOKDETAILS:

			iterator.next();
			BookDetail bookDetailData = null;
			try {
				while (iterator.hasNext()) {
					String[] bookDetail = iterator.next();

					// Set data in bean Book
					bookDetailData = new BookDetail();

					Book book = new Book();
					book.setBookId(Integer.parseInt(bookDetail[0]));
					bookDetailData.setBook(book);

					Author author = new Author();
					author.setAuthorsId(Integer.parseInt(bookDetail[1]));
					bookDetailData.setAuthor(author);

					bookDetailData.setUserCreate(userName);
					bookDetailData.setUserUpdate(userName);
					bookDetailData.setDateCreate(dateCreate);
					bookDetailData.setDateUpdate(dateCreate);
					bookDetailData.setDeleteFlag(ConstantModel.DEL_FLG);

					bookDetailDAO.insertBookDetail(bookDetailData);
				}
			} catch (Exception e) {
				logger.error("Error Insert BookDetail: " + e.getMessage());

				return 0;
			}

			// Table TABLE_CATEGORIES
		case Constant.TABLE_CATEGORIES:

			iterator.next();
			Categories categories = null;
			try {
				while (iterator.hasNext()) {
					String[] category = iterator.next();

					// Set data in bean Book
					categories = new Categories();

					categories.setCategoriesCode(category[1]);
					categories.setCatSubId(Integer.parseInt(category[0]));
					categories.setName(category[2]);
					categories.setUserCreate(userName);
					categories.setUserUpdate(userName);
					categories.setDateCreate(dateCreate);
					categories.setDateUpdate(dateCreate);
					categories.setDeleteFlag(ConstantModel.DEL_FLG);

					categoryDAO.insertCategory(categories);
				}
			} catch (Exception e) {
				logger.error("Error Insert Categories: " + e.getMessage());

				return 0;
			}

			// Table TABLE_CATEGORIES
		case Constant.TABLE_AUTHORS:

			iterator.next();
			Author author = null;
			try {
				while (iterator.hasNext()) {
					String[] authorData = iterator.next();

					// Set data in bean Book
					author = new Author();

					author.setAuthorsName(authorData[0]);
					author.setSex(authorData[1]);
					author.setEmail(authorData[2]);
					author.setDescription(authorData[3]);
					author.setPhone(authorData[4]);
					author.setBirthday(Helpers.convertStringtoDate(authorData[5]));
					author.setAddress(authorData[6]);

					author.setUserCreate(userName);
					author.setUserUpdate(userName);
					author.setDateCreate(dateCreate);
					author.setDateUpdate(dateCreate);
					author.setDeleteFlag(ConstantModel.DEL_FLG);

					authorDAO.insertAuthor(author);
				}
			} catch (Exception e) {
				logger.error("Error Insert Author: " + e.getMessage());

				return 0;
			}

			// Table TABLE_PUBLISHERS
		case Constant.TABLE_PUBLISHERS:

			iterator.next();
			Publishers publishers = null;
			try {
				while (iterator.hasNext()) {
					String[] publisherData = iterator.next();

					// Set data in bean Book
					publishers = new Publishers();

					publishers.setPublishersName(publisherData[0]);
					publishers.setPhone(publisherData[1]);
					publishers.setEmail(publisherData[2]);
					publishers.setAddress(publisherData[3]);

					publishers.setUserCreate(userName);
					publishers.setUserUpdate(userName);
					publishers.setDateCreate(dateCreate);
					publishers.setDateUpdate(dateCreate);
					publishers.setDeleteFlag(ConstantModel.DEL_FLG);

					publishersDAO.insertPublisher(publishers);
				}
			} catch (Exception e) {
				logger.error("Error Insert Publisher: " + e.getMessage());

				return 0;
			}

			// Table TABLE_PUBLISHERS
		case Constant.TABLE_USERS:

			iterator.next();
			Users users = null;
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			try {
				while (iterator.hasNext()) {
					String[] userData = iterator.next();

					// Set data in bean Book
					users = new Users();

					Permissions permissions = new Permissions();
					permissions.setPermissionsId(Integer.parseInt(userData[0]));
					users.setPermissions(permissions);

					users.setUserName(userData[1]);
					users.setPassWord(passwordEncoder.encode(userData[2]));
					users.setBirthDate(Helpers.convertStringtoDate(userData[3]));
					users.setName(userData[4]);
					users.setAddress(userData[5]);
					users.setPhone(userData[6]);
					users.setSex(userData[7]);
					users.setEmail(userData[8]);
					users.setUserCreate(userName);
					users.setUserUpdate(userName);
					users.setDateCreate(dateCreate);
					users.setDateUpdate(dateCreate);
					users.setDeleteFlag(ConstantModel.DEL_FLG);

					userDao.insertUser(users);
				}
			} catch (Exception e) {
				logger.error("Error Insert Users: " + e.getMessage());

				return 0;
			}

			// Table TABLE_PERMISSIONS
		case Constant.TABLE_PERMISSIONS:

			iterator.next();
			Permissions permissions = null;
			try {
				while (iterator.hasNext()) {
					String[] permissionData = iterator.next();

					// Set data in bean Book
					permissions = new Permissions();

					permissions.setPermissionName(permissionData[0]);
					permissions.setDescription(permissionData[1]);

					permissions.setUserCreate(userName);
					permissions.setUserUpdate(userName);
					permissions.setDateCreate(dateCreate);
					permissions.setDateUpdate(dateCreate);
					permissions.setDeleteFlag(ConstantModel.DEL_FLG);

					permissionDao.insertPermission(permissions);
				}
			} catch (Exception e) {
				logger.error("Error Insert Permissions: " + e.getMessage());

				return 0;
			}

		}
		return 1;
	}
}
