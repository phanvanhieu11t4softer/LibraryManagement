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
}
