package com.eco.environet.finance.dto;

import com.eco.environet.finance.model.DateRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixedExpensesDto {
    @NotNull(message = "Id is required")
    private Long id;

    @NotBlank(message = "Type is required")
    private String type;

    @NotNull(message = "Period is required")
    private DateRange period;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be a positive number")
    private double amount;

    @NotNull(message = "Creator is required")
    private EmployeeDto creator;

    @NotNull(message = "Creation date is required")
    private Timestamp createdOn;

    private String description;
    private EmployeeDto employee;
    private double overtimeHours;
}
