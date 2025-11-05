package com.ediest.programenrollment.service;

import com.ediest.programenrollment.dto.CreateProgramDto;
import com.ediest.programenrollment.entity.Organization;
import com.ediest.programenrollment.entity.Program;
import com.ediest.programenrollment.exception.BadRequestException;
import com.ediest.programenrollment.exception.ResourceNotFoundException;
import com.ediest.programenrollment.repository.OragnizationRepository;
import com.ediest.programenrollment.repository.ProgramRepository;
import com.ediest.programenrollment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProgramService {
    @Autowired
    private ProgramRepository ProgramRepo;

    @Autowired
    private OragnizationRepository orgRepo;

    @Autowired
    private UserRepository userRepo;

    public Program createprogram(CreateProgramDto dto) throws BadRequestException {
        var org = orgRepo.findById(dto.getOrganizationId()).orElseThrow(() -> new ResourceNotFoundException(" Organization not found :" + dto.getOrganizationId()));

        if (dto.getStartDate() == null || dto.getEndDate() == null)
            throw new BadRequestException("Start and End Date are Required ");

        if (dto.getEndDate().isBefore(dto.getStartDate()))
            throw new BadRequestException("End date cannot be before Start Date");
        // here org is get because organization got by the orgnization id in which org has got that is assigned to it

        Program p = Program.builder()
                .name(dto.getName())
                .StartDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .organization(org)
                .build();
        return ProgramRepo.save(p);
    }


    public Program updateprogram(Long ProgramId, CreateProgramDto dto) {

        Program existing = ProgramRepo.findById(ProgramId).orElseThrow(() -> new ResourceNotFoundException("Program not found " + ProgramId));

        if (dto.getName() != null) existing.setName(dto.getName());

        if (dto.getStartDate() != null) existing.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) existing.setEndDate(dto.getEndDate());

        if (dto.getOrganizationId() != null) {
            // findById Returns object of Optional type like Optional<Organization> opt org; so if not present then return optional class object to orelsethrow to throw excecption;
            Organization org = orgRepo.findById(dto.getOrganizationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Organization not found :" + dto.getOrganizationId()));
            existing.setOrganization(org);
        }
        return ProgramRepo.save(existing);


    }

    public List<Program> listProgramByorg(Long orgId) {
        return ProgramRepo.findByOrganizationId(orgId);
    }

    public void deleteProgram(Long id) {

        if (!ProgramRepo.existsById(id)) throw new ResourceNotFoundException("program not found :" + id);

        ProgramRepo.deleteById(id);
    }

    public Program FindById(Long id) {

        return ProgramRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Program not found " + id));
    }

    public boolean isProgramActive(Program p) {

        LocalDate Today = LocalDate.now();
        return !Today.isBefore(p.getStartDate()) && !Today.isAfter(p.getEndDate());
    }

}



