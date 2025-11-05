package com.ediest.programenrollment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class CreateProgramDto {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long organizationId;

}
