package com.eco.environet.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserContactDto {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
}
