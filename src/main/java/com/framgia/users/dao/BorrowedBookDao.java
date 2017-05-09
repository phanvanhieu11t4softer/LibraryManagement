package com.framgia.users.dao;

import java.util.List;

import com.framgia.users.model.BorrowedDetails;
import com.framgia.users.model.Borroweds;
import com.framgia.util.ConditionSearchBorrowed;

public interface BorrowedBookDao {
	List<Borroweds> findByCondition(ConditionSearchBorrowed condition);

	Borroweds findByIdBorrowed(int idBorrowed);

	Borroweds update(Borroweds mBorrowed);
	
	BorrowedDetails updateBorrowedDetails(BorrowedDetails mBorrowedDetails);
}
