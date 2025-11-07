package com.ediest.programenrollment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceTest {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String Subject, String Body) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(Subject);
            mail.setText(Body);
            javaMailSender.send(mail);
        } catch (Exception e) {
            log.error("Exception while SendEmail", e);
        }
    }
}
