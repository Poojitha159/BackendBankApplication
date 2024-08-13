package com.techlabs.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {


		  @Autowired
		  private JavaMailSender javaMailSender;

		  @Value("${spring.mail.username}")
		  private String sender;

		  @Override
		  public void sendMail(String recipientEmail, String subject, String text) {

		    SimpleMailMessage mailMessage = new SimpleMailMessage();

		    mailMessage.setFrom(sender);
		    mailMessage.setTo(recipientEmail);
		    mailMessage.setText(text);
		    mailMessage.setSubject(subject);
		    javaMailSender.send(mailMessage);
		  }

		

		
	}


