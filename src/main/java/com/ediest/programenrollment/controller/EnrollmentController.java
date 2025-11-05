package com.ediest.programenrollment.controller;

import com.ediest.programenrollment.service.EnrollmentService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/enroll")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/{programId}")
    public ResponseEntity<?> enroll(@PathVariable Long programId) throws BadRequestException {
        enrollmentService.enrollCurrentUser(programId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("Status", "enrolled"));
    }

    @GetMapping("/my-programs")
    public ResponseEntity<?> myPrograms() throws BadRequestException {
        return ResponseEntity.status(HttpStatus.OK).body(enrollmentService.getProgramForCurrentUser());
    }

    @GetMapping("/program/{programId}/participants")
    public ResponseEntity<?> paritcipants(@PathVariable Long programId) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.OK).body(enrollmentService.getParticipantsForProgram(programId));
    }

}
