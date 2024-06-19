package com.eco.environet.finance.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import com.eco.environet.finance.model.DateRange;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetPlanDto {
    private Long id;
    private String name;
    private String description;
    private String status;
    private Timestamp lastUpdatedOnDate;
    private DateRange fiscalDateRange;
    private EmployeeDto author;
}
