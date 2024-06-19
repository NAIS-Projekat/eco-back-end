package com.eco.environet.finance.services;

import com.eco.environet.finance.dto.FixedExpensesDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FixedExpensesService {
    Page<FixedExpensesDto> lastMonthSalaryExpenses(Long creatorId, Pageable pageable);
    FixedExpensesDto create(FixedExpensesDto newFixedExpenseDto);
    Page<FixedExpensesDto> findAll(String period, List<String> types, List<Long> employees, List<Long> creators, Pageable pageable);
    FixedExpensesDto findById(Long id);
    FixedExpensesDto updateSalaryExpense(FixedExpensesDto salaryExpenseDto);
    FixedExpensesDto update(FixedExpensesDto fixedExpenseDto);
    void delete(Long id);
}
