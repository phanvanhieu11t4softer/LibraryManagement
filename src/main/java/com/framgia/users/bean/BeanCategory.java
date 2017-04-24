package com.framgia.users.bean;

/**
 * BeanCategory.java description infor data insert table Book
 * 
 * @version 22/04/2017
 * @author phan.van.hieu@framgia.com
 */
public class BeanCategory {
	private int catSubId;
	private String categoriesCode;
	private String name;
	
	public BeanCategory() {
	}

	public int getCatSubId() {
		return catSubId;
	}

	public void setCatSubId(int catSubId) {
		this.catSubId = catSubId;
	}

	public String getCategoriesCode() {
		return categoriesCode;
	}

	public void setCategoriesCode(String categoriesCode) {
		this.categoriesCode = categoriesCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
