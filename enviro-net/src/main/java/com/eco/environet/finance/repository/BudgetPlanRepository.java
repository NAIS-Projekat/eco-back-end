package com.eco.environet.finance.repository;

import com.eco.environet.finance.model.BudgetPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, Long>{
    Page<BudgetPlan> findAll(Specification<BudgetPlan> specification, Pageable pageable);
}
