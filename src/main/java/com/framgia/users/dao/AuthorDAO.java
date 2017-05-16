package com.framgia.users.dao;

import com.framgia.users.model.Author;

/**
 * AuthorDAO.java
 * 
 * @version 24/04/2017
 * @author phan.van.hieu@framgia.com
 */
public interface AuthorDAO {

	// Find by AuthorID
	public Author findAuthorId(String authorsId);

	// Insert data into table Author
	public void insertAuthor(Author author);
}
