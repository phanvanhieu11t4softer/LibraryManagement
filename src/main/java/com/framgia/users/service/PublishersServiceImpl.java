package com.framgia.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framgia.users.bean.BeanPublishers;
import com.framgia.users.dao.PublishersDAO;
import com.framgia.users.model.Publishers;

/**
 * PublishersServiceImpl.java
 * 
 * @version 19/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Service
public class PublishersServiceImpl implements PublishersService {
	
	@ Autowired
	PublishersDAO publishersDAO;

	@Override
	@Transactional
	public List<Publishers> listPublishers() {
		return this.publishersDAO.listPublishers();
	}

	@Override
	@Transactional
	public Long findPublishersId(String id, String deleteFlag) {
		return this.publishersDAO.findPublishersId(id, deleteFlag);
	}
	
	@Override
	@Transactional
	public int addPublishers(List<BeanPublishers> listPublishers) {
		return this.publishersDAO.addPublishers(listPublishers);
	}
}
