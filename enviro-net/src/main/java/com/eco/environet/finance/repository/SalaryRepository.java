package com.eco.environet.finance.repository;

import com.eco.environet.finance.model.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    @Query("SELECT f FROM FixedExpenses f WHERE f.employee IS NOT NULL AND f.period.startDate = :startDate AND f.period.endDate = :endDate")
    List<Salary> findByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
