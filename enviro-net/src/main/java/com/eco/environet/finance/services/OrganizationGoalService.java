package com.eco.environet.finance.services;

import com.eco.environet.finance.dto.OrganizationGoalDto;
import com.eco.environet.finance.dto.OrganizationGoalsSetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrganizationGoalService {
    OrganizationGoalDto create(OrganizationGoalDto newGoal);
    Page<OrganizationGoalsSetDto> findAll(String title, String period, List<String> statuses, List<Long> authors, Pageable pageable);
    OrganizationGoalsSetDto findCurrent();
    OrganizationGoalDto findById(Long id);
    OrganizationGoalDto update(OrganizationGoalDto oldGoal);
    void delete(Long id);
    OrganizationGoalsSetDto publish(OrganizationGoalsSetDto newValid);
}
