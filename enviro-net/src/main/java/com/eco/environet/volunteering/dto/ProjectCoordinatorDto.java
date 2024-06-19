package com.eco.environet.volunteering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCoordinatorDto {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
}
