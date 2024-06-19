package com.eco.environet.finance.services;

import com.eco.environet.finance.dto.BudgetPlanDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BudgetPlanService {
    BudgetPlanDto create(BudgetPlanDto newBudgetPlan);
    Page<BudgetPlanDto> findAll(Long currentUserId, String name, String period, List<String> statuses, List<Long> authors, Pageable pageable);
    BudgetPlanDto findById(Long id);
    BudgetPlanDto update(BudgetPlanDto budgetPlan);
    void archive(BudgetPlanDto budgetPlan);
    void close(BudgetPlanDto budgetPlan);
}
