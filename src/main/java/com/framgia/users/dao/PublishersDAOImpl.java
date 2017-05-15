package com.framgia.users.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.framgia.users.model.ConstantModel;
import com.framgia.users.model.Publishers;

/**
 * PublishersDAOImpl.java
 * 
 * @version 19/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Repository("publishersDAO")
public class PublishersDAOImpl extends AbstractDao<Integer, Publishers> implements ConstantModel, PublishersDAO {

	@SuppressWarnings("unchecked")
	@Override
	public Publishers findPublishersId(String id) {
		List<Publishers> users = new ArrayList<Publishers>();

		users = getOpenSession().createQuery("from Publishers where publishersId=:publishersId and deleteFlag=:delFlg")
				.setParameter("publishersId", Integer.parseInt(id)).setParameter("delFlg", ConstantModel.DEL_FLG)
				.list();

		if (users.size() > 0) {

			return users.get(0);
		} else {

			return null;
		}

	}

}
