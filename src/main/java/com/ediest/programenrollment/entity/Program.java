package com.ediest.programenrollment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate StartDate;

    @Column(nullable = false)
    private LocalDate endDate;


    //    optional means every program must have an Organization  if you try to save a program without assigning an Oraganization ,you will get an get an error
    //    here MantoOne Relationship because there would be multiple program but belongs to One Organization so that we used foreignkey of Organization
    // optional = false mean not any field should be empty
    @ManyToOne(optional = false)
    private Organization organization;


}
