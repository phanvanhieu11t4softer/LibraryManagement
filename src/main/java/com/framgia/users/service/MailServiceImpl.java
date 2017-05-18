package com.framgia.users.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.framgia.users.bean.BorrowedInfo;
import com.framgia.users.bean.UserInfo;
import com.framgia.util.ConvertDataModelAndBean;

/**
 * ManagementUsersController.java
 * 
 * @version 06/05/2017
 * @author vu.thi.tran.van@framgia.com
 */
@Service("mailService")
public class MailServiceImpl implements MailService {

	// log
	private static final Logger logger = Logger.getLogger(MailServiceImpl.class);

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	VelocityEngine velocityEngine;

	@Autowired
	MessageSource messageSource;

	@Override
	public void sendEmailBorrowed(Object object) {

		BorrowedInfo borrowedInfo = (BorrowedInfo) object;

		MimeMessagePreparator preparator = getMessagePreparatorChangeBorrowed(borrowedInfo);

		try {
			mailSender.send(preparator);
			logger.info("Message sending...");

		} catch (MailException ex) {
			ex.printStackTrace();
			logger.error("Error send mail: ", ex);
		}
	}

	private MimeMessagePreparator getMessagePreparatorChangeBorrowed(final BorrowedInfo borrowedInfo) {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

				helper.setSubject(messageSource.getMessage("subject_change_borrowed", null, Locale.getDefault())
				        + borrowedInfo.getStatus());

				helper.setTo(borrowedInfo.getUserInfo().getEmail());

				Map<String, Object> model = new HashMap<String, Object>();
				model.put("borrowedInfo", borrowedInfo);
				String text = "";

				if (borrowedInfo.getStatus().equals(ConvertDataModelAndBean.borr_status_cancel)) {
					text = getContentBorrowedCanCel(model);
				} else if (borrowedInfo.getStatus().equals(ConvertDataModelAndBean.borr_status_finish)) {
					text = getContentBorrowedFinish(model);
				} else {
					text = getContentBorrowedApprove(model);
				}

				logger.info("Body mail: " + text);
				helper.setText(text, true);
			}
		};

		return preparator;
	}

	@SuppressWarnings("deprecation")
	public String getContentBorrowedApprove(Map<String, Object> model) {
		StringBuffer content = new StringBuffer();
		try {
			content.append(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
			        "/templateMail/MailBorrowed_Approve.vm", model));
			return content.toString();
		} catch (Exception e) {
			logger.error("Error processing velocity template: ", e);
		}
		return "";
	}

	@SuppressWarnings("deprecation")
	public String getContentBorrowedCanCel(Map<String, Object> model) {
		StringBuffer content = new StringBuffer();
		try {
			content.append(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
			        "/templateMail/MailBorrowed_Cancel.vm", model));
			return content.toString();
		} catch (Exception e) {
			logger.error("Error processing velocity template: ", e);
		}
		return "";
	}

	@SuppressWarnings("deprecation")
	public String getContentBorrowedFinish(Map<String, Object> model) {
		StringBuffer content = new StringBuffer();
		try {
			content.append(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
			        "/templateMail/MailBorrowed_Finish.vm", model));
			return content.toString();
		} catch (Exception e) {
			logger.error("Error processing velocity template: ", e);
		}
		return "";
	}

	// send mail of reset password
	@Override
	public void sendMailRestPass(String contextPath, Object object) {

		UserInfo user = (UserInfo) object;

		MimeMessagePreparator preparator = getMessagePreparatorResetPass(contextPath, user);

		try {
			mailSender.send(preparator);
			logger.info("Message Sending...");

		} catch (MailException ex) {
			ex.printStackTrace();
			logger.error("Error send mail: ", ex);
		}
	}

	private MimeMessagePreparator getMessagePreparatorResetPass(final String contextPath, final UserInfo user) {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

				String url = contextPath + "?id=" + user.getUserId() + "&token=" + user.getTokenResetPassword();

				helper.setSubject(messageSource.getMessage("subject_reset_pass", null, Locale.getDefault()));

				helper.setTo(user.getEmail());

				Map<String, Object> model = new HashMap<String, Object>();
				model.put("name", user.getName());
				model.put("url", url);
				String text = getContentMailResetPass(model);

				logger.info("Body mail: " + text);
				helper.setText(text, true);
			}
		};

		return preparator;
	}

	@SuppressWarnings("deprecation")
	public String getContentMailResetPass(Map<String, Object> model) {
		StringBuffer content = new StringBuffer();
		try {
			content.append(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
			        "/templateMail/MailResetPassword.vm", model));
			return content.toString();
		} catch (Exception e) {
			logger.error("Error processing velocity template: ", e);
		}
		return "";
	}
}
