package com.framgia.users.service;

import java.util.List;

import com.framgia.users.bean.BeanPublishers;
import com.framgia.users.model.Publishers;

/**
 * PublishersService.java
 * 
 * @version 19/04/2017
 * @author phan.van.hieu@framgia.com
 */
public interface PublishersService {

	public List<Publishers> listPublishers();
	public Long findPublishersId(String id, String deleteFlag);
	public int addPublishers(List<BeanPublishers> listPublishers);
	
}
