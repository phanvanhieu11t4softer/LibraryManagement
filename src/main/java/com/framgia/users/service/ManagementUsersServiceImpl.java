package com.framgia.users.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framgia.users.bean.PermissionInfo;
import com.framgia.users.bean.UserInfo;
import com.framgia.users.dao.PermissionDAO;
import com.framgia.users.dao.UserDAO;
import com.framgia.users.model.ConstantModel;
import com.framgia.users.model.Permissions;
import com.framgia.users.model.Users;
import com.framgia.util.Constant;
import com.framgia.util.ConvertDataModelAndBean;
import com.framgia.util.DateUtil;
import com.framgia.util.Helpers;

/**
 * ManagementUsersServiceImpl.java
 * 
 * @version 18/04/2017
 * @author vu.thi.tran.van@framgia.com
 * 
 */
@Service("managementUsersService")
public class ManagementUsersServiceImpl implements ManagementUsersService {

	// log
	private static final Logger logger = Logger.getLogger(ManagementUsersServiceImpl.class);

	@Autowired
	private UserDAO userDao;

	@Autowired
	private PermissionDAO permissionDao;

	@Override
	public Users findByUserName(String username) {
		// TODO Auto-generated method stub
		return userDao.findByUserName(username);
	}

	@Override
	public List<UserInfo> findByUsersWithCondition(String txtName, int txtPermission) {
		// TODO Auto-generated method stub
		List<Users> userList = userDao.findByConditon(txtName, txtPermission);
		if (!Helpers.isEmpty(userList)) {
			List<UserInfo> userInfo = new ArrayList<UserInfo>();

			for (Users item : userList) {

				UserInfo user = new UserInfo();

				user = ConvertDataModelAndBean.converUserModelToBean(item);

				userInfo.add(user);

			}
			return userInfo;
		}
		return null;
	}

	@Override
	public List<PermissionInfo> findByDelFlg() {

		// get value permission role for select box
		List<PermissionInfo> permissionInfo = new ArrayList<PermissionInfo>();

		List<Permissions> permissionList = permissionDao.findByDelFlg();
		if (!Helpers.isEmpty(permissionList)) {
			for (Permissions item : permissionList) {

				PermissionInfo per = new PermissionInfo();

				per.setPermissionsId(item.getPermissionsId());
				per.setPermissionName(item.getPermissionName());

				permissionInfo.add(per);
			}
		}

		return permissionInfo;
	}

	@Transactional
	@Override
	public int delLogicUser(int idUser, String userUpd) {
		try {
			Users updUser = userDao.findToUpdate(idUser, null, null, null, null);

			if (updUser == null) {
				return 0;
			}
			updUser.setUserName(null);
			updUser.setDeleteFlag(ConstantModel.DEL_FLG_DEL);
			updUser.setUserUpdate(userUpd);
			updUser.setDateUpdate(DateUtil.getDateNow());

			userDao.updateUser(updUser);
			return 1;

		} catch (Exception e) {
			logger.error("Error delete logic: ", e);
		}
		return 0;
	}

	@Override
	public UserInfo findByIdUser(int idUser) {

		UserInfo user = new UserInfo();

		Users userModel = userDao.findById(idUser);

		if (null != userModel && userModel.getUserId() != null) {
			user = ConvertDataModelAndBean.converUserModelToBean(userModel);
		}

		// TODO Auto-generated method stub
		return user;
	}

	@Transactional
	@Override
	public boolean updateUser(UserInfo user) {
		try {

			Users updUser = userDao.findToUpdate(user.getUserId(), null, null, null, null);

			if (updUser == null) {
				return false;
			}

			if (StringUtils.isNotBlank(user.getName())) {
				updUser.setName(user.getName());
			}

			if (0 < user.getPermissions().getPermissionsId()) {
				Permissions per = new Permissions();
				per.setPermissionsId(user.getPermissions().getPermissionsId());
				updUser.setPermissions(per);
			}

			if (StringUtils.isNotBlank(user.getBirthDate())) {
				updUser.setBirthDate(DateUtil.convertStringtoDate(user.getBirthDate()));
			}

			if (StringUtils.isNotBlank(user.getAddress())) {
				updUser.setAddress(user.getAddress());
			}

			if (StringUtils.isNotBlank(user.getPhone())) {
				updUser.setPhone(user.getPhone());
			}

			if (StringUtils.isNotBlank(user.getSex())) {
				updUser.setSex(user.getSex());
			}

			if (StringUtils.isNotBlank(user.getEmail())) {
				updUser.setEmail(user.getEmail());
			}

			updUser.setUserUpdate(user.getUserUpdate());

			updUser.setDateUpdate(DateUtil.getDateNow());

			userDao.updateUser(updUser);

			return true;
		} catch (ParseException e) {
			logger.error("Error update logic: ", e);
			return false;
		}
	}

	@Transactional
	@Override
	public UserInfo updateForgotPassword(String email) {

		try {
			Users updUser = userDao.findToUpdate(0, email, null, null, null);

			String token = UUID.randomUUID().toString();

			String passwordConvert = passwordEncoderToString(token);

			updUser.setUserUpdate(updUser.getUserName());
			updUser.setPassWord(passwordEncoderToString(passwordConvert));
			updUser.setTokenResetPassword(token);
			updUser.setUserUpdate(Constant.USER_UPDATE_DEFAULT);
			updUser.setDateUpdate(DateUtil.getDateNow());

			userDao.updateUser(updUser);
			return ConvertDataModelAndBean.converUserModelToBean(updUser);

		} catch (Exception e) {
			logger.error("Error reset pass to email: ", e);
			return null;
		}
	}

	@Transactional
	@Override
	public boolean updatePassword(int id, String token, String currencePass, String password) {

		try {
			Users updUser = userDao.findToUpdate(id, null, null, currencePass, token);
			updUser.setUserId(id);
			updUser.setUserUpdate(updUser.getUserName());
			updUser.setPassWord(passwordEncoderToString(password));
			updUser.setTokenResetPassword(UUID.randomUUID().toString());
			updUser.setDateUpdate(DateUtil.getDateNow());

			userDao.updateUser(updUser);
			return true;
		} catch (ParseException e) {
			logger.error("Error reset pass to form: ", e);
			return false;
		}
	}

	// encode password
	public String passwordEncoderToString(String token) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(token);
		return hashedPassword;
	}

}
