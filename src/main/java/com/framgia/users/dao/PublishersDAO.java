package com.framgia.users.dao;

import java.util.List;

import com.framgia.users.bean.BeanPublishers;
import com.framgia.users.model.Publishers;

/**
 * PublishersDAO.java
 * 
 * @version 19/04/2017
 * @author phan.van.hieu@framgia.com
 */
public interface PublishersDAO {

	public List<Publishers> listPublishers();
	public Long findPublishersId(String id, String deleteFlag);
	public int addPublishers(List<BeanPublishers> listPublishers);
}
