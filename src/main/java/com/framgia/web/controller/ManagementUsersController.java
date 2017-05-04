package com.framgia.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.framgia.users.bean.PermissionInfo;
import com.framgia.users.bean.UserInfo;
import com.framgia.users.service.MailService;
import com.framgia.users.service.ManagementUsersService;
import com.framgia.util.Constant;
import com.framgia.util.CsvFileWriter;
import com.framgia.util.DateUtil;

/**
 * ManagementUsersController.java
 * 
 * @version 18/04/2017
 * @author vu.thi.tran.van@framgia.com
 */
@Controller
public class ManagementUsersController {

	// log
	private static final Logger logger = Logger.getLogger(ManagementUsersController.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	ManagementUsersService managementUsersService;

	@Autowired
	MailService mailService;

	// File input stream
	private FileInputStream inputStream;

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtil.getSimpleDateFormat(), true));
	}

	@RequestMapping(value = "/managementUsers", method = RequestMethod.GET)
	public ModelAndView referencePage() {
		logger.info("call service: get list permission");

		// get value permission role for select box
		List<PermissionInfo> permissionInfo = managementUsersService.findByDelFlg();

		return new ModelAndView("managementUsers", "permissionInfo", permissionInfo);
	}

	@RequestMapping(value = "/managementUsers/search", method = RequestMethod.POST)
	public @ResponseBody List<UserInfo> findByCondition(@RequestParam(value = "txtName") String txtName,
	        @RequestParam(value = "txtPermission") int txtPermission, ModelMap model) {
		logger.info("call service; get lisst user");

		List<UserInfo> userInfo = managementUsersService.findByUsersWithCondition(txtName, txtPermission);

		return userInfo;
	}

	@RequestMapping(value = "/managementUsers/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public int delLogicUser(@PathVariable("id") int idUser, @RequestParam(value = "dateUpd") String dateUpd) {
		logger.info("call service: delete user");

		return managementUsersService.delLogicUser(idUser, getUserName(), dateUpd);

	}

	@RequestMapping(value = "/managementUsers/detail/{id}", method = RequestMethod.GET)
	public ModelAndView detailPage(@PathVariable("id") int idUser) {
		logger.info("call service: get user and get list permission");

		// get infor user
		UserInfo user = managementUsersService.findByIdUser(idUser);

		// get value permission role for select box
		List<PermissionInfo> permissionInfo = managementUsersService.findByDelFlg();

		// render page detail user
		ModelAndView mv = new ModelAndView("managementUserDetail", "user", user);

		mv.addObject("permissionInfo", permissionInfo);

		return mv;
	}

	@RequestMapping(value = "/managementUsers/update", method = RequestMethod.POST)
	public @ResponseBody UserInfo updateUsser(@ModelAttribute("user") UserInfo user, BindingResult result,
			ModelMap model) {
		logger.info("call service: to update user");
		
		user.setUserUpdate(getUserName());
		UserInfo resultUpd = null;
		try {
			resultUpd = managementUsersService.updateUser(user);

		} catch (Exception e) {
			logger.error("Error update user: " + e.getMessage());
		}

		return resultUpd;
	}

	@RequestMapping(value = "/managementUsers/downloadCSV", method = RequestMethod.POST)
	public void downloadCSV(HttpServletResponse response, @RequestParam(value = "txtName") String txtName,
			@RequestParam(value = "txtPermission") int txtPermission, ModelMap model) throws IOException {
		logger.info("call service: download csv with condition search");
		
		String FILE_PATH = System.getProperty("java.io.tmpdir");
		String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
		String filePath = FILE_PATH + "/" + Constant.NAME_REPORT_USER + curTime + Constant.EXTERNAL;

		// get value
		List<UserInfo> userInfo = managementUsersService.findByUsersWithCondition(txtName, txtPermission);
		
		// Write CSV
		CsvFileWriter.writeUsersCsv(filePath, userInfo);

		File downloadFile = new File(filePath);
		OutputStream outStream = null;
		try {
			inputStream = new FileInputStream(downloadFile);

			response.setContentLength((int) downloadFile.length());

			// response header details
			String headerKey = "Content-Disposition";
			String fileName = URLEncoder.encode(downloadFile.getName(), "UTF-8");
			response.setHeader(headerKey, "attachment; filename*=UTF-8''" + fileName);

			// response
			outStream = response.getOutputStream();
			IOUtils.copy(inputStream, outStream);

		} catch (Exception e) {
			logger.error("Erro download csv: " + e.getMessage());
		} finally {
			try {
				if (null != inputStream)
					inputStream.close();
				if (null != inputStream)
					outStream.close();

				// Delete file on server
				downloadFile.delete();
			} catch (IOException e) {
				logger.error("Erro download csv: " + e.getMessage());
			}
		}
	}

	public String getUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		return auth.getName();
	}

}
