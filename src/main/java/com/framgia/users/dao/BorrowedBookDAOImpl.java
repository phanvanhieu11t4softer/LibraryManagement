package com.framgia.users.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.framgia.users.model.BorrowedDetails;
import com.framgia.users.model.Borroweds;
import com.framgia.users.model.ConstantModel;
import com.framgia.util.ConditionSearchBorrowed;

/**
 * ManagementUsersController.java
 * 
 * @version 03/05/2017
 * @author vu.thi.tran.van@framgia.com
 */
@Repository("borrowedBookDao")
public class BorrowedBookDAOImpl extends AbstractDao<Integer, Borroweds> implements BorrowedBookDAO {

	// log
	private static final Logger logger = Logger.getLogger(BorrowedBookDAOImpl.class);

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Borroweds> findByCondition(ConditionSearchBorrowed condition) {
		logger.info("Search list borroweds");

		Session session = getOpenSession();
		List<Borroweds> queryList = new ArrayList<Borroweds>();

		try {

			String sql = "FROM Borroweds where deleteFlag = :deleteFlag";

			// add condition
			if (StringUtils.isNotEmpty(condition.getTxtBorrowedCode())) {
				sql = sql + " AND borrowedCode LIKE :borrowedCode";
			}

			if (StringUtils.isNotEmpty(condition.getTxtStatus())) {
				sql = sql + " AND status = :status";
			}

			if (StringUtils.isNotEmpty(condition.getTxtIntenDateBorrowed())) {
				sql = sql + " AND dIntendBorrowed = :dIntendBorrowed";
			}

			if (StringUtils.isNotEmpty(condition.getTxtIntenDatePayment())) {
				sql = sql + " AND dIntendArrived = :dIntendArrived";
			}

			if (StringUtils.isNotEmpty(condition.getTxtDateBorrowed())) {
				sql = sql + " AND dateBorrrowed = :dateBorrrowed";
			}

			if (StringUtils.isNotEmpty(condition.getTxtDatePayment())) {
				sql = sql + " AND dateArrived = :dateArrived";
			}

			Query query = session.createQuery(sql);

			// set param
			query.setParameter("deleteFlag", ConstantModel.DEL_FLG);

			if (StringUtils.isNotEmpty(condition.getTxtBorrowedCode())) {
				query.setParameter("borrowedCode", condition.getTxtBorrowedCode());
			}

			if (StringUtils.isNotEmpty(condition.getTxtStatus())) {
				query.setParameter("status", condition.getTxtStatus());
			}

			if (StringUtils.isNotEmpty(condition.getTxtIntenDateBorrowed())) {
				query.setParameter("dIntendBorrowed", condition.getTxtIntenDateBorrowed());
			}

			if (StringUtils.isNotEmpty(condition.getTxtIntenDatePayment())) {
				query.setParameter("dIntendArrived", condition.getTxtIntenDatePayment());
			}

			if (StringUtils.isNotEmpty(condition.getTxtDateBorrowed())) {
				query.setParameter("dateBorrrowed", condition.getTxtDateBorrowed());
			}

			if (StringUtils.isNotEmpty(condition.getTxtDatePayment())) {
				query.setParameter("dateArrived", condition.getTxtDatePayment());
			}

			queryList = query.list();
			logger.info("End search.");

			return queryList;

		} catch (Exception e) {
			logger.error("Search list borrowed error: ", e);

			return null;
		}
	}

	@Override
	public Borroweds findByIdBorrowed(int idBorrowed) {
		logger.info("Search borrowed by Id");

		Borroweds borrowed = new Borroweds();
		try {
			Session session = getOpenSession();
			String sql = "FROM Borroweds where deleteFlag = :deleteFlag AND borrowedId = :borrowedId";

			Query query = session.createQuery(sql);

			query.setParameter("deleteFlag", ConstantModel.DEL_FLG);

			query.setParameter("borrowedId", idBorrowed);

			if (query.list().size() > 0) {
				borrowed = (Borroweds) query.list().get(0);
			}

			logger.info("Search end.");
			return borrowed;

		} catch (Exception e) {
			logger.error("Search find by id error: ", e);

			return null;
		}
	}
	@Override
	public void update(Borroweds mBorrowed) {
		getSession().saveOrUpdate(mBorrowed);

	}

	@Override
	public void updateBorrowedDetails(BorrowedDetails mBorrowedDetails) {
		logger.info("Update borrowed detail.");
		getSession().saveOrUpdate(mBorrowedDetails);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public Borroweds findToUpdate(int borrowedId) {
		logger.info("Search borrowed to update");
		Criteria crit = getSession().createCriteria(Borroweds.class);
		crit.add(Restrictions.eq("deleteFlag", ConstantModel.DEL_FLG));
		crit.add(Restrictions.eq("borrowedId", borrowedId));
		crit.setLockMode(LockMode.UPGRADE);

		List<Borroweds> items = crit.list();

		if (items != null && items.size() > 0) {
			return items.get(0);
		}
		return null;
	}

	@Override
	public Borroweds getLastRecord() {
		logger.info("Search borrowed by Id");

		Borroweds borrowed = new Borroweds();
		try {
			Session session = getSession();
			String sql = "FROM Borroweds where deleteFlag = :deleteFlag order by borrowedId DESC";

			Query query = session.createQuery(sql);

			query.setParameter("deleteFlag", ConstantModel.DEL_FLG);

			query.setMaxResults(1);

			borrowed = (Borroweds) query.uniqueResult();

			logger.info("Search end.");
			
			return borrowed;

		} catch (Exception e) {
			logger.error("Search find by id error: " + e.getMessage());

			return null;
		}
	}

	@Override
	public int insertBorrowBook(Borroweds borroweds) {
		
		// Insert data into table Books
		getOpenSession().saveOrUpdate(borroweds);
		logger.info("Insert success.");
		return borroweds.getBorrowedId();
	}
}
