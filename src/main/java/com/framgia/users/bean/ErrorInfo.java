package com.framgia.users.bean;

/**
 * ErrorLog.java description error in file import
 * 
 * @version 21/04/2017
 * @author phan.van.hieu@framgia.com
 */
public class ErrorInfo {
	private Integer numberLine;
	private String column;
	private String error;

	public ErrorInfo() {
	}

	public Integer getNumberLine() {
		return numberLine;
	}

	public void setNumberLine(Integer numberLine) {
		this.numberLine = numberLine;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
