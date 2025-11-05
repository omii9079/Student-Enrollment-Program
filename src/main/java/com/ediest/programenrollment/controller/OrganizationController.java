package com.ediest.programenrollment.controller;

import com.ediest.programenrollment.dto.CreateOrganizationDto;
import com.ediest.programenrollment.entity.Organization;
import com.ediest.programenrollment.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/org")
public class OrganizationController {
    @Autowired
    private OrganizationService orgservice;

    @PostMapping()
    public Organization create(@RequestBody CreateOrganizationDto dto) {
        return orgservice.createOrganization(dto.getName());
    }

    @PutMapping("/{id}")
    public Organization update(@PathVariable long id, @RequestBody CreateOrganizationDto dto) {
        return orgservice.UpdateOrganization(id, dto.getName());

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        orgservice.deleteOrganization(id);
    }

    @GetMapping
    public List<Organization> list() {
        return orgservice.listAll();
    }


}
