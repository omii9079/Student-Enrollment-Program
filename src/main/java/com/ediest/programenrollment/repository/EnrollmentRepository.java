package com.ediest.programenrollment.repository;

import com.ediest.programenrollment.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByuserId(long userId);

    List<Enrollment> findByprogramId(long ProgramId);

    boolean existsByUserIdAndProgramId(Long userId, Long programId);
}
