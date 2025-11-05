package com.ediest.programenrollment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    //    @Enumerated(Enumtype.STRING) tell whenever you are storing Role (Enum) store it using name of it in string
//    @Enumerated(Enumtype.ORDINAL) tells Whenever you store this Enum in the Database stores it as integer mean position number of that role
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ManyToOne
    private Organization organization;
}
