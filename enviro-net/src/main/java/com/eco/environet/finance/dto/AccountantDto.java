package com.eco.environet.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountantDto {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
}