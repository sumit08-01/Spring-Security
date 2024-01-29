package com.otp.email.service;

import jakarta.mail.MessagingException;

public interface EmailService {

	public void sendMail(String to, String subject, String body) throws MessagingException;
}
