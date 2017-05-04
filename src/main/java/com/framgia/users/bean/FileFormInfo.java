package com.framgia.users.bean;

import org.springframework.web.multipart.MultipartFile;

/**
 * FileForm.java description table File Import Data
 * 
 * @version 21/04/2017
 * @author phan.van.hieu@framgia.com
 */
public class FileFormInfo {
	private MultipartFile fileImport;
	private String nameTable;
	private String fileName;

	public FileFormInfo() {
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getNameTable() {
		return nameTable;
	}

	public void setNameTable(String nameTable) {
		this.nameTable = nameTable;
	}

	public MultipartFile getFileImport() {
		return fileImport;
	}

	public void setFileImport(MultipartFile fileImport) {
		this.fileImport = fileImport;
	}

}
