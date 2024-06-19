package com.eco.environet.finance.repository;

import com.eco.environet.finance.model.OrganizationGoal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationGoalRepository extends JpaRepository<OrganizationGoal, Long> {
    Page<OrganizationGoal> findAll(Specification<OrganizationGoal> spec, Pageable pageable);;
    Page<OrganizationGoal> findByValidPeriodEndDateIsNull(Pageable pageable);
}
