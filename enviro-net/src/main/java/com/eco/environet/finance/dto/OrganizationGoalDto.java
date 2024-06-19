package com.eco.environet.finance.dto;

import com.eco.environet.finance.model.DateRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationGoalDto {
    private Long id;
    private String title;
    private String description;
    private String rationale;
    private int priority;
    private String status;
    private DateRange validPeriod;
    private AccountantDto creator;
}
