package com.ediest.programenrollment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "program_id"}))
// @Table is used to give Extra info about how your entity should look in database Table
// here uniqueConstraints these column have unique combination mean userid and program shouldnot have same duplicate combination like in user 101 and program 202  again same in another row is not allowed
public class Enrollment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Program program;

}
