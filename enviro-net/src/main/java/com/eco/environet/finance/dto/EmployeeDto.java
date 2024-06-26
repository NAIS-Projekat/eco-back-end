package com.eco.environet.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private double wage;
    private double workingHours;
    private double overtimeWage;
}
