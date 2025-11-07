package com.ediest.programenrollment.controller;

import com.ediest.programenrollment.service.EmailServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send")
public class EmailSenderController {

    @Autowired
    private EmailServiceTest emailService;

    @PostMapping("/mail")
    public ResponseEntity<?> testSendMail() {
        emailService.sendEmail("rhushinandodkar03@gmail.com ", "kay kartoy lavdya ", "sagar chya aaicha dana  ");
        return ResponseEntity.status(HttpStatus.OK).body("your mail has been send ");
    }


}
