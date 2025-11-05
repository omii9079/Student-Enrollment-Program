package com.ediest.programenrollment.repository;

import com.ediest.programenrollment.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    List<Program> findByOrganizationId(Long orgId);
}
