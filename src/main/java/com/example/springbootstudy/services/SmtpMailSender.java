package com.example.springbootstudy.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SmtpMailSender {

    @Value("${spring.mail.username}")
    private String username;

    private final JavaMailSender mailSender;


    public SmtpMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String mailTo,String subject,String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(mailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
