package com.eco.environet.finance.services.impl;

import com.eco.environet.finance.dto.BudgetPlanDto;
import com.eco.environet.finance.model.BudgetPlan;
import com.eco.environet.finance.model.BudgetPlanStatus;
import com.eco.environet.finance.model.DateRange;
import com.eco.environet.finance.repository.BudgetPlanRepository;
import com.eco.environet.finance.repository.BudgetPlanSpecifications;
import com.eco.environet.finance.services.BudgetPlanService;
import com.eco.environet.users.model.OrganizationMember;
import com.eco.environet.util.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetPlanServiceImpl implements BudgetPlanService {
    @Value("${baseFrontUrl}")
    private String baseFrontUrl;
    private final ObjectMapper objectMapper;
    private final BudgetPlanRepository repository;

    @Override
    public BudgetPlanDto create(BudgetPlanDto newBudgetPlan){
        if (!newBudgetPlan.getFiscalDateRange().isValid() || !newBudgetPlan.getFiscalDateRange().isInFuture()) {
            throw new IllegalArgumentException("Invalid fiscal date range");
        }
        OrganizationMember author = new OrganizationMember();
        author.setId(newBudgetPlan.getAuthor().getId());

        BudgetPlan budget = BudgetPlan.builder()
                .name(newBudgetPlan.getName())
                .description(newBudgetPlan.getDescription())
                .status(BudgetPlanStatus.DRAFT)
                .lastUpdatedOnDate(newBudgetPlan.getLastUpdatedOnDate())
                .fiscalDateRange(newBudgetPlan.getFiscalDateRange())
                .author(author)
                .build();

        repository.save(budget);
        return Mapper.map(budget, BudgetPlanDto.class);
    }
    @Override
    public Page<BudgetPlanDto> findAll(Long currentUserId, String name, String period, List<String> statuses, List<Long> authors, Pageable pageable) {
        Specification<BudgetPlan> spec = getSpecification(currentUserId, name, period, statuses, authors);
        Page<BudgetPlan> allPlans = repository.findAll(spec, pageable);
        Page<BudgetPlanDto> allPlansDto = Mapper.mapPage(allPlans, BudgetPlanDto.class);
        return allPlansDto;
    }
    private Specification<BudgetPlan> getSpecification(Long currentUserId, String name, String period, List<String> statuses, List<Long> authors) {
        DateRange dateRange = getDateRange(period);
        List<BudgetPlanStatus> statusList = getStatusListFromStringList(statuses);
        return Specification.where(
                StringUtils.isBlank(name) ? null : BudgetPlanSpecifications.nameLike(name))
                .and(BudgetPlanSpecifications.statusIn(statusList, currentUserId))
                .and(BudgetPlanSpecifications.afterStartDate(dateRange))
                .and(BudgetPlanSpecifications.beforeEndDate(dateRange))
                .and(BudgetPlanSpecifications.authorIn(authors));
    }
    private DateRange getDateRange(String period) {
        DateRange dateRange = new DateRange();
        if (dateRange.getEndDate() == null){
            dateRange.setStartDate(null);
        }
        if (period != null && !period.isEmpty() && !period.equals("undefined")) {
            try {
                dateRange = objectMapper.readValue(period, DateRange.class);
            } catch (IOException e) {
                // Handle the exception or throw a custom exception
                throw new IllegalArgumentException("Invalid period format", e);
            }
        }
        if (!dateRange.isValid()) {
            throw new IllegalArgumentException("Invalid period " + period);
        }
        return dateRange;
    }
    private List<BudgetPlanStatus> getStatusListFromStringList(List<String> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return BudgetPlanStatus.getAllValidStatus();
        }
        List<BudgetPlanStatus> result = new ArrayList<>();
        for (String status : statuses){
            BudgetPlanStatus filtered = BudgetPlanStatus.valueOf(status);
            result.add(filtered);
        }
        return result;
    }

    @Override
    public BudgetPlanDto findById(Long id){
        BudgetPlan budget = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Budget plan not found with ID: " + id));
        return Mapper.map(budget, BudgetPlanDto.class);
    }

    @Override
    public BudgetPlanDto update(BudgetPlanDto budgetPlanDto){
        BudgetPlan budget = repository.findById(budgetPlanDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Budget plan not found with ID: " + budgetPlanDto.getId()));
        // check if status is valid
        checkStatus(budget, budgetPlanDto);

        budget.setLastUpdatedOnDate(new Timestamp(System.currentTimeMillis()));
        budget.setName(budgetPlanDto.getName());
        BudgetPlanStatus status = BudgetPlanStatus.valueOf(budgetPlanDto.getStatus());
        budget.setStatus(status);
        budget.setDescription(budgetPlanDto.getDescription());
        if (!budgetPlanDto.getFiscalDateRange().isValid()){
            throw new IllegalArgumentException("Invalid fiscal date range");
        }
        budget.setFiscalDateRange(budgetPlanDto.getFiscalDateRange());
        repository.save(budget);
        return Mapper.map(budget, BudgetPlanDto.class);
    }

    private void checkStatus(BudgetPlan budget, BudgetPlanDto budgetPlanDto){
        // If status isn't changed, return
        if (budget.getStatus().toString().equals(budgetPlanDto.getStatus())){
            return;
        }
        // Check if status of old budget plan status is valid
        if (budget.getStatus() != BudgetPlanStatus.DRAFT && budget.getStatus() != BudgetPlanStatus.PENDING) {
            throw new IllegalArgumentException("Can't update budget plan that isn't DRAFT or PENDING.");
        }
        // Check if status of new budget plan status is valid
        if (!budgetPlanDto.getStatus().equals("PENDING") && !budgetPlanDto.getStatus().equals("APPROVED") && !budgetPlanDto.getStatus().equals("REJECTED")) {
            throw new IllegalArgumentException("Invalid new budget plan status. New status must be PENDING, APPROVED or REJECTED.");
        }
        // DRAFT -> PENDING
        if (budget.getStatus() == BudgetPlanStatus.DRAFT && budgetPlanDto.getStatus().equals("PENDING")){
            return;
        }
        // PENDING -> APPROVED || PENDING -> REJECTED
        if (budget.getStatus() == BudgetPlanStatus.PENDING && (budgetPlanDto.getStatus().equals("APPROVED") || budgetPlanDto.getStatus().equals("REJECTED"))) {
            return;
        } else {
            throw new IllegalArgumentException("Invalid budget plan status update.");
        }
    }

    @Override
    public void archive(BudgetPlanDto budgetPlan) {
        BudgetPlan budget = repository.findById(budgetPlan.getId())
                .orElseThrow(() -> new EntityNotFoundException("Budget plan not found with ID: " + budgetPlan.getId()));
        if ((budget.getStatus() != BudgetPlanStatus.APPROVED) && (budget.getStatus() != BudgetPlanStatus.REJECTED)) {
            throw new IllegalArgumentException("Can't archive budget plan that isn't APPROVED or REJECTED.");
        }
        budget.archive();
        repository.save(budget);
    }

    @Override
    public void close(BudgetPlanDto budgetPlan) {
        BudgetPlan budget = repository.findById(budgetPlan.getId())
                .orElseThrow(() -> new EntityNotFoundException("Budget plan not found with ID: " + budgetPlan.getId()));
        if (budget.getStatus() != BudgetPlanStatus.DRAFT){
            throw new IllegalArgumentException("Can't close budget plan that isn't DRAFT.");
        }
        budget.close();
        repository.save(budget);
    }
}
