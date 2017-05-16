package com.framgia.users.service;

public interface MailService {

	public void sendMailRestPass(final String contextPath, final Object object);

	public void sendEmailBorrowed(final Object object);
}
