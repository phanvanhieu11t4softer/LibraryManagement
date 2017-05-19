package com.framgia.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.framgia.users.bean.AuthorInfo;
import com.framgia.users.bean.CategoryInfo;
import com.framgia.users.bean.PublisherInfo;
import com.framgia.users.bean.UserInfo;
import com.framgia.users.service.BorrowBookService;
import com.framgia.users.service.MailService;
import com.framgia.users.service.ManagementUsersService;

/**
 * MainController.java
 * 
 * @version 16/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Controller
public class MainController {

	// log
	private static final Logger logger = Logger.getLogger(ManagementBorowedBookController.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	MailService mailService;

	@Autowired
	ManagementUsersService managementUsersService;
	
	@Autowired
	BorrowBookService borrowBookService;

	@RequestMapping(value = { "/home" }, method = RequestMethod.GET)
	public ModelAndView home(Model m) {

		// Get Authentication 
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		ModelAndView model = new ModelAndView();
		
		if("[ROLE_STUDENT]".equals(auth.getAuthorities().toString())){
			List<CategoryInfo> categoryInfoList = new ArrayList<CategoryInfo>();
			List<AuthorInfo> authorInfoList = new ArrayList<AuthorInfo>();
			List<PublisherInfo> publisherInfoList = new ArrayList<PublisherInfo>();
			
			categoryInfoList = borrowBookService.listCategory();
			authorInfoList = borrowBookService.listAuthor();
			publisherInfoList = borrowBookService.listPublisher();
			
	    	m.addAttribute("listCategory", categoryInfoList);
	    	m.addAttribute("authorInfoList", authorInfoList);
	    	m.addAttribute("publisherInfoList", publisherInfoList);
	    	
	    	model.setViewName("borrowBook");
	    }else{
	    	model.setViewName("home");
	    }
		
		return model;

	}

	@RequestMapping(value = { "/", "/login" })
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
	        @RequestParam(value = "logout", required = false) String logout, HttpServletRequest request) {

		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		model.setViewName("login");

		return model;

	}

	// customize the error message
	private String getErrorMessage(HttpServletRequest request, String key) {

		Exception exception = (Exception) request.getSession().getAttribute(key);

		String error = "";
		if (exception instanceof BadCredentialsException) {
			error = "Invalid username and password!";
		} else if (exception instanceof LockedException) {
			error = exception.getMessage();
		} else {
			error = "Invalid username and password!";
		}

		return error;
	}

	// for 403 access denied page
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied() {

		ModelAndView model = new ModelAndView();

		// check if user is login
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			System.out.println(userDetail);

			model.addObject("username", userDetail.getUsername());

		}

		model.setViewName("error");
		return model;

	}

	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public @ResponseBody String forgotPassword(HttpServletRequest request, @RequestParam("email") String email,
	        ModelMap model) {
		logger.info("Forgot password");

		UserInfo userInfo = managementUsersService.updateForgotPassword(email);

		String message = messageSource.getMessage("mgs_not_exist", null, Locale.getDefault());

		if (null != userInfo) {
			message = messageSource.getMessage("mgs_forgot_password_success", null, Locale.getDefault());

			String contextPath = request.getRequestURL().toString();
			contextPath = contextPath.replaceAll("forgotPassword", "resetPassword");
			// Send mail
			mailService.sendMailRestPass(contextPath, userInfo);
		}

		return message;
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public ModelAndView resetPassword(@RequestParam("id") int idUser, @RequestParam("token") String token,
	        ModelMap model) {
		ModelAndView mv = new ModelAndView("resetPassword", "token", token);
		mv.addObject("idUser", idUser);
		return mv;
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public String updateResetPassword(
			@RequestParam("idUser") int idUser,
			@RequestParam("currencePass") String currencePass,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("token") String token,
			ModelMap model, RedirectAttributes redirectAttributes) {

		if (managementUsersService.updatePassword(idUser, token, currencePass, newPassword)) {
			redirectAttributes.addFlashAttribute("messageChangePass",
			        messageSource.getMessage("change_password_success", null, Locale.getDefault()));
			return "redirect:/login";
		}

		redirectAttributes.addFlashAttribute("messageChangePass",
		        messageSource.getMessage("change_password_error", null, Locale.getDefault()));
		return "redirect:/resetPassword?id="+idUser+"&token="+token;
	}
}
