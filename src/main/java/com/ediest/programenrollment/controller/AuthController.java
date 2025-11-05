package com.ediest.programenrollment.controller;

import com.ediest.programenrollment.config.JwtUtil;
import com.ediest.programenrollment.dto.AuthRequest;
import com.ediest.programenrollment.dto.AuthResponse;
import com.ediest.programenrollment.entity.Role;
import com.ediest.programenrollment.entity.User;
import com.ediest.programenrollment.repository.OragnizationRepository;
import com.ediest.programenrollment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private OragnizationRepository orgnizationRepository;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {

        var user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Credentials"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid Credentails");

        Map<String, Object> Claims = new HashMap<>();
        Claims.put("role", user.getRole().name());// getrole given enum value which role and .name() is built method enum it converts into string
        Claims.put("userId", user.getId());
        String token = jwtUtil.generateToken(user.getEmail(), Claims);
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(token));
    }

    @PutMapping("/create-super-admin")
    public String createsuper(@RequestBody AuthRequest req) {
        Optional<User> user = userRepo.findByEmail(req.getEmail());
        if (user.isPresent()) return "already exists";

        var user1 = User.builder().firstname("Super").lastname("Admin").email(req.getEmail()).password(passwordEncoder.encode(req.getPassword())).role(Role.SUPER_ADMIN).build();
        userRepo.save(user1);
        return "created";
    }


    @PostMapping("/create-admin")
    public Map<String, String> createAdming(@RequestBody Map<String, String> req) {
        var orgId = Long.parseLong(req.get("orgId"));
        var org = orgnizationRepository.findById(orgId).orElseThrow(() -> new RuntimeException("Org not found "));

        if (userRepo.findByEmail(req.get("email")).isPresent()) return Map.of("status", "admin-exists");

        var admin = User.builder().firstname(req.get("firstname"))
                .lastname(req.get("lastname"))
                .email(req.get("email"))
                .role(Role.ADMIN)
                .password(passwordEncoder.encode(req.get("password")))
                .organization(org)
                .build();
        userRepo.save(admin);
        return Map.of("Status", "Admin_CREATED");
    }

}
