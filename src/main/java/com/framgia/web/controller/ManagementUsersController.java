package com.framgia.web.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public int delLogicUser(@PathVariable("id") int idUser) {
		logger.info("call service: delete user");

		return managementUsersService.delLogicUser(idUser, getUserName());

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
	public String updateUsser(@ModelAttribute("user") UserInfo user, BindingResult result,
	        RedirectAttributes redirectAttributes, ModelMap model) {
		logger.info("call service: to update user");

		if (null == getUserName()) {
			logger.info("Cannot get username.");
			return "redirect:/login";
		}

		user.setUserUpdate(getUserName());
		try {
			boolean flagUpdate = managementUsersService.updateUser(user);

			if (flagUpdate) {
				redirectAttributes.addFlashAttribute("messageUpd",
				        messageSource.getMessage("update_success", null, Locale.getDefault()));
			} else {
				redirectAttributes.addFlashAttribute("messageUpd",
				        messageSource.getMessage("update_error", null, Locale.getDefault()));
			}
		} catch (Exception e) {
			logger.error("Error update user: ", e);
		}

		return "redirect:/managementUsers/detail/" + user.getUserId();
	}

	@RequestMapping(value = "/managementUsers/downloadCSV", method = RequestMethod.POST)
	public void downloadCSV(HttpServletResponse response, @RequestParam(value = "txtName") String txtName,
	        @RequestParam(value = "txtPermission") int txtPermission, ModelMap model) throws IOException {
		logger.info("call service: download csv with condition search");

		// get value
		List<UserInfo> userInfo = managementUsersService.findByUsersWithCondition(txtName, txtPermission);

		String FILE_PATH = System.getProperty("java.io.tmpdir");
		String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
		String filePath = FILE_PATH + "/" + Constant.NAME_REPORT_USER + curTime + Constant.EXTERNAL;

		// Write CSV
		try {
			CsvFileWriter.writeUsersCsv(filePath, userInfo);

		} catch (FileNotFoundException e) {
			logger.error("Error write csv: ", e);
		}

		File downloadFile = new File(filePath);

		response.setContentLength((int) downloadFile.length());

		// response header details
		String headerKey = "Content-Disposition";
		String fileName = URLEncoder.encode(downloadFile.getName(), "UTF-8");
		response.setHeader(headerKey, "attachment; filename*=UTF-8''" + fileName);

		// response
		OutputStream outStream = response.getOutputStream();

		CsvFileWriter.deleteFileInServer(downloadFile, outStream);
	}

	public String getUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			logger.info("username: " + userDetail.getUsername());

			return userDetail.getUsername();
		} else {
			return null;
		}
	}
}
