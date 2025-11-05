package com.ediest.programenrollment.service;

import com.ediest.programenrollment.entity.Organization;
import com.ediest.programenrollment.exception.ResourceNotFoundException;
import com.ediest.programenrollment.repository.OragnizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    private OragnizationRepository orgRepo;

    public Organization createOrganization(String name) {
        Organization org = Organization.builder().name(name).build();
        return orgRepo.save(org);
    }


    public Organization UpdateOrganization(long id, String name) {
        Organization org = orgRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Organization not found " + id));

        org.setName(name);
        return orgRepo.save(org);
    }

    public void deleteOrganization(long id) {
        if (!orgRepo.existsById(id)) throw new ResourceNotFoundException("Organization not found :" + id);

        orgRepo.deleteById(id);
    }

    public List<Organization> listAll() {
        return orgRepo.findAll();
    }

}
