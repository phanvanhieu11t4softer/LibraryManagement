package com.framgia.users.dao;

import com.framgia.users.model.Categories;

/**
 * CategoryDAO.java
 * 
 * @version 19/04/2017
 * @author phan.van.hieu@framgia.com
 */
public interface CategoryDAO {

	// Find CategoryId
	public Categories findCategoryId(String categoryId);

	// Find CategorCode
	public Categories findCategoryCode(String categoryCode);

	// Insert data into table Category
	public void insertCategory(Categories categories);
}
