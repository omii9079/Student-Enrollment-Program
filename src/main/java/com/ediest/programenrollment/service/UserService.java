package com.ediest.programenrollment.service;

import com.ediest.programenrollment.dto.CreateUserDto;
import com.ediest.programenrollment.entity.Organization;
import com.ediest.programenrollment.entity.Role;
import com.ediest.programenrollment.entity.User;
import com.ediest.programenrollment.exception.BadRequestException;
import com.ediest.programenrollment.exception.ResourceNotFoundException;
import com.ediest.programenrollment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User CreateUserFororg(CreateUserDto dto, long adminUserId) {
        User admin = userRepository.findById(adminUserId).orElseThrow(() -> new ResourceNotFoundException(" Admin user not found " + adminUserId));


        if (admin.getOrganization() == null) throw new BadRequestException("Admin does not begin to an organization");

        if (dto.getRole() == Role.SUPER_ADMIN) throw new BadRequestException("Cannot create SUPER_ADMIN here");

        Optional<User> opt = userRepository.findByEmail(dto.getEmail());
        if (opt.isPresent()) throw new BadRequestException("Email already in use");

        Organization org = admin.getOrganization();

        User user = User.builder()
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .organization(org)
                .build();
        return userRepository.save(user);


    }

    public void deleteUser(long userId, long AdminUserId) {

        User Admin = userRepository.findById(AdminUserId).orElseThrow(() -> new ResourceNotFoundException(" Admind user not found " + AdminUserId));
        User Target = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not Found :" + userId));

        if (Target.getRole() == Role.SUPER_ADMIN) throw new BadRequestException("cannot delete superAdmin");

        if (Admin.getOrganization() == null || Target.getOrganization() == null || !Admin.getOrganization().getId().equals(Target.getOrganization().getId()))
            throw new BadRequestException("Admin can only delete users in their Organization ");

        userRepository.delete(Target);
    }

    // orElseThrow always need to throw an exception
    public User findbyemail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user not found " + email));
    }

    public User findbyid(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found " + id));

    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found" + id));
    }

    // Stream can be used only once that means one-time use only and Stream create just view of original list ( not copy of original list ) ( it just travelling over the our original list )  where we just add operation like filter and stream is lazy unless and util we dont tell it get that list i doesnot give us
    public List<User> listUserOrg(Long orgId) {
        return userRepository.findAll().stream().filter(u -> u.getOrganization() != null && u.getOrganization().getId().equals(orgId)).toList();
    }


}
