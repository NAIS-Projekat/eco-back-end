package com.eco.environet.finance.repository;

import com.eco.environet.finance.model.FixedExpensesEstimation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FixedExpensesEstimationRepository extends JpaRepository<FixedExpensesEstimation, Long> {
    Page<FixedExpensesEstimation> findAll(Specification<FixedExpensesEstimation> specification, Pageable pageable);;
    @Query("SELECT f FROM FixedExpensesEstimation f WHERE f.budgetPlan.id = :id")
    List<FixedExpensesEstimation> findByBudgetPlanId(@Param("id") Long id);
}
