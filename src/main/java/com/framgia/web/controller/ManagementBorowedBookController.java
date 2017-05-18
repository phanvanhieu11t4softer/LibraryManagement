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

import com.framgia.users.bean.BorrowedInfo;
import com.framgia.users.service.MailService;
import com.framgia.users.service.ManagementBorrowedBookService;
import com.framgia.util.ConditionSearchBorrowed;
import com.framgia.util.Constant;
import com.framgia.util.CsvFileWriter;
import com.framgia.util.DateUtil;

/**
 * ManagementUsersController.java
 * 
 * @version 03/05/2017
 * @author vu.thi.tran.van@framgia.com
 */
@Controller
public class ManagementBorowedBookController {

	// log
	private static final Logger logger = Logger.getLogger(ManagementBorowedBookController.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	ManagementBorrowedBookService managementBorrowedBookService;

	@Autowired
	MailService mailService;

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtil.getSimpleDateFormat(), true));
	}

	@RequestMapping(value = "/managementBorrowed", method = RequestMethod.GET)
	public ModelAndView referencePage() {

		// return new ModelAndView("managementBorrowed", "borrowedInfoList",
		// borrowedInfoList);
		return new ModelAndView("managementBorrowed");
	}

	@RequestMapping(value = "/managementBorrowed/search", method = RequestMethod.POST)
	public @ResponseBody List<BorrowedInfo> findByCondition(
			@RequestParam(value = "txtBorrowedCode") String txtBorrowedCode,
			@RequestParam(value = "txtStatus") String txtStatus,
			@RequestParam(value = "txtIntenDateBor") String txtIntenDateBor,
			@RequestParam(value = "txtIntenDatePay") String txtIntenDatePay,
			@RequestParam(value = "txtDateBor") String txtDateBor,
			@RequestParam(value = "txtDatePay") String txtDatePay, ModelMap model) {
		logger.info("call service: get list borrowed");

		ConditionSearchBorrowed condition = new ConditionSearchBorrowed(txtBorrowedCode, txtStatus, txtIntenDateBor,
				txtIntenDatePay, txtDateBor, txtDatePay);

		// get value permission role for select box
		List<BorrowedInfo> borrowedInfo = managementBorrowedBookService.getBorrowedInfoByFindCondition(condition);

		return borrowedInfo;
	}

	@RequestMapping(value = "managementBorrowed/downloadCSV", method = RequestMethod.POST)
	public void downloadCondition(HttpServletResponse response,
			@RequestParam(value = "txtBorrowedCode") String txtBorrowedCode,
			@RequestParam(value = "txtStatus") String txtStatus,
			@RequestParam(value = "txtIntenDateBor") String txtIntenDateBor,
			@RequestParam(value = "txtIntenDatePay") String txtIntenDatePay,
			@RequestParam(value = "txtDateBor") String txtDateBor,
			@RequestParam(value = "txtDatePay") String txtDatePay, ModelMap model) throws IOException {
		logger.info("call service: download csv with condition search");

		ConditionSearchBorrowed condition = new ConditionSearchBorrowed(txtBorrowedCode, txtStatus, txtIntenDateBor,
				txtIntenDatePay, txtDateBor, txtDatePay);

		// get value
		List<BorrowedInfo> borrowedInfo = managementBorrowedBookService.getBorrowedInfoByFindCondition(condition);

		String FILE_PATH = System.getProperty("java.io.tmpdir");
		String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
		String filePath = FILE_PATH + "/" + Constant.NAME_REPORT_BORROWED + curTime + Constant.EXTERNAL;

		// Write CSV
		try {
			CsvFileWriter.writeBorrowedCsv(filePath, borrowedInfo);

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

	@RequestMapping(value = "/managementBorrowed/detail/{id}", method = RequestMethod.GET)
	public ModelAndView detailPage(@PathVariable("id") int idBorrowed) {
		logger.info("call service: get detail borrowed");

		// get infor borrowed
		BorrowedInfo borrowedInfo = managementBorrowedBookService.findByIdBorrowed(idBorrowed);

		// render page detail borrowed
		ModelAndView mv = new ModelAndView("managementBorrowedDetail", "borrowedInfo", borrowedInfo);

		return mv;
	}

	@RequestMapping(value = "/managementBorrowed/update", method = RequestMethod.POST)
	public String updatePage(@ModelAttribute("borrowedInfo") BorrowedInfo borrowedInfo, ModelMap model,
			RedirectAttributes redirectAttributes) {
		logger.info("call service: to update borrowed");

		if (null == getUserName()) {
			logger.info("Cannot get username.");
			return "redirect:/login";
		}

		borrowedInfo.setUserUpdate(getUserName());

		// get infor borrowed
		BorrowedInfo updBorroweds = managementBorrowedBookService.update(borrowedInfo);

		if (null != updBorroweds) {
			redirectAttributes.addFlashAttribute("messageUpd",
					messageSource.getMessage("update_success", null, Locale.getDefault()));
			mailService.sendEmailBorrowed(updBorroweds);

		} else {
			redirectAttributes.addFlashAttribute("messageUpd",
					messageSource.getMessage("update_error", null, Locale.getDefault()));

		}

		return "redirect:/managementBorrowed/detail/" + borrowedInfo.getBorrowedId();
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
