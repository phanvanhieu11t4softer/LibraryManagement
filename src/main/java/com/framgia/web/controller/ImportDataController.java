package com.framgia.web.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.framgia.users.bean.ErrorInfo;
import com.framgia.users.bean.FileFormInfo;
import com.framgia.users.service.ImportDataService;
import com.framgia.util.Constant;
import com.framgia.util.Helpers;

/**
 * ManagerBookController.java
 * 
 * @version 21/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Controller
public class ImportDataController {

	@Autowired
	MessageSource messageSource;
	
	@Autowired
	ImportDataService importDataService;

	/**
	 * 
	 * @param m
	 * @return
	 */
	@RequestMapping(value = { "/manageImportData" }, method = RequestMethod.GET)
	public ModelAndView home(Model m) {

		ModelAndView model = new ModelAndView();
		model.setViewName("importData");

		return model;
	}

	/**
	 * 
	 * @param dataImportBean
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/importData")
	public ModelAndView pageImportData(@ModelAttribute("dataImportBean") FileFormInfo dataImportBean,
			RedirectAttributes redirectAttributes, Model m) throws FileNotFoundException {
		ModelAndView model = new ModelAndView();

		List<ErrorInfo> errorInfoList = new ArrayList<ErrorInfo>();
		String errorFormat = null;
		Boolean check = true;
		MultipartFile fileUpload = dataImportBean.getFileImport();

		// Check empty Name Table
		if (Helpers.isEmpty(dataImportBean.getNameTable())) {
			check = false;
			m.addAttribute("error_table", messageSource.getMessage("choose_table_name", null, Locale.getDefault()));
			model.setViewName("importData");
		}

		// Check empty File
		if (Helpers.isEmpty(dataImportBean.getFileImport().toString())) {
			check = false;
			m.addAttribute("error_file", messageSource.getMessage("choose_file", null, Locale.getDefault()));
			model.setViewName("importData");
		} else {
			// Check format File
			if (!Helpers.checkFormatFile(fileUpload.getOriginalFilename(), Constant.CONSTANT_CSV)) {
				check = false;
				m.addAttribute("error_file", messageSource.getMessage("choose_file_csv", null, Locale.getDefault()));
				model.setViewName("importData");
			}
		}

		// Check checkValidation true
		if (check) {
			errorFormat = importDataService.checkFormatFile(dataImportBean);
			if (errorFormat != null) {
				m.addAttribute("err_data", errorFormat);
				model.setViewName("importData");
				return model;
			} else {
				errorInfoList = importDataService.checkDataFile(dataImportBean);
			}

			if (errorInfoList.size() > 0) {
				m.addAttribute("listError", errorInfoList);
				model.setViewName("importData");
			} else {
				
				// Get name login
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				String userName = auth.getName();
				
				// Import data into table Books
				int record = importDataService.importData(dataImportBean, userName);
				if (record == 1) {
					m.addAttribute("success", messageSource.getMessage("import_success", null, Locale.getDefault())
							.replace(Constant.DEFAULT_VALUE_MSG, Constant.TB_BOOKS));
				} else {
					m.addAttribute("fail", messageSource.getMessage("import_fail", null, Locale.getDefault())
							.replace(Constant.DEFAULT_VALUE_MSG, Constant.TB_BOOKS));
				}
				model.setViewName("importData");
			}

		}
		return model;
	}
}
