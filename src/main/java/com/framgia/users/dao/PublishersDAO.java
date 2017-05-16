package com.framgia.users.dao;

import com.framgia.users.model.Publishers;

/**
 * PublishersDAO.java
 * 
 * @version 19/04/2017
 * @author phan.van.hieu@framgia.com
 */
public interface PublishersDAO {

	// Find PublishersId
	public Publishers findPublishersId(String id);

	// Insert data into table Publishers
	public void insertPublisher(Publishers publishers);
}
