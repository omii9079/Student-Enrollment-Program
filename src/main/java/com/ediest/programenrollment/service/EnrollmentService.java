package com.ediest.programenrollment.service;

import com.ediest.programenrollment.entity.Enrollment;
import com.ediest.programenrollment.entity.Program;
import com.ediest.programenrollment.entity.Role;
import com.ediest.programenrollment.entity.User;
import com.ediest.programenrollment.exception.BadRequestException;
import com.ediest.programenrollment.exception.ResourceNotFoundException;
import com.ediest.programenrollment.repository.EnrollmentRepository;
import com.ediest.programenrollment.repository.ProgramRepository;
import com.ediest.programenrollment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private ProgramRepository ProgramRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ProgramService programService;

    private User getCurrentUserEntity() throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String Email = (String) authentication.getPrincipal();

        if (Email == null) throw new BadRequestException("no Authenticated User ");

        return userRepo.findByEmail(Email).orElseThrow(() -> new ResourceNotFoundException("user not found" + Email));
    }

    public void enrollCurrentUser(Long programId) throws BadRequestException {

        User user = getCurrentUserEntity();
        Program program = ProgramRepo.findById(programId).orElseThrow(() -> new ResourceNotFoundException("program not found :" + programId));

        if (!(user.getRole() == Role.STUDENT || user.getRole() == Role.COACH))
            throw new BadRequestException("Only students or coaches can enroll");

        if (user.getOrganization() == null || !user.getOrganization().getId().equals(program.getOrganization().getId()))
            throw new BadRequestException("User and Program must belong to same Organization ");


        LocalDate today = LocalDate.now();
        if (today.isBefore(program.getStartDate()) || today.isAfter((program.getEndDate())))
            throw new BadRequestException("program is not active");

        if (enrollmentRepository.existsByUserIdAndProgramId(user.getId(), programId)) {
            throw new BadRequestException("User already enrolled in program");
        }

        Enrollment e = Enrollment.builder().user(user).program(program).build();
        enrollmentRepository.save(e);
    }

    public List<Program> getProgramForCurrentUser() throws BadRequestException {
        User user = getCurrentUserEntity();
        List<Enrollment> enrollments = enrollmentRepository.findByuserId(user.getId());
        return enrollments.stream().map(enrollment -> enrollment.getProgram()).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getParticipantsForProgram(Long programId) throws BadRequestException {
        User user = getCurrentUserEntity();
        Program program = ProgramRepo.findById(programId).orElseThrow(() -> new ResourceNotFoundException("Program not found :" + programId));

        if (!(user.getRole() == Role.ADMIN || user.getRole() == Role.COACH))
            throw new BadRequestException("Only admin or Coach can view participants");

        if (user.getOrganization() == null || !user.getOrganization().getId().equals(program.getOrganization().getId()))
            throw new BadRequestException("Not allowed for this program");


        //map() used to transform data one format to another format and Map.of funtion is used that please convert into key value format like hashmap collection type
        // this would create like this below
        //  {"userId": 1, "firstName": "Omkar", "lastName": "Lande", "role": "STUDENT"},
        //  {"userId": 2, "firstName": "Ravi", "lastName": "Kumar", "role": "COACH"},
        //  {"userId": 3, "firstName": "Neha", "lastName": "Patil", "role": "STUDENT"}
        List<Enrollment> list = enrollmentRepository.findByprogramId(programId);

        return list.stream()
                .map(en -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", en.getUser().getId());
                    map.put("FirstName", en.getUser().getFirstname());
                    map.put("Lastname", en.getUser().getLastname());
                    map.put("Role", en.getUser().getRole());
                    return map;
                }).collect(Collectors.toList());

    }


}


