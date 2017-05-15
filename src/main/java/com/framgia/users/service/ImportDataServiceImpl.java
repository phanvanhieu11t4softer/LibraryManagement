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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.framgia.users.bean.ErrorInfo;
import com.framgia.users.bean.FileFormInfo;
import com.framgia.users.dao.BookDAO;
import com.framgia.users.dao.CategoryDAO;
import com.framgia.users.dao.PublishersDAO;
import com.framgia.users.model.Book;
import com.framgia.users.model.Categories;
import com.framgia.users.model.ConstantModel;
import com.framgia.users.model.Publishers;
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

		switch (dataImportBean.getNameTable()) {
		
		// Table Books
		case Constant.TABLE_BOOK:
			if (!Constant.NAME_FILE_DATA_BOOK.equals(fileUpload.getOriginalFilename())) {
				return messageSource.getMessage("file_incorrect", null, Locale.getDefault());
			}

			// Next line Header
			String[] header = iterator.next();

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
		}catch (ParseException e) {
			logger.error("ERROR GET DATE: ", e);
		}
		
		Iterator<String[]> iterator = listData.iterator();
		switch (dataImportBean.getNameTable()) {
		
		// Table Books
		case Constant.TABLE_BOOK:

			iterator.next();
			Book bookData = null;
			try{
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
			
		}
		return 1;
	}
}
