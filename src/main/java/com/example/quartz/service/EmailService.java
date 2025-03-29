package com.example.quartz.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

@Service
public class EmailService {
	
    @Autowired
    private JavaMailSender emailSender;
    
    @Value("${mail.sender}")
    private String senderEmail;

    public void sendEmail(String to, String subject, String body) throws IOException, MessagingException {

        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom(senderEmail);
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(body);
        emailSender.send(message);
    	
    }
}

