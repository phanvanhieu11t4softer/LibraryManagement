package com.framgia.users.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.framgia.users.model.Categories;
import com.framgia.users.model.ConstantModel;

/**
 * CategoryDAOImpl.java
 * 
 * @version 19/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Repository("categoryDAO")
public class CategoryDAOImpl extends AbstractDao<Integer, Categories> implements ConstantModel, CategoryDAO {

	@SuppressWarnings("unchecked")
	@Override
	public Categories findCategoryId(String categoryId) {
		List<Categories> users = new ArrayList<Categories>();

		users = getOpenSession().createQuery("from Categories where categoriesId=:categoriesId and deleteFlag=:delFlg")
				.setParameter("categoriesId", Integer.parseInt(categoryId))
				.setParameter("delFlg", ConstantModel.DEL_FLG).list();

		if (users.size() > 0) {

			return users.get(0);
		} else {

			return null;
		}
	}
}
