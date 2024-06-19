package com.eco.environet.finance.repository;

import com.eco.environet.finance.model.FixedExpenses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FixedExpensesRepository extends JpaRepository<FixedExpenses, Long> {
    Page<FixedExpenses> findAll(Specification<FixedExpenses> specification, Pageable pageable);;
    @Query("SELECT f FROM FixedExpenses f WHERE f.type != 'SALARY' AND f.period.startDate = :startDate AND f.period.endDate = :endDate")
    List<FixedExpenses> findNonSalaryByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
