package com.eco.environet.users.dto;

import com.eco.environet.users.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private Long id;
    private String role;
    private String email;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private Gender gender;
    private Timestamp dateOfBirth;
}
