package com.eco.environet.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
    private String membershipStatus;
}
