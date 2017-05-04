package com.framgia.users.service;

import java.util.List;

import com.framgia.users.bean.ErrorInfo;
import com.framgia.users.bean.FileFormInfo;

/**
 * ImportDataService.java
 * 
 * @version 3/05/2017
 * @author phan.van.hieu@framgia.com
 * 
 */
public interface ImportDataService {

	// Check format file import
	String checkFormatFile(FileFormInfo dataImportBean);

	// Check data file import
	List<ErrorInfo> checkDataFile(FileFormInfo dataImportBean);

	// Insert data into table
	int importData(FileFormInfo dataImportBean, String userName);

}
