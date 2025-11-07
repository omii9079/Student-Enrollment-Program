package com.ediest.programenrollment;

import com.ediest.programenrollment.service.EmailServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
// we need to add while test because normally if we dont start the application application add classes would not be like applicationcontext and beans wont create so that's we need to add this
// by adding this annoation we telling springinternally start everything read the application.yml and beans , server everything no need to start the application manually
public class EmailServiceTests {

    @Autowired
    private EmailServiceTest emailServiceTest;

    @Test
    void testSendMail() {
        emailServiceTest.sendEmail("jayrajgajul10@gmail.com ", "Testing Java Mail Sender", "kas chally mg junior pahije ka tula  ?");
    }
}
