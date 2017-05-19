package com.framgia.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.framgia.users.bean.BorrowBookInfo;
import com.framgia.users.service.BorrowBookService;
import com.framgia.util.Helpers;

/**
 * ManagerBookController.java
 * 
 * @version 17/05/2017
 * @author phan.van.hieu@framgia.com
 */
@Controller
public class BorrowBookController {

	// log
	private static final Logger logger = Logger.getLogger(BorrowBookController.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	BorrowBookService borrowBookService;

	@RequestMapping(value = "/listBook/search", method = RequestMethod.POST)
	public @ResponseBody List<BorrowBookInfo> findByCondition(@RequestParam(value = "txtName") String txtName,
			@RequestParam(value = "txtCategoryName") int txtCategoryId,
			@RequestParam(value = "txtPublisherName") int txtPublisherId,
			@RequestParam(value = "txtAuthorName") int txtAuthorId, ModelMap model) {
		logger.info("call service; get list book");

		List<BorrowBookInfo> bookInfos = borrowBookService.findByBookWithCondition(txtName, txtCategoryId,
				txtPublisherId, txtAuthorId);

		return bookInfos;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ordernow/{id}", method = RequestMethod.GET)
	@ResponseBody
	public int removeCartBook(@PathVariable(value = "id") int id, HttpSession session, Model m) {
		List<BorrowBookInfo> cart;
		if (session.getAttribute("cart") == null) {
			cart = new ArrayList<BorrowBookInfo>();
			BorrowBookInfo borrowBookInfo = borrowBookService.getBookInfo(id);
			cart.add(borrowBookInfo);
			session.setAttribute("cart", cart);
			return 1;
		} else {
			cart = (List<BorrowBookInfo>) session.getAttribute("cart");
			int index = isExisting(id, session);
			if (index == -1) {
				BorrowBookInfo borrowBookInfo = borrowBookService.getBookInfo(id);
				cart.add(borrowBookInfo);
				session.setAttribute("cart", cart);
				return 1;
			} else {
				return 0;
			}

		}
	}

	@RequestMapping(value = "/cartbook")
	public ModelAndView login() {
		ModelAndView model = new ModelAndView();
		model.setViewName("cartBook");

		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/borrowBook/request")
	public ModelAndView request(@RequestParam(value = "dateBorrow") String dateBorrow,
			@RequestParam(value = "dateReturn") String dateReturn, Model m, HttpSession session, SessionStatus status) {
		ModelAndView model = new ModelAndView();
		Boolean check = true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// Check empty date Borrow
		if (Helpers.isEmpty(dateBorrow)) {
			check = false;
			m.addAttribute("err_dateborrow", messageSource.getMessage("borrow_empty", null, Locale.getDefault()));
			model.setViewName("cartBook");
		}

		// Check empty date Return
		if (Helpers.isEmpty(dateReturn)) {
			check = false;
			m.addAttribute("err_datereturn", messageSource.getMessage("return_empty", null, Locale.getDefault()));
			model.setViewName("cartBook");
		}
		try {
			Date dateBorrowed = sdf.parse(dateBorrow);
			Date dateArrive = sdf.parse(dateReturn);

			// Check date errror
			if (dateArrive.before(dateBorrowed)) {
				check = false;
				m.addAttribute("err_date", messageSource.getMessage("date_erorr", null, Locale.getDefault()));
				model.setViewName("cartBook");
			}

		} catch (ParseException e) {
			logger.info("DATE ERROR: " + e.getMessage());
		}

		if (check) {
			List<BorrowBookInfo> cart = (List<BorrowBookInfo>) session.getAttribute("cart");
			// Get name login
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String userName = auth.getName();
			int record = borrowBookService.createRequest(cart, userName, dateBorrow, dateReturn);
			if (record == 1) {

				// Remove session
				cart.removeAll(cart);
				session.setAttribute("cart", cart);
				m.addAttribute("success", messageSource.getMessage("request_success", null, Locale.getDefault()));
			} else {
				m.addAttribute("fail", messageSource.getMessage("request_fail", null, Locale.getDefault()));
			}
			model.setViewName("cartBook");
		}

		return model;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/borrowBook/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable(value = "id") int id, HttpSession session) {
		ModelAndView model = new ModelAndView();
		List<BorrowBookInfo> cart = (List<BorrowBookInfo>) session.getAttribute("cart");

		// Remove session
		int index = isExisting(id, session);
		cart.remove(index);
		session.setAttribute("cart", cart);
		model.setViewName("cartBook");

		return model;
	}

	@SuppressWarnings("unchecked")
	private int isExisting(int id, HttpSession session) {

		List<BorrowBookInfo> cart = (List<BorrowBookInfo>) session.getAttribute("cart");

		// Check exsist data in session
		for (int i = 0; i < cart.size(); i++)

			if (cart.get(i).getBookId() == id)
				return i;

		return -1;
	}
}
