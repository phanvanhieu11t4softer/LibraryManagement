package com.framgia.users.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.framgia.users.bean.PermissionInfo;
import com.framgia.users.bean.UserInfo;
import com.framgia.users.dao.PermissionDao;
import com.framgia.users.dao.UserDao;
import com.framgia.users.model.Permissions;
import com.framgia.users.model.Users;
import com.framgia.util.DateUtil;

/**
 * ManagementUsersServiceImpl.java
 * 
 * @version 18/04/2017
 * @author vu.thi.tran.van@framgia.com
 * 
 */
@Service("managementUsersService")
public class ManagementUsersServiceImpl implements ManagementUsersService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PermissionDao permissionDao;

	@Override
	public Users findByUserName(String username) {
		// TODO Auto-generated method stub
		return userDao.findByUserName(username);
	}

	@Override
	public List<UserInfo> findByUsersWithCondition(String txtName, int txtPermission) {
		// TODO Auto-generated method stub
		List<Users> userList = userDao.findByConditon(txtName, txtPermission);

		List<UserInfo> userInfo = new ArrayList<UserInfo>();

		for (Users item : userList) {

			UserInfo user = new UserInfo();

			user.setUserId(item.getUserId());
			user.setUserName(item.getUserName());
			user.setPassWord(item.getPassWord());
			user.setBirthDate(item.getBirthDate().toString());
			user.setName(item.getName());
			user.setAddress(item.getAddress());

			user.setPhone(item.getPhone());
			user.setPassWord(item.getPassWord());

			user.setSex("Male");
			if (StringUtils.isNotBlank(item.getSex()) && item.getSex().equals("0")) {
				user.setSex("Fmale");
			}
			user.setEmail(item.getEmail());
			user.setDeleteFlag(item.getDeleteFlag());
			user.setDateCreate(item.getDateCreate().toString());
			user.setUserCreate(item.getUserCreate());
			user.setDateUpdate(item.getDateUpdate().toString());
			user.setUserUpdate(item.getUserUpdate());
			PermissionInfo per = new PermissionInfo();
			per.setPermissionName(item.getPermissions().getPermissionName());
			user.setPermissions(per);

			userInfo.add(user);

		}

		return userInfo;
	}

	@Override
	public List<PermissionInfo> findByDelFlg() {

		// get value permission role for select box
		List<PermissionInfo> permissionInfo = new ArrayList<PermissionInfo>();

		List<Permissions> permissionList = permissionDao.findByDelFlg();

		for (Permissions item : permissionList) {

			PermissionInfo per = new PermissionInfo();

			per.setPermissionsId(item.getPermissionsId());
			per.setPermissionName(item.getPermissionName());

			permissionInfo.add(per);
		}

		return permissionInfo;
	}

	@Transactional
	@Override
	public int delLogicUser(int idUser, String userUpd, String dateUpdate) {
		int result = 0;
		if (userDao.delLogicUser(idUser, userUpd, DateUtil.convertStringtoDateTime(dateUpdate))) {
			result = 1;
			TransactionAspectSupport.currentTransactionStatus().isCompleted();

		} else {
			TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
		}

		return result;
	}

	@Override
	public UserInfo findByIdUser(int idUser) {

		UserInfo user = new UserInfo();

		Users userModel = userDao.findById(idUser);

		if (null != userModel && userModel.getUserId() != null) {
			user = converModelToBean(userModel);
		}

		// TODO Auto-generated method stub
		return user;
	}

	@Transactional
	@Override
	public UserInfo updateUser(UserInfo user) throws ParseException {
		// TODO Auto-generated method stub
		UserInfo userReturn = new UserInfo();

		Users userModel = new Users();
		
		userModel.setUserId(user.getUserId());
		userModel.setName(user.getName());
		Permissions per = new Permissions();
		per.setPermissionsId(user.getPermissions().getPermissionsId());
		userModel.setPermissions(per);
		userModel.setBirthDate(DateUtil.convertStringtoDate(user.getBirthDate()));
		userModel.setEmail(user.getEmail());
		userModel.setPhone(user.getPhone());
		userModel.setSex(user.getSex());
		userModel.setAddress(user.getAddress());
		userModel.setUserUpdate(user.getUserUpdate());
		userModel.setDateUpdate(DateUtil.convertStringtoDateTime(user.getDateUpdate()));
		userModel.setDateCreate(DateUtil.convertStringtoDateTime(user.getDateCreate()));
		userModel.setDeleteFlag(user.getDeleteFlag());
		userModel.setUserCreate(user.getUserCreate());
		userModel.setPassWord(user.getPassWord());
		userModel.setUserName(user.getUserName());

		Users userUpd = userDao.updateUser(userModel);
		if (null != userUpd && userUpd.getUserId() != null) {

			TransactionAspectSupport.currentTransactionStatus().isCompleted();

			userReturn = converModelToBean(userUpd);
			
			userReturn.setDateUpdate(DateUtil.convertDateTimetoString(userUpd.getDateUpdate()));
		} else {
			TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
		}

		return userReturn;
	}

	public UserInfo converModelToBean(Users mUser) {

		UserInfo bUser = new UserInfo();

		if (null != mUser && mUser.getUserId() != null) {
			if (null != mUser.getUserId()) {
				bUser.setUserId(mUser.getUserId());
			}
			bUser.setUserName(mUser.getUserName());
			bUser.setPassWord(mUser.getPassWord());
			if (null != mUser.getBirthDate()) {
			bUser.setBirthDate(mUser.getBirthDate().toString());
			}
			bUser.setName(mUser.getName());
			bUser.setAddress(mUser.getAddress());
			bUser.setPhone(mUser.getPhone());
			bUser.setPassWord(mUser.getPassWord());

			bUser.setSex("Male");
			if (StringUtils.isNotBlank(mUser.getSex()) && mUser.getSex().equals("0")) {
				bUser.setSex("Fmale");
			}
			bUser.setEmail(mUser.getEmail());
			bUser.setDeleteFlag(mUser.getDeleteFlag());

			if (null != mUser.getDateCreate()) {
			bUser.setDateCreate(mUser.getDateCreate().toString().substring(0, 19));
			}
			bUser.setUserCreate(mUser.getUserCreate());
			if (null != mUser.getDateUpdate()) {
			bUser.setDateUpdate(mUser.getDateUpdate().toString().substring(0, 19));
			}
			bUser.setUserUpdate(mUser.getUserUpdate());
			PermissionInfo perInfo = new PermissionInfo();

			perInfo.setPermissionName(mUser.getPermissions().getPermissionName());
			bUser.setPermissions(perInfo);
		}
		return bUser;
	}
}
