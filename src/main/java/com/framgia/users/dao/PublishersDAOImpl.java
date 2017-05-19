package com.framgia.users.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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

	// log
	private static final Logger logger = Logger.getLogger(PublishersDAOImpl.class);

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

	@Override
	public void insertPublisher(Publishers publishers) {

		// Insert data into table Books
		getOpenSession().saveOrUpdate(publishers);
		logger.info("Insert success.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Publishers> listPublisher() {
		List<Publishers> publisherList = getSession().createQuery("from Publishers where deleteFlag = '0'").list();
		return publisherList;
	}

}
