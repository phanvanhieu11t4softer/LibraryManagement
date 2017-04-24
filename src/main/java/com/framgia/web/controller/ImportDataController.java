package com.framgia.web.controller;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.framgia.users.bean.BeanAuthor;
import com.framgia.users.bean.BeanBook;
import com.framgia.users.bean.BeanBookDetail;
import com.framgia.users.bean.BeanCategory;
import com.framgia.users.bean.BeanPublishers;
import com.framgia.users.bean.ErrorLog;
import com.framgia.users.bean.FileForm;
import com.framgia.users.service.AuthorService;
import com.framgia.users.service.BookDetailService;
import com.framgia.users.service.BookService;
import com.framgia.users.service.CategoryService;
import com.framgia.users.service.PublishersService;
import com.framgia.users.util.Constant;
import com.framgia.users.util.Helpers;

/**
 * ManagerBookController.java
 * 
 * @version 21/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Controller
public class ImportDataController {

    @Autowired
    BookService bookService;
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    PublishersService publishersService;
    
    @Autowired
    AuthorService authorService;
    
    @Autowired
    BookDetailService bookDetailService;
    
    /**
     * 
     * @param dataImportBean
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/importData")
    public ModelAndView pageImportData(@ModelAttribute("dataImportBean") FileForm dataImportBean,
            RedirectAttributes redirectAttributes, Model m) {
        ModelAndView model = new ModelAndView();

        // BufferedInput Stream
        List<ErrorLog> listErrorLog= new ArrayList<ErrorLog>();
        BufferedInputStream bufferedInputStream = null;
        DataInputStream dataInputStream = null;
        Boolean check= true;
        MultipartFile fileUpload = dataImportBean.getFileImport();
        ErrorLog errorLog;
        int line = 2;
        
        // Check empty Name Table
        if (Helpers.isEmpty(dataImportBean.getNameTable())) {
            check = false;
            m.addAttribute("error_table", "Please choose Table name");
            model.setViewName("importdata");
        }

        // Check empty File
        if (Helpers.isEmpty(dataImportBean.getFileImport().toString())) {
            check = false;
            m.addAttribute("error_file", "Please choose File");
            model.setViewName("importdata");
        }else{
        	// Check format File
            if (!Helpers.checkFormatFile(fileUpload.getOriginalFilename(), Constant.CONSTANT_CSV)) {
                check = false;
                m.addAttribute("error_file", "Please choose File CSV");
                model.setViewName("importdata");
            }
        }

        // Check checkValidation
		if (check) {

			// Switch ... Case import data
			// Insert data table Book
			switch (dataImportBean.getNameTable()) {
			
				// Table Books
				case Constant.TABLE_BOOK:
					// Check name file import
					if (!Constant.NAME_FILE_DATA_BOOK.equals(fileUpload.getOriginalFilename())) {
						m.addAttribute("err_data", Constant.ERROR_FILE);
						model.setViewName("importdata");
						return model;
					}
					try {
						// Get data input File CSV
						InputStream inputStream = fileUpload.getInputStream();
						bufferedInputStream = new BufferedInputStream(inputStream);
						dataInputStream = new DataInputStream(bufferedInputStream);
	
						// Next line Header
						String[] header = dataInputStream.readLine().split(Constant.CHARACTER);
	
						// Check format header File
						if (header.length != 9) {
							m.addAttribute("err_data", Constant.ERROR_FILE_FORMAT);
							model.setViewName("importdata");
							return model;
						} else if (!((Constant.BOOK_CODE.equals(header[0])) && (Constant.CATEGORIES_ID.equals(header[1])
								&& (Constant.CATEGORIES_SUB_ID.equals(header[2])
										&& (Constant.PUBLISHERS_ID.equals(header[3])) && (Constant.NAME.equals(header[4]))
										&& (Constant.PRICE.equals(header[5])) && (Constant.STATUS_BOOK.equals(header[6]))
										&& (Constant.NUMBER_BOOK.equals(header[7]))
										&& (Constant.NUMBER_PAGE.equals(header[8])))))) {
							m.addAttribute("err_data", Constant.ERROR_FILE_FORMAT);
							model.setViewName("importdata");
							return model;
						}
						
						// Check data file
						if (dataInputStream.available() == 0) {
							m.addAttribute("err_data", Constant.ERROR_FILE_EMPTY.replace(Constant.DEFAULT_VALUE_MSG,
								fileUpload.getOriginalFilename()));
							model.setViewName("importdata");
							return model;
						}
	
						while (dataInputStream.available() != 0) {
							// Array Book information
							String[] book = dataInputStream.readLine().split(Constant.CHARACTER);
	
							if(book.length != 9){
								// Gen errorLog
								errorLog = new ErrorLog();
	
								// Set data list listErrorLog
								errorLog.setError(Constant.ERROR_LINE_FORMAT);
								errorLog.setNumberLine(line);
								listErrorLog.add(errorLog);
							}else{
								// Check null column Book Code
								if (Helpers.checkNullColumn(book[0], Constant.BOOK_CODE, line) != null) {
									listErrorLog.add(Helpers.checkNullColumn(book[0], Constant.BOOK_CODE, line));
								}
		
								// Check null column CategoriesID
								if (Helpers.checkNullColumn(book[1], Constant.CATEGORIES_ID, line) != null) {
									listErrorLog.add(Helpers.checkNullColumn(book[1], Constant.CATEGORIES_ID, line));
								}
		
								// Check null column CategoriesID
								if (Helpers.checkNullColumn(book[2], Constant.CATEGORIES_SUB_ID, line) != null) {
									listErrorLog.add(Helpers.checkNullColumn(book[2], Constant.CATEGORIES_SUB_ID, line));
								}
		
								// Check null column PublishersID
								if (Helpers.checkNullColumn(book[3], Constant.PUBLISHERS_ID, line) != null) {
									listErrorLog.add(Helpers.checkNullColumn(book[3], Constant.PUBLISHERS_ID, line));
								}
		
								// Check null column StatusBook
								if (Helpers.checkNullColumn(book[6], Constant.STATUS_BOOK, line) != null) {
									listErrorLog.add(Helpers.checkNullColumn(book[6], Constant.STATUS_BOOK, line));
								}else {
									if (!Constant.DEFAULT_VALUE_0.equals(book[6])
											&& !Constant.DEFAULT_VALUE_1.equals(book[6])) {
										// Gen errorLog
										errorLog = new ErrorLog();

										// Set data list listErrorLog
										errorLog.setColumn(Constant.STATUS_BOOK);
										errorLog.setError(Constant.ERROR_CHAR01.replace(Constant.DEFAULT_VALUE_MSG, 
												Constant.STATUS_BOOK));
										errorLog.setNumberLine(line);
										listErrorLog.add(errorLog);
									}
								}
		
								// Check format Integer column PUBLISHERS_ID
								if (Helpers.checkIntegerColumn(book[3], Constant.PUBLISHERS_ID, line) != null) {
									listErrorLog.add(Helpers.checkIntegerColumn(book[3], Constant.PUBLISHERS_ID, line));
								}
		
								// Check format Float column PRICE
								if (!Helpers.isEmpty(book[5])
										&& Helpers.checkFloatColumn(book[5], Constant.PRICE, line) != null) {
									listErrorLog.add(Helpers.checkFloatColumn(book[5], Constant.PRICE, line));
								}
		
								// Check format Float column NUMBER_BOOK
								if (!Helpers.isEmpty(book[7])
										&& Helpers.checkIntegerColumn(book[7], Constant.NUMBER_BOOK, line) != null) {
									listErrorLog.add(Helpers.checkIntegerColumn(book[7], Constant.NUMBER_BOOK, line));
								}
		
								// Check format Float column NUMBER_PAGE
								if (!Helpers.isEmpty(book[8])
										&& Helpers.checkIntegerColumn(book[8], Constant.NUMBER_PAGE, line) != null) {
									listErrorLog.add(Helpers.checkIntegerColumn(book[8], Constant.NUMBER_PAGE, line));
								}
		
								// Check maxlength column BOOK_CODE
								if (Helpers.checkMaxLength(book[0], Constant.BOOK_CODE, line, 10) != null) {
									listErrorLog.add(Helpers.checkMaxLength(book[0], Constant.BOOK_CODE, line, 10));
								}
								
								// Check maxlength column NAME
								if (Helpers.checkMaxLength(book[3], Constant.NAME, line, 100) != null) {
									listErrorLog.add(Helpers.checkMaxLength(book[3], Constant.NAME, line, 100));
								}
								
								// Check key id BookID
								long countIdCategory = this.categoryService.findCategoryId(book[1], book[2],
										Constant.NOT_DELETE);
								if (countIdCategory == 0) {
		
									// Gen errorLog
									errorLog = new ErrorLog();
		
									// Set data list listErrorLog
									errorLog.setColumn(Constant.CATEGORIES_ID);
									errorLog.setError(Constant.ERROR_EXIST_KEY_CATEGORY_ID);
									errorLog.setNumberLine(line);
									listErrorLog.add(errorLog);
								}
		
								// Check key id Publishers
								long countIdPublisher = this.publishersService.findPublishersId(book[3], Constant.NOT_DELETE);
								if (countIdPublisher == 0) {
		
									// Gen errorLog
									errorLog = new ErrorLog();
		
									// Set data list listErrorLog
									errorLog.setColumn(Constant.PUBLISHERS_ID);
									errorLog.setError(Constant.ERROR_EXIST_KEY_PUBLISHER_ID);
									errorLog.setNumberLine(line);
									listErrorLog.add(errorLog);
								}
							}
							
							line++;
						}
	
						if (listErrorLog.size() > 0) {
							m.addAttribute("listError", listErrorLog);
							model.setViewName("importdata");
						} else {
	
							// Set data in BeanBook
							inputStream = fileUpload.getInputStream();
							bufferedInputStream = new BufferedInputStream(inputStream);
							dataInputStream = new DataInputStream(bufferedInputStream);
							dataInputStream.readLine().split(Constant.CHARACTER);
							List<BeanBook> listBook = new ArrayList<BeanBook>();
							BeanBook beanBook = null;
							while (dataInputStream.available() != 0) {
								String[] book = dataInputStream.readLine().split(Constant.CHARACTER);
								// Set data in categoryId
	
								StringBuffer categoryId = new StringBuffer(book[1]);
								categoryId.append(Constant.CHARACTER);
								categoryId.append(book[2]);
	
								// Set data in bean Book
								beanBook = new BeanBook();
								beanBook.setBookCode(book[0]);
								beanBook.setCategoriesId(categoryId.toString());
								beanBook.setPublishersId(Integer.parseInt(book[3]));
								beanBook.setName(book[4]);
								beanBook.setPrice(Float.parseFloat(book[5]));
								beanBook.setStatusBook(book[6]);
								beanBook.setNumberBook(Integer.parseInt(book[7]));
								beanBook.setNumberPage(Integer.parseInt(book[8]));
								listBook.add(beanBook);
							}
							
							// Import data into table Books
							int record = bookService.addBook(listBook);
							if(record > 0){
								m.addAttribute("success", Helpers.replaceString(Constant.MSG_SUCCESS, Constant.TB_BOOKS));
							}else{
								m.addAttribute("fail", Helpers.replaceString(Constant.MSG_FAIL, Constant.TB_BOOKS));
							}
							model.setViewName("importdata");
						}
	
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							bufferedInputStream.close();
							dataInputStream.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
					break;
				
				// Table BookDetails
				case Constant.TABLE_BOOKDETAILS:
					if (!Constant.NAME_FILE_DATA_BOOK_DETAIL.equals(fileUpload.getOriginalFilename())) {
						m.addAttribute("err_data", Constant.ERROR_FILE);
						model.setViewName("importdata");
						return model;
					}
					try {
						// Get data input File CSV
						InputStream inputStream = fileUpload.getInputStream();
						bufferedInputStream = new BufferedInputStream(inputStream);
						dataInputStream = new DataInputStream(bufferedInputStream);
	
						// Next line Header
						String[] header = dataInputStream.readLine().split(Constant.CHARACTER);
	
						// Check format header File
						if (header.length != 2) {
							m.addAttribute("err_data", Constant.ERROR_FILE_FORMAT);
							model.setViewName("importdata");
							return model;
						} else if (!(Constant.BOOK_ID.equals(header[0]) && Constant.AUTHOR_ID.equals(header[1]))){
							m.addAttribute("err_data", Constant.ERROR_FILE_FORMAT);
							model.setViewName("importdata");
							return model;
						}
						
						// Check data file
						if (dataInputStream.available() == 0) {
							m.addAttribute("err_data", Constant.ERROR_FILE_EMPTY.replace(Constant.DEFAULT_VALUE_MSG,
								fileUpload.getOriginalFilename()));
							model.setViewName("importdata");
							return model;
						}
	
						while (dataInputStream.available() != 0) {
							// Array Book information
							String[] book = dataInputStream.readLine().split(Constant.CHARACTER);
	
							if(book.length != 2){
								// Gen errorLog
								errorLog = new ErrorLog();
	
								// Set data list listErrorLog
								errorLog.setError(Constant.ERROR_LINE_FORMAT);
								errorLog.setNumberLine(line);
								listErrorLog.add(errorLog);
							}else{
								// Check null column BOOK_ID
								if (Helpers.checkNullColumn(book[0], Constant.BOOK_ID, line) != null) {
									listErrorLog.add(Helpers.checkNullColumn(book[0], Constant.BOOK_ID, line));
								}
		
								// Check null column AUTHOR_ID
								if (Helpers.checkNullColumn(book[1], Constant.AUTHOR_ID, line) != null) {
									listErrorLog.add(Helpers.checkNullColumn(book[1], Constant.AUTHOR_ID, line));
								}
		
								// Check format Integer column BOOK_ID
								if (Helpers.checkIntegerColumn(book[0], Constant.BOOK_ID, line) != null) {
									listErrorLog.add(Helpers.checkIntegerColumn(book[0], Constant.BOOK_ID, line));
								}
		
								// Check format Integer column AUTHOR_ID
								if (Helpers.checkIntegerColumn(book[1], Constant.AUTHOR_ID, line) != null) {
									listErrorLog.add(Helpers.checkIntegerColumn(book[1], Constant.AUTHOR_ID, line));
								}
		
								// Check key id Book
								long countBookId = this.bookService.findBookId(book[0], Constant.NOT_DELETE);
								if (countBookId == 0) {
		
									// Gen errorLog
									errorLog = new ErrorLog();
		
									// Set data list listErrorLog
									errorLog.setColumn(Constant.BOOK_ID);
									errorLog.setError(Constant.ERROR_EXIST_KEY_BOOK_ID);
									errorLog.setNumberLine(line);
									listErrorLog.add(errorLog);
								}
		
								// Check key id Publishers
								long countAuthorId = this.authorService.findAuthorId(book[1], Constant.NOT_DELETE);
								if (countAuthorId == 0) {
		
									// Gen errorLog
									errorLog = new ErrorLog();
		
									// Set data list listErrorLog
									errorLog.setColumn(Constant.AUTHOR_ID);
									errorLog.setError(Constant.ERROR_EXIST_KEY_AUTHOR_ID);
									errorLog.setNumberLine(line);
									listErrorLog.add(errorLog);
								}
							}
							
							line++;
						}
	
						if (listErrorLog.size() > 0) {
							m.addAttribute("listError", listErrorLog);
							model.setViewName("importdata");
						} else {
	
							// Set data in BeanBook
							inputStream = fileUpload.getInputStream();
							bufferedInputStream = new BufferedInputStream(inputStream);
							dataInputStream = new DataInputStream(bufferedInputStream);
							dataInputStream.readLine().split(Constant.CHARACTER);
							List<BeanBookDetail> listBookDetail = new ArrayList<BeanBookDetail>();
							BeanBookDetail beanBookDetail = null;
							while (dataInputStream.available() != 0) {
								String[] book = dataInputStream.readLine().split(Constant.CHARACTER);
								
								// Set data in bean Book
								beanBookDetail = new BeanBookDetail();
								beanBookDetail.setBookId(Integer.parseInt(book[0]));
								beanBookDetail.setAuthorsId(Integer.parseInt(book[1]));
								listBookDetail.add(beanBookDetail);
							}
							
							// Import data into table BookDetails
							int record = bookDetailService.addBookDetail(listBookDetail);
							if(record > 0){
								m.addAttribute("success", Helpers.replaceString(Constant.MSG_SUCCESS, Constant.TB_BOOK_DETAILS));
							}else{
								m.addAttribute("fail", Helpers.replaceString(Constant.MSG_FAIL, Constant.TB_BOOK_DETAILS));
							}
							
							model.setViewName("importdata");
						}
	
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							bufferedInputStream.close();
							dataInputStream.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
					break;
					
				// Table TABLE_CATEGORIES
				case Constant.TABLE_CATEGORIES:
					if (!Constant.NAME_FILE_DATA_CATEGORY.equals(fileUpload.getOriginalFilename())) {
						m.addAttribute("err_data", Constant.ERROR_FILE);
						model.setViewName("importdata");
						return model;
					}
					try {
						// Get data input File CSV
						InputStream inputStream = fileUpload.getInputStream();
						bufferedInputStream = new BufferedInputStream(inputStream);
						dataInputStream = new DataInputStream(bufferedInputStream);
	
						// Next line Header
						String[] header = dataInputStream.readLine().split(Constant.CHARACTER);
	
						// Check format header File
						if (header.length != 3) {
							m.addAttribute("err_data", Constant.ERROR_FILE_FORMAT);
							model.setViewName("importdata");
							return model;
							
						} else if (!(Constant.CAT_SUB_ID.equals(header[0]) && Constant.CATEGORIES_CODE.equals(header[1])
								&& Constant.CATEGORY_NAME.equals(header[2]))){
							m.addAttribute("err_data", Constant.ERROR_FILE_FORMAT);
							model.setViewName("importdata");
							return model;
						}
						
						// Check data file
						if (dataInputStream.available() == 0) {
							m.addAttribute("err_data", Constant.ERROR_FILE_EMPTY.replace(Constant.DEFAULT_VALUE_MSG,
								fileUpload.getOriginalFilename()));
							model.setViewName("importdata");
							return model;
						}
	
						while (dataInputStream.available() != 0) {
							// Array Category information
							String[] category = dataInputStream.readLine().split(Constant.CHARACTER);
							if(category.length != 3){
								// Gen errorLog
								errorLog = new ErrorLog();
	
								// Set data list listErrorLog
								errorLog.setError(Constant.ERROR_LINE_FORMAT);
								errorLog.setNumberLine(line);
								listErrorLog.add(errorLog);
							}else{
								// Check null column CATEGORIES_CODE
								if (Helpers.checkNullColumn(category[1], Constant.CATEGORIES_CODE, line) != null) {
									listErrorLog.add(Helpers.checkNullColumn(category[1], Constant.CATEGORIES_CODE, line));
								}
		
								// Check null column AUTHOR_ID
								if (Helpers.checkNullColumn(category[2], Constant.CATEGORY_NAME, line) != null) {
									listErrorLog.add(Helpers.checkNullColumn(category[2], Constant.CATEGORY_NAME, line));
								}
		
								// Check format Integer column BOOK_ID
								if (Helpers.checkIntegerColumn(category[0], Constant.CAT_SUB_ID, line) != null && !Helpers.isEmpty(category[0])) {
									listErrorLog.add(Helpers.checkIntegerColumn(category[0], Constant.CAT_SUB_ID, line));
								}
		
								// Check maxlength column CATEGORIES_CODE
								if (Helpers.checkMaxLength(category[1], Constant.CATEGORIES_CODE, line, 10) != null) {
									listErrorLog.add(Helpers.checkMaxLength(category[1], Constant.CATEGORIES_CODE, line, 10));
								}
								
								// Check maxlength column CATEGORY_NAME
								if (Helpers.checkMaxLength(category[2], Constant.CATEGORY_NAME, line, 100) != null) {
									listErrorLog.add(Helpers.checkMaxLength(category[2], Constant.CATEGORY_NAME, line, 100));
								}
								
								// Check key catSubId Category
								if(!Helpers.isEmpty(category[0])){
									long countCatSubId = this.categoryService.findCatSubId(category[0], Constant.NOT_DELETE);
									if (countCatSubId == 0) {
			
										// Gen errorLog
										errorLog = new ErrorLog();
			
										// Set data list listErrorLog
										errorLog.setColumn(Constant.CAT_SUB_ID);
										errorLog.setError(Constant.ERROR_KEY_CAT_SUB_ID);
										errorLog.setNumberLine(line);
										listErrorLog.add(errorLog);
									}
								}
								
								// Check bookCoed Category
								if(!Helpers.isEmpty(category[1])){
									long countCategoryCode = this.categoryService.findCategoryCode(category[1], Constant.NOT_DELETE);
									if (countCategoryCode > 0) {
			
										// Gen errorLog
										errorLog = new ErrorLog();
			
										// Set data list listErrorLog
										errorLog.setColumn(Constant.CATEGORIES_CODE);
										errorLog.setError(Constant.ERROR_CATEGORY_CODE);
										errorLog.setNumberLine(line);
										listErrorLog.add(errorLog);
									}
								}
							}
							
							line++;
						}
	
						if (listErrorLog.size() > 0) {
							m.addAttribute("listError", listErrorLog);
							model.setViewName("importdata");
						} else {
	
							// Set data in BeanBook
							inputStream = fileUpload.getInputStream();
							bufferedInputStream = new BufferedInputStream(inputStream);
							dataInputStream = new DataInputStream(bufferedInputStream);
							dataInputStream.readLine().split(Constant.CHARACTER);
							List<BeanCategory> listCategory = new ArrayList<BeanCategory>();
							BeanCategory beanCategory = null;
							while (dataInputStream.available() != 0) {
								String[] category = dataInputStream.readLine().split(Constant.CHARACTER);
								
								// Set data in bean Category
								beanCategory = new BeanCategory();
								beanCategory.setCatSubId(Integer.parseInt(category[0]));
								beanCategory.setCategoriesCode(category[1]);
								beanCategory.setName(category[2]);
								listCategory.add(beanCategory);
							}
							
							// Import data into table BookDetails
							int record = categoryService.addCategories(listCategory);
							if(record > 0){
								m.addAttribute("success", Helpers.replaceString(Constant.MSG_SUCCESS, Constant.TB_BOOK_CATEGORY));
							}else{
								m.addAttribute("fail", Helpers.replaceString(Constant.MSG_FAIL, Constant.TB_BOOK_CATEGORY));
							}
							
							model.setViewName("importdata");
						}
	
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							bufferedInputStream.close();
							dataInputStream.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
					break;
					
				// Table Author
				case Constant.TABLE_AUTHORS:
					if (!Constant.NAME_FILE_DATA_AUTHOR.equals(fileUpload.getOriginalFilename())) {
						m.addAttribute("err_data", Constant.ERROR_FILE);
						model.setViewName("importdata");
						return model;
					}
					try {
						// Get data input File CSV
						InputStream inputStream = fileUpload.getInputStream();
						bufferedInputStream = new BufferedInputStream(inputStream);
						dataInputStream = new DataInputStream(bufferedInputStream);

						// Next line Header
						String[] header = dataInputStream.readLine().split(Constant.CHARACTER);

						// Check format header File
						if (header.length != 7) {
							m.addAttribute("err_data", Constant.ERROR_FILE_FORMAT);
							model.setViewName("importdata");
							return model;

						} else if (!(Constant.AUTHORS_NAME.equals(header[0]) && Constant.SEX.equals(header[1])
							&& Constant.EMAIL.equals(header[2]) && Constant.DESCRIPTION.equals(header[3])
							&& Constant.PHONE.equals(header[4]) && Constant.BIRTH_DAY.equals(header[5])
							&& Constant.ADDRESS.equals(header[6]))) {
							
							m.addAttribute("err_data", Constant.ERROR_FILE_FORMAT);
							model.setViewName("importdata");
						return model;
						}

						if (dataInputStream.available() == 0) {
							m.addAttribute("err_data", Constant.ERROR_FILE_EMPTY.replace(Constant.DEFAULT_VALUE_MSG,
								fileUpload.getOriginalFilename()));
							model.setViewName("importdata");
							return model;
						}
						
						while (dataInputStream.available() != 0) {
							// Array Category information
							String[] author = dataInputStream.readLine().split(Constant.CHARACTER);
							if(author.length != 7){
								// Gen errorLog
								errorLog = new ErrorLog();
	
								// Set data list listErrorLog
								errorLog.setError(Constant.ERROR_LINE_FORMAT);
								errorLog.setNumberLine(line);
								listErrorLog.add(errorLog);
							}else{
								// Check null column AUTHORS_NAME
								if (Helpers.checkNullColumn(author[0], Constant.AUTHORS_NAME, line) != null) {
									listErrorLog.add(Helpers.checkNullColumn(author[0], Constant.AUTHORS_NAME, line));
								}
		
								// Check null column SEX
								if (Helpers.checkNullColumn(author[1], Constant.SEX, line) != null) {
									listErrorLog.add(Helpers.checkNullColumn(author[1], Constant.SEX, line));
								} else {
									if (!Constant.DEFAULT_VALUE_0.equals(author[1])
											&& !Constant.DEFAULT_VALUE_1.equals(author[1])) {
										// Gen errorLog
										errorLog = new ErrorLog();

										// Set data list listErrorLog
										errorLog.setColumn(Constant.SEX);
										errorLog.setError(Constant.ERROR_CHAR01.replace(Constant.DEFAULT_VALUE_MSG, 
												Constant.SEX));
										errorLog.setNumberLine(line);
										listErrorLog.add(errorLog);
									}
								}
		
								// Check null column EMAIL
								if (Helpers.checkNullColumn(author[2], Constant.EMAIL, line) != null) {
									listErrorLog.add(Helpers.checkNullColumn(author[2], Constant.EMAIL, line));
								}
								
								// Check maxlength column AUTHORS_NAME
								if (Helpers.checkMaxLength(author[0], Constant.AUTHORS_NAME, line, 100) != null) {
									listErrorLog.add(Helpers.checkMaxLength(author[0], Constant.AUTHORS_NAME, line, 100));
								}
								
								// Check maxlength column EMAIL
								if (Helpers.checkMaxLength(author[2], Constant.EMAIL, line, 30) != null) {
									listErrorLog.add(Helpers.checkMaxLength(author[2], Constant.EMAIL, line, 30));
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
						    		errorLog = new ErrorLog();
						            
						            // Set data list listErrorLog
						            errorLog.setColumn(Constant.BIRTH_DAY);
						            errorLog.setError(Constant.ERROR_DATE);
						            errorLog.setNumberLine(line);
						            listErrorLog.add(errorLog);
								}
							}
							
							line++;
						}
	
						if (listErrorLog.size() > 0) {
							m.addAttribute("listError", listErrorLog);
							model.setViewName("importdata");
						} else {
	
							// Set data in BeanBook
							inputStream = fileUpload.getInputStream();
							bufferedInputStream = new BufferedInputStream(inputStream);
							dataInputStream = new DataInputStream(bufferedInputStream);
							dataInputStream.readLine().split(Constant.CHARACTER);
							List<BeanAuthor> listAuthor = new ArrayList<BeanAuthor>();
							BeanAuthor beanAuthor = null;
							while (dataInputStream.available() != 0) {
								String[] author = dataInputStream.readLine().split(Constant.CHARACTER);
								
								// Set data in bean Category
								beanAuthor = new BeanAuthor();
								beanAuthor.setAuthorsName(author[0]);
								beanAuthor.setSex(author[1]);
								beanAuthor.setEmail(author[2]);
								beanAuthor.setDescription(author[3]);
								beanAuthor.setPhone(author[4]);
								beanAuthor.setBirthday(author[5]);
								beanAuthor.setAddress(author[6]);
								listAuthor.add(beanAuthor);
							}
							
							// Import data into table BookDetails
							int record = authorService.addAuthor(listAuthor);
							if(record > 0){
								m.addAttribute("success", Helpers.replaceString(Constant.MSG_SUCCESS, Constant.TB_BOOK_AUHTHOR));
							}else{
								m.addAttribute("fail", Helpers.replaceString(Constant.MSG_FAIL, Constant.TB_BOOK_AUHTHOR));
							}
							
							model.setViewName("importdata");
						}
	
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							bufferedInputStream.close();
							dataInputStream.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
					break;
					
				// Table TABLE_PUBLISHERS
				case Constant.TABLE_PUBLISHERS:
					if (!Constant.NAME_FILE_DATA_PUBLISHERS.equals(fileUpload.getOriginalFilename())) {
						m.addAttribute("err_data", Constant.ERROR_FILE);
						model.setViewName("importdata");
						return model;
					}
					try {
						// Get data input File CSV
						InputStream inputStream = fileUpload.getInputStream();
						bufferedInputStream = new BufferedInputStream(inputStream);
						dataInputStream = new DataInputStream(bufferedInputStream);

						// Next line Header
						String[] header = dataInputStream.readLine().split(Constant.CHARACTER);

						// Check format header File
						if (header.length != 4) {
							m.addAttribute("err_data", Constant.ERROR_FILE_FORMAT);
							model.setViewName("importdata");
							return model;

						} else if (!(Constant.PUBLISHERS_NAME.equals(header[0]) && Constant.PHONE.equals(header[1])
							&& Constant.EMAIL.equals(header[2]) && Constant.ADDRESS.equals(header[3]))) {
							
							m.addAttribute("err_data", Constant.ERROR_FILE_FORMAT);
							model.setViewName("importdata");
						return model;
						}

						if (dataInputStream.available() == 0) {
							m.addAttribute("err_data", Constant.ERROR_FILE_EMPTY.replace(Constant.DEFAULT_VALUE_MSG,
								fileUpload.getOriginalFilename()));
							model.setViewName("importdata");
							return model;
						}
						while (dataInputStream.available() != 0) {
							// Array Category information
							String[] publishers = dataInputStream.readLine().split(Constant.CHARACTER);
							if(publishers.length != 4){
								// Gen errorLog
								errorLog = new ErrorLog();
	
								// Set data list listErrorLog
								errorLog.setError(Constant.ERROR_LINE_FORMAT);
								errorLog.setNumberLine(line);
								listErrorLog.add(errorLog);
							}else{
								// Check null column PUBLISHERS_NAME
								if (Helpers.checkNullColumn(publishers[0], Constant.PUBLISHERS_NAME, line) != null) {
									listErrorLog.add(Helpers.checkNullColumn(publishers[0], Constant.PUBLISHERS_NAME, line));
								}
								
								// Check null column PUBLISHERS_NAME
								if (Helpers.checkNullColumn(publishers[1], Constant.PHONE, line) != null) {
									listErrorLog.add(Helpers.checkNullColumn(publishers[1], Constant.PHONE, line));
								}
								
								// Check null column PUBLISHERS_NAME
								if (Helpers.checkNullColumn(publishers[2], Constant.EMAIL, line) != null) {
									listErrorLog.add(Helpers.checkNullColumn(publishers[2], Constant.EMAIL, line));
								}
								
								// Check maxlength column PUBLISHERS_NAME
								if (Helpers.checkMaxLength(publishers[0], Constant.PUBLISHERS_NAME, line, 100) != null) {
									listErrorLog.add(Helpers.checkMaxLength(publishers[0], Constant.PUBLISHERS_NAME, line, 100));
								}
								// Check maxlength column PHONE
								if (Helpers.checkMaxLength(publishers[1], Constant.PHONE, line, 11) != null) {
									listErrorLog.add(Helpers.checkMaxLength(publishers[1], Constant.PUBLISHERS_NAME, line, 11));
								}
								// Check maxlength column EMAIL
								if (Helpers.checkMaxLength(publishers[2], Constant.EMAIL, line, 30) != null) {
									listErrorLog.add(Helpers.checkMaxLength(publishers[2], Constant.PUBLISHERS_NAME, line, 30));
								}
								
							}
							
							line++;
						}
	
						// Check error list
						if (listErrorLog.size() > 0) {
							m.addAttribute("listError", listErrorLog);
							model.setViewName("importdata");
						} else {
	
							// Set data in BeanBook
							inputStream = fileUpload.getInputStream();
							bufferedInputStream = new BufferedInputStream(inputStream);
							dataInputStream = new DataInputStream(bufferedInputStream);
							dataInputStream.readLine().split(Constant.CHARACTER);
							List<BeanPublishers> listPublishers = new ArrayList<BeanPublishers>();
							BeanPublishers beanPublishers = null;
							while (dataInputStream.available() != 0) {
								String[] publishers = dataInputStream.readLine().split(Constant.CHARACTER);
								
								// Set data in bean BeanPublishers
								beanPublishers = new BeanPublishers();
								beanPublishers.setPublishersName(publishers[0]);
								beanPublishers.setPhone(publishers[1]);
								beanPublishers.setEmail(publishers[2]);
								beanPublishers.setAddress(publishers[3]);
								listPublishers.add(beanPublishers);
							}
							
							// Import data into table Publishers
							int record = publishersService.addPublishers(listPublishers);
							if(record > 0){
								m.addAttribute("success", Helpers.replaceString(Constant.MSG_SUCCESS, Constant.TB_BOOK_PUBLISHERS));
							}else{
								m.addAttribute("fail", Helpers.replaceString(Constant.MSG_FAIL, Constant.TB_BOOK_PUBLISHERS));
							}
							
							model.setViewName("importdata");
						}
	
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							bufferedInputStream.close();
							dataInputStream.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
					break;
			}
        }

        return model;
    }
}
