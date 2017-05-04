package com.framgia.users.service;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.framgia.users.bean.BorrowedInfo;
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

	@Override
	public void sendEmail(Object object) {

		BorrowedInfo borrowedInfo = (BorrowedInfo) object;

		MimeMessagePreparator preparator = getMessagePreparator(borrowedInfo);

		try {
			mailSender.send(preparator);
			logger.info("Message Sending...");
			
		} catch (MailException ex) {
			ex.printStackTrace();
			logger.error("Error send mail: " + ex.getMessage());
		}
	}

	public String bodyMail(BorrowedInfo borrowedInfo) {
		String body = "";

		if (ConvertDataModelAndBean.borr_status_approve.equals(borrowedInfo.getStatus())) {
			body = "Dear " + borrowedInfo.getUserInfo().getName() + ","
					+ "<p>We want to send this mail to confirm with you, that your borrowed book: "
					+ borrowedInfo.getBorrowedCode() + " have been accepted.</p><p>"
					+ "You access this borrowed book to know detail information."
					+ "Please earliest go to library to receive book.</p>";

		} else if (ConvertDataModelAndBean.borr_status_cancel.equals(borrowedInfo.getStatus())){
			body = "Dear " + borrowedInfo.getUserInfo().getName() + ","
					+ "<p>We sorry must notification with you that borrowed book: " + borrowedInfo.getBorrowedCode()
					+ " have been canceled.</p><p>Please create request borrowed book other or borrowed this book go to back later.</p>";
		}
		return body;
	}

	private MimeMessagePreparator getMessagePreparator(final BorrowedInfo borrowedInfo) {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

				helper.setSubject("Your Borrowed book: have been change status: " + borrowedInfo.getStatus());
				helper.setTo(borrowedInfo.getUserInfo().getEmail());
				helper.setText("<html><body><p>" + bodyMail(borrowedInfo) + "</p></body></html>", true);
			}
		};
		return preparator;
	}
}
