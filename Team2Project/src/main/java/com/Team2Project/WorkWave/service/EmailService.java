package com.Team2Project.WorkWave.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVeriEmail(String to, String verificationCode) throws MessagingException {
        String subject = "WORKWAVE 회원가입 인증코드";
        String htmlContent = "<p>인증번호: " + verificationCode + "</p>";

        sendEmail(to, subject, htmlContent);
    }
    
    public void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true indicates that this is HTML content
        helper.setFrom("ftvkal98@naver.com");
        
        mailSender.send(message);
    }
}
