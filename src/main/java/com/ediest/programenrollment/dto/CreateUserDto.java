package com.ediest.programenrollment.dto;

import com.ediest.programenrollment.entity.Role;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {
    private String Firstname;
    private String Lastname;
    private String email;
    private String password;
    private Role role;

}
