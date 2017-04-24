package com.framgia.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framgia.users.bean.BeanCategory;
import com.framgia.users.dao.CategoryDAO;
import com.framgia.users.model.Categories;

/**
 * CategoryServiceImpl.java
 * 
 * @version 19/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Service
public class CategoryServiceImpl implements CategoryService {
	
	@ Autowired
	CategoryDAO categoryDAO;

	@Override
	@Transactional
	public List<Categories> listCategory() {
		return this.categoryDAO.listCategory();
	}

	@Override
	@Transactional
	public Long findCategoryId(String categoryId, String categorySubId, String deleteFlag) {
		return this.categoryDAO.findCategoryId(categoryId, categorySubId, deleteFlag);
	}
	
	@Override
	@Transactional
	public Long findCatSubId(String catSubId, String deleteFlag) {
		return this.categoryDAO.findCatSubId(catSubId, deleteFlag);
	}
	
	@Override
	@Transactional
	public Long findCategoryCode(String categoryCode, String deleteFlag) {
		return this.categoryDAO.findCategoryCode(categoryCode, deleteFlag);
	}
	
	@Override
	@Transactional
	public int addCategories(List<BeanCategory> listCategory) {
		return this.categoryDAO.addCategories(listCategory);
	}
}
