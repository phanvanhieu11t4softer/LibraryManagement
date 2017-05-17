package com.framgia.users.dao;

import java.util.List;

import com.framgia.users.model.BorrowedDetails;
import com.framgia.users.model.Borroweds;
import com.framgia.util.ConditionSearchBorrowed;

public interface BorrowedBookDAO {
	List<Borroweds> findByCondition(ConditionSearchBorrowed condition);

	Borroweds findByIdBorrowed(int idBorrowed);

	void update(Borroweds mBorrowed);
	
	void updateBorrowedDetails(BorrowedDetails mBorrowedDetails);
	
	Borroweds findToUpdate(int borrowedId);
}
