package com.framgia.users.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framgia.users.bean.BorrowedInfo;
import com.framgia.users.dao.BorrowedBookDAO;
import com.framgia.users.model.Book;
import com.framgia.users.model.BorrowedDetails;
import com.framgia.users.model.Borroweds;
import com.framgia.users.model.ConstantModel;
import com.framgia.util.ConditionSearchBorrowed;
import com.framgia.util.ConvertDataModelAndBean;
import com.framgia.util.DateUtil;

/**
 * ManagementBorrowedBookServiceImpl.java
 * 
 * @version 03/05/2017
 * @author vu.thi.tran.van@framgia.com
 * 
 */
@Service("managementBorrowedBookService")
public class ManagementBorrowedBookServiceImpl implements ManagementBorrowedBookService {

	// log
	private static final Logger logger = Logger.getLogger(ManagementBorrowedBookServiceImpl.class);

	@Autowired
	private BorrowedBookDAO borrowedBookDao;

	@Override
	public List<BorrowedInfo> getBorrowedInfoByFindCondition(ConditionSearchBorrowed condition) {

		List<Borroweds> mBorrowed = borrowedBookDao.findByCondition(condition);

		List<BorrowedInfo> bBorrowedInfo = new ArrayList<BorrowedInfo>();

		if (mBorrowed != null) {
			for (Borroweds item : mBorrowed) {

				BorrowedInfo borrowedInfo = new BorrowedInfo();
				borrowedInfo = ConvertDataModelAndBean.converBorrowedModelToBean(item);
				bBorrowedInfo.add(borrowedInfo);
			}
		}

		return bBorrowedInfo;
	}

	@Override
	public BorrowedInfo findByIdBorrowed(int idBorrowed) {

		Borroweds mBorrowed = borrowedBookDao.findByIdBorrowed(idBorrowed);
		BorrowedInfo bBorrowed = new BorrowedInfo();

		if (mBorrowed != null) {
			bBorrowed = ConvertDataModelAndBean.converBorrowedModelToBean(mBorrowed);
		}

		return bBorrowed;
	}

	@Transactional
	@Override
	public BorrowedInfo update(BorrowedInfo bBorrowed) {

		try {
			Borroweds updBorrowed = new Borroweds();

			updBorrowed = borrowedBookDao.findToUpdate(bBorrowed.getBorrowedId());

			switch (bBorrowed.getStatus()) {
			case ConstantModel.BOR_STATUS_APPROVE:
				updBorrowed.setStatus(ConstantModel.BOR_STATUS_APPROVE);
				break;
			case ConstantModel.BOR_STATUS_CANCEL:
				updBorrowed.setStatus(ConstantModel.BOR_STATUS_CANCEL);

				break;
			case ConstantModel.BOR_STATUS_BORRWED:
				updBorrowed.setStatus(ConstantModel.BOR_STATUS_BORRWED);
				updBorrowed.setDateBorrrowed(DateUtil.convertStringtoDate(bBorrowed.getDateBorrrowed()));
				break;
			case ConstantModel.BOR_STATUS_FINISH:
				updBorrowed.setStatus(ConstantModel.BOR_STATUS_FINISH);

				if (StringUtils.isNotBlank(bBorrowed.getDateBorrrowed().toString())) {
					updBorrowed.setDateBorrrowed(DateUtil.convertStringtoDate(bBorrowed.getDateBorrrowed()));
				}

				updBorrowed.setDateArrived(DateUtil.convertStringtoDate(bBorrowed.getDateArrived()));
				break;
			}

			// borrowedBookDao.update(updBorrowed);

			// Update detail
			if (updBorrowed.getBorrowedDetails() != null) {
				int index = 0;
				for (BorrowedDetails updateItemDetail : updBorrowed.getBorrowedDetails()) {

					if (bBorrowed.getBorrowedDetail() != null
						&& StringUtils.isNotBlank(bBorrowed.getBorrowedDetail().get(index).getStatus())) {
						updateItemDetail.setUserUpdate(bBorrowed.getUserUpdate());
						updateItemDetail.setDateUpdate(DateUtil.getDateNow());

						updateItemDetail.setStatus(bBorrowed.getBorrowedDetail().get(index).getStatus());

						// update book
						if (updateItemDetail.getBook() != null && 
								(ConstantModel.BOR_STATUS_CANCEL.equals(bBorrowed.getStatus())
									|| ConstantModel.BOR_STATUS_FINISH.equals(bBorrowed.getStatus())
									|| (ConstantModel.BOR_STATUS_BORRWED.equals(bBorrowed.getStatus())
											&& ConstantModel.BOR_DET_STATUS_REJECT.equals(bBorrowed.getBorrowedDetail().get(index).getStatus())))) {
							Book updBook = new Book();
							updBook = updateItemDetail.getBook();

							updBook.setUserUpdate(bBorrowed.getUserUpdate());
							updBook.setDateUpdate(DateUtil.getDateNow());
							updBook.setNumberRest(updBook.getNumberRest() + 1);
							updBook.setNumberBorrowed(updBook.getNumberBorrowed() - 1);

							updateItemDetail.setBook(updBook);
						}
						borrowedBookDao.updateBorrowedDetails(updateItemDetail);
					} else {
						if ((ConstantModel.BOR_STATUS_FINISH.equals(bBorrowed.getStatus())
								&& ConstantModel.BOR_DET_STATUS_ACCEPT.equals(updateItemDetail.getStatus()))) {

							// update book
							Book updBook = new Book();
							updBook = updateItemDetail.getBook();

							updBook.setUserUpdate(bBorrowed.getUserUpdate());
							updBook.setDateUpdate(DateUtil.getDateNow());
							updBook.setNumberRest(updBook.getNumberRest() + 1);
							updBook.setNumberBorrowed(updBook.getNumberBorrowed() - 1);

							updateItemDetail.setBook(updBook);

							borrowedBookDao.updateBorrowedDetails(updateItemDetail);
						}
					}
					index++;
				}
			}
			borrowedBookDao.update(updBorrowed);

			return ConvertDataModelAndBean.converBorrowedModelToBean(updBorrowed);

		} catch (Exception e) {
			logger.error("Error update borrowed: ", e);
			return null;
		}
	}
}
