package com.eco.environet.volunteering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredUserDto {
    private Long id;
    private String email;
    private String username;
    private String name;
    private String surname;
    private String phoneNumber;
    private int points;
}
