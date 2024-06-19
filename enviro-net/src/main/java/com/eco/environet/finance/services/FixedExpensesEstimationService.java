package com.eco.environet.finance.services;

import com.eco.environet.finance.dto.FixedExpensesEstimationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FixedExpensesEstimationService {
    List<FixedExpensesEstimationDto> generateEstimationForBudgetPlan(Long id);
    FixedExpensesEstimationDto create(FixedExpensesEstimationDto fixedExpensesEstimationDto);
    Page<FixedExpensesEstimationDto> findAll(Long budgetPlanId, List<String> types, List<Long> employees, Pageable pageable);
    FixedExpensesEstimationDto findById(Long id);
    FixedExpensesEstimationDto update(FixedExpensesEstimationDto fixedExpensesEstimationDto);
    void delete(Long id);
}
