package com.eco.environet.finance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixedExpensesEstimationDto {
    @NotNull(message = "Id is required")
    private Long id;

    @NotNull(message = "Budget Plan is required")
    private BudgetPlanDto budgetPlan;

    @NotNull(message = "Fixed Expense is required")
    private FixedExpensesDto fixedExpense;
}
