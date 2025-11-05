package com.ediest.programenrollment.controller;

import com.ediest.programenrollment.dto.CreateProgramDto;
import com.ediest.programenrollment.entity.Program;
import com.ediest.programenrollment.entity.User;
import com.ediest.programenrollment.exception.BadRequestException;
import com.ediest.programenrollment.service.ProgramService;
import com.ediest.programenrollment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/program")
public class ProgramController {

    @Autowired
    private ProgramService programService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateProgramDto dto) throws BadRequestException {
        // Authenticatio object is used to get current logged  so currently loggend in user stored with in the securityContext holder in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();// get principal means who the user is (username/email or userDetails )
        User admin = userService.findbyemail(email);
        if (admin.getOrganization() == null || !admin.getOrganization().getId().equals(dto.getOrganizationId()))
            throw new BadRequestException("not allowed to update this program");

        return ResponseEntity.status(HttpStatus.OK).body(programService.createprogram(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CreateProgramDto dto) throws BadRequestException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();

        User admin = userService.findbyemail(email);

        Program existing = programService.FindById(id);
        if (admin.getOrganization() == null || !admin.getOrganization().getId().equals(existing.getOrganization().getId()))
            throw new BadRequestException("Not allowed to update this Program");

        if (dto.getOrganizationId() != null && !dto.getOrganizationId().equals(existing.getOrganization().getId()))
            throw new BadRequestException("cannot change program");

        return ResponseEntity.status(HttpStatus.OK).body(programService.updateprogram(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User admin = userService.findbyemail(email);
        Program existing = programService.FindById(id);

        if (admin.getOrganization() == null || !admin.getOrganization().getId().equals(existing.getOrganization().getId()))
            throw new BadRequestException("Not allowed to Delete this Program");
        programService.deleteProgram(id);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping("/org/{orgId}")
    public ResponseEntity<?> listbyorg(@PathVariable Long orgId) {
        return ResponseEntity.status(HttpStatus.OK).body(programService.listProgramByorg(orgId));
    }


}
