package com.eco.environet.finance.services.impl;

import com.eco.environet.finance.dto.AccountantDto;
import com.eco.environet.finance.model.OrganizationGoalStatus;
import com.eco.environet.finance.repository.OrganizationGoalSpecifications;
import com.eco.environet.finance.services.OrganizationGoalService;
import com.eco.environet.users.model.User;
import com.eco.environet.finance.dto.OrganizationGoalDto;
import com.eco.environet.finance.dto.OrganizationGoalsSetDto;
import com.eco.environet.finance.model.DateRange;
import com.eco.environet.finance.model.OrganizationGoal;
import com.eco.environet.finance.repository.OrganizationGoalRepository;
import com.eco.environet.util.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationGoalServiceImpl implements OrganizationGoalService {
    @Value("${baseFrontUrl}")
    private String baseFrontUrl;
    private final ObjectMapper objectMapper;
    private final OrganizationGoalRepository repository;

    @Override
    public OrganizationGoalDto create(OrganizationGoalDto newGoal) {
        User creator = new User();
        creator.setId(newGoal.getCreator().getId());
        DateRange period = new DateRange(newGoal.getValidPeriod().getStartDate(), null);

        OrganizationGoal goal = OrganizationGoal.builder()
                .title(newGoal.getTitle())
                .description(newGoal.getDescription())
                .rationale(newGoal.getRationale())
                .priority(newGoal.getPriority())
                .status(OrganizationGoalStatus.DRAFT)
                .validPeriod(period)
                .creator(creator)
                .build();
        repository.save(goal);
        return Mapper.map(goal, OrganizationGoalDto.class);
    }

    @Override
    public Page<OrganizationGoalsSetDto> findAll(String title, String period, List<String> statuses, List<Long> creators, Pageable pageable) {
        // Increase pageSize by a factor of 5, because one set can have 3-5 goals
        Pageable adjustedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize() * 5,
                pageable.getSort()
        );
        Specification<OrganizationGoal> spec = getSpecification(title, period, statuses, creators);
        Page<OrganizationGoal> allGoals = repository.findAll(spec, adjustedPageable);

        // Group organization goals by their validity periods
        Map<DateRange, List<OrganizationGoal>> goalsByValidityPeriod = allGoals.getContent().stream()
                .collect(Collectors.groupingBy(OrganizationGoal::getValidPeriod));

        // Convert the grouped goals into OrganizationGoalsSetDto
        List<OrganizationGoalsSetDto> goalsSets = goalsByValidityPeriod.entrySet().stream()
                .map(entry -> OrganizationGoalsSetDto.builder()
                        .validPeriod(entry.getKey())
                        .goals(entry.getValue().stream()
                                .map(goal -> Mapper.map(goal, OrganizationGoalDto.class))
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        // Custom sort: first by status (DRAFT > VALID > ARCHIVED), then by validPeriod.startDate (newest first)
        goalsSets.sort(Comparator.comparing((OrganizationGoalsSetDto set) -> {
            switch (OrganizationGoalStatus.valueOf(set.getStatus())) {
                case DRAFT:
                    return 1;
                case VALID:
                    return 2;
                case ARCHIVED:
                    return 3;
                default:
                    return 4;
            }
        }).thenComparing(set -> set.getValidPeriod().getStartDate(), Comparator.reverseOrder()));
        return new PageImpl<>(goalsSets, pageable, goalsSets.size());
    }
    private Specification<OrganizationGoal> getSpecification(String title, String period, List<String> statuses, List<Long> creators) {
        DateRange dateRange = getDateRange(period);
        return Specification.where(
                OrganizationGoalSpecifications.titleLike(title))
                .and(OrganizationGoalSpecifications.afterStartDate(dateRange))
                .and(OrganizationGoalSpecifications.beforeEndDate(dateRange))
                .and(OrganizationGoalSpecifications.statusIn(statuses))
                .and(OrganizationGoalSpecifications.creatorIn(creators));
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

    @Override
    public OrganizationGoalsSetDto findCurrent() {
        OrganizationGoalsSetDto goalSet = new OrganizationGoalsSetDto();
        List<OrganizationGoalDto> validGoalsList = new ArrayList<>();

        Page<OrganizationGoal> currentGoals = repository.findByValidPeriodEndDateIsNull(Pageable.unpaged());
        for (OrganizationGoal goal: currentGoals){
            AccountantDto creator = new AccountantDto(goal.getCreator().getId(),goal.getCreator().getUsername(),goal.getCreator().getName(),goal.getCreator().getSurname(),goal.getCreator().getEmail());
            validGoalsList.add(new OrganizationGoalDto(goal.getId(), goal.getTitle(), goal.getDescription(), goal.getRationale(), goal.getPriority(), goal.getStatus().toString(), goal.getValidPeriod(), creator));
            goalSet.setValidPeriod(goal.getValidPeriod());
        }
        goalSet.setStatus("VALID");
        goalSet.setGoals(validGoalsList);

        return goalSet;
    }

    @Override
    public OrganizationGoalDto findById(Long id) {
        OrganizationGoal goal = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization goal not found with ID: " + id));
        return Mapper.map(goal, OrganizationGoalDto.class);
    }

    @Override
    public OrganizationGoalDto update(OrganizationGoalDto oldGoal){
        OrganizationGoal goal = repository.findById(oldGoal.getId())
                .orElseThrow(() -> new EntityNotFoundException("Organization goal not found with ID: " + oldGoal.getId()));

        if (oldGoal.getStatus().equals("DRAFT")){
            goal.setTitle(oldGoal.getTitle());
        }
        goal.setDescription(oldGoal.getDescription());
        goal.setRationale(oldGoal.getRationale());
        goal.setPriority(oldGoal.getPriority());
        if (!goal.getValidPeriod().isValid()){
            throw new IllegalArgumentException("Invalid organization goal date range");
        }
        goal.setValidPeriod(oldGoal.getValidPeriod());
        repository.save(goal);
        return Mapper.map(goal, OrganizationGoalDto.class);
    }

    @Override
    public void delete(Long id) {
        OrganizationGoal goal = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization goal not found with ID: " + id));

        repository.delete(goal);
    }

    @Override
    public OrganizationGoalsSetDto publish(OrganizationGoalsSetDto newValid) {
        List<OrganizationGoalDto> newValidGoalsList = new ArrayList<>();
        DateRange period = new DateRange();

        // update old VALID set to ARCHIVED
        Page<OrganizationGoal> currentGoals = repository.findByValidPeriodEndDateIsNull(Pageable.unpaged());
        for (OrganizationGoal goal: currentGoals){
            goal.setValidPeriod(new DateRange(goal.getValidPeriod().getStartDate(), LocalDate.now()));
            goal.setStatus(OrganizationGoalStatus.ARCHIVED);
            repository.save(goal);
        }
        // set DRAFT set to new VALID set
        for (OrganizationGoalDto goal: newValid.getGoals()){
            if(!goal.getStatus().equals("DRAFT")){
                throw new IllegalArgumentException("Invalid organization goal set. Set has to be draft to be published!");
            } else {
                OrganizationGoal updateGoal = repository.findById(goal.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Organization goal not found with ID: " + goal.getId()));
                updateGoal.setValidPeriod(period);
                updateGoal.setStatus(OrganizationGoalStatus.VALID);
                repository.save(updateGoal);
                AccountantDto creator = new AccountantDto(updateGoal.getCreator().getId(),updateGoal.getCreator().getUsername(),updateGoal.getCreator().getName(),updateGoal.getCreator().getSurname(),updateGoal.getCreator().getEmail());
                newValidGoalsList.add(new OrganizationGoalDto(updateGoal.getId(), updateGoal.getTitle(), updateGoal.getDescription(), updateGoal.getRationale(), updateGoal.getPriority(), updateGoal.getStatus().toString(), updateGoal.getValidPeriod(), creator));
            }
        }

        newValid.setGoals(newValidGoalsList);
        return newValid;
    }
}
