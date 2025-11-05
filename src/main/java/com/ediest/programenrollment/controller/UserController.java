package com.ediest.programenrollment.controller;

import com.ediest.programenrollment.dto.CreateUserDto;
import com.ediest.programenrollment.entity.Role;
import com.ediest.programenrollment.entity.User;
import com.ediest.programenrollment.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    //@GetMapping


    @PostMapping
    public ResponseEntity<?> CreateUser(@RequestBody CreateUserDto dto) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String Email = (String) authentication.getPrincipal();
        User Admin = userService.findbyemail(Email);

        if (Admin.getRole() != Role.ADMIN) throw new BadRequestException("only Admin  can Create users ");

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.CreateUserFororg(dto, Admin.getId()));
        // ResponseEntity is a special Spring class that represents a complete http response basically this class give configuration we can configure the response which data to send which as response tells everything
        // we can also specify that ResponseEntity.ok() means we are passing the ok status directly as per this we can do for anther method as well
        //Body → the actual data you want to send (e.g., a created user)
        //HTTP Status Code → tells the client what happened (OK, CREATED, BAD_REQUEST, etc.)
        //Headers → optional info (like location or custom info)
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String Email = (String) authentication.getPrincipal();
        User Admin = userService.findbyemail(Email);
        if (Admin.getRole() != Role.ADMIN) throw new BadRequestException("ONly admin can delete users ");
        userService.deleteUser(id, Admin.getId());
        return ResponseEntity.ok().build();
    }
}
