package com.framgia.users.service;

import java.util.List;

import com.framgia.users.bean.BeanCategory;
import com.framgia.users.model.Categories;

/**
 * CategoryService.java
 * 
 * @version 19/04/2017
 * @author phan.van.hieu@framgia.com
 */
public interface CategoryService {

	public List<Categories> listCategory();
	public Long findCategoryId(String categoryId, String categorySubId, String deleteFlag);
	public Long findCatSubId(String catSubId, String deleteFlag);
	public Long findCategoryCode(String categoryCode, String deleteFlag);
	public int addCategories(List<BeanCategory> listCategory);
}
