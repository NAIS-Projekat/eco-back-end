package com.eco.environet.finance.services.impl;

import com.eco.environet.finance.dto.FixedExpensesEstimationDto;
import com.eco.environet.finance.model.*;
import com.eco.environet.finance.repository.BudgetPlanRepository;
import com.eco.environet.finance.repository.FixedExpensesEstimationRepository;
import com.eco.environet.finance.repository.FixedExpensesEstimationSpecifications;
import com.eco.environet.finance.services.FixedExpensesEstimationService;
import com.eco.environet.users.model.OrganizationMember;
import com.eco.environet.users.repository.OrganizationMemberRepository;
import com.eco.environet.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FixedExpensesEstimationServiceImpl implements FixedExpensesEstimationService {
    @Value("${baseFrontUrl}")
    private String baseFrontUrl;
    private final FixedExpensesEstimationRepository repository;
    private final BudgetPlanRepository budgetPlanRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    private FixedExpensesEstimation setEstimation(FixedExpensesEstimation estimation) {
        estimation.setFixedExpense();
        return estimation;
    }
    private Page<FixedExpensesEstimationDto> paginate(List<FixedExpensesEstimation> list, Pageable pageable){
        List<FixedExpensesEstimationDto> result = new ArrayList<>();
        for (FixedExpensesEstimation estimation : list){
            setEstimation(estimation);
            var dto = Mapper.map(estimation, FixedExpensesEstimationDto.class);
            result.add(dto);
        }

        // Implement pagination
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<FixedExpensesEstimationDto> pagedResult;

        if (result.size() < startItem) {
            pagedResult = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, result.size());
            pagedResult = result.subList(startItem, toIndex);
        }
        return new PageImpl<>(pagedResult, pageable, result.size());
    }

    @Override
    public List<FixedExpensesEstimationDto> generateEstimationForBudgetPlan(Long id){
        BudgetPlan budgetPlan = budgetPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Budget Plan not found with ID: " + id));
        List<FixedExpensesEstimation> existing = repository.findByBudgetPlanId(id);
        if (!existing.isEmpty()){
            existing.forEach(this::setEstimation);
            return Mapper.mapList(existing, FixedExpensesEstimationDto.class);
        }

        OrganizationMember creator = new OrganizationMember();
        creator.setId(budgetPlan.getAuthor().getId());
        List<OrganizationMember> employees = organizationMemberRepository.findAllActiveOrganizationMembers();
        for (OrganizationMember employee : employees){
            Salary newSalary = createSalaryForEmployee(budgetPlan.getFiscalDateRange(), creator, employee);
            FixedExpensesEstimation newEstimation = new FixedExpensesEstimation(budgetPlan, newSalary);
            existing.add(newEstimation);
            repository.save(newEstimation);
        }
        existing.forEach(this::setEstimation);
        return Mapper.mapList(existing, FixedExpensesEstimationDto.class);
    }
    private Salary createSalaryForEmployee(DateRange period, OrganizationMember creator, OrganizationMember employee){
        FixedExpenses newExpense = FixedExpenses.fixedExpensesBuilder()
                .type(FixedExpensesType.SALARY)
                .period(period)
                .amount(0)
                .creator(creator)
                .createdOn(new Timestamp(System.currentTimeMillis()))
                .description(employee.getName() + " " + employee.getSurname() + " - Salary for period " + period.getStartDate().toString() + " to " + period.getStartDate().toString())
                .build();
        return new Salary(newExpense, employee, 0);
    }

    @Override
    public FixedExpensesEstimationDto create(FixedExpensesEstimationDto fixedExpensesEstimationDto){
        // get Budget Plan
        BudgetPlan budgetPlan = budgetPlanRepository.findById(fixedExpensesEstimationDto.getBudgetPlan().getId())
                .orElseThrow(() -> new EntityNotFoundException("Budget Plan not found with ID: " + fixedExpensesEstimationDto.getBudgetPlan().getId()));
        OrganizationMember creator = new OrganizationMember();
        creator.setId(budgetPlan.getAuthor().getId());

        FixedExpensesType fixedExpensesType;
        try {
            fixedExpensesType = FixedExpensesType.valueOf(fixedExpensesEstimationDto.getFixedExpense().getType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid type provided: " + fixedExpensesEstimationDto.getFixedExpense().getType());
        }
        if (fixedExpensesType == FixedExpensesType.SALARY){
            throw new IllegalArgumentException("Invalid type provided! Type can't be SALARY!");
        }

        FixedExpensesEstimation newEstimation = FixedExpensesEstimation.fixedExpensesEstimationBuilder()
                .budgetPlan(budgetPlan)
                .fixedExpenseId(budgetPlan.getId())
                .type(fixedExpensesType)
                .period(budgetPlan.getFiscalDateRange())
                .amount(0)
                .creator(creator)
                .createdOn(new Timestamp(System.currentTimeMillis()))
                .description(fixedExpensesEstimationDto.getFixedExpense().getDescription())
                .employee(null)
                .overtimeHours(0)
                .build();
        setEstimation(newEstimation);
        repository.save(newEstimation);
        return Mapper.map(newEstimation, FixedExpensesEstimationDto.class);
    }

    @Override
    public Page<FixedExpensesEstimationDto> findAll(Long budgetPlanId, List<String> types, List<Long> employees, Pageable pageable) {
        Specification<FixedExpensesEstimation> spec = getSpecification(budgetPlanId, types, employees);
        Page<FixedExpensesEstimation> all = repository.findAll(spec, pageable);
        all.forEach(this::setEstimation);
        return Mapper.mapPage(all, FixedExpensesEstimationDto.class);
    }
    private Specification<FixedExpensesEstimation> getSpecification(Long budgetPlanId, List<String> types, List<Long> employees){
        List<FixedExpensesType> typeList = getTypesList(types);
        return Specification.where(
                FixedExpensesEstimationSpecifications.typeIn(typeList))
                .and(FixedExpensesEstimationSpecifications.budgetPlanIn(budgetPlanId))
                .and(FixedExpensesEstimationSpecifications.employeeIn(employees));
    }
    private List<FixedExpensesType> getTypesList(List<String> typesString){
        if (typesString == null || typesString.isEmpty()){
            return Arrays.asList(FixedExpensesType.values());
        }
        List<FixedExpensesType> result = new ArrayList<>();
        for (String type : typesString){
            FixedExpensesType filtered = FixedExpensesType.valueOf(type);
            result.add(filtered);
        }
        return result;
    }

    @Override
    public FixedExpensesEstimationDto findById(Long id){
        FixedExpensesEstimation estimation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fixed expense estimation not found with ID: " + id));
        setEstimation(estimation);
        return Mapper.map(estimation, FixedExpensesEstimationDto.class);
    }

    @Override
    public FixedExpensesEstimationDto update(FixedExpensesEstimationDto fixedExpensesEstimationDto){
        FixedExpensesEstimation estimation = repository.findById(fixedExpensesEstimationDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Fixed expense estimation not found with ID: " + fixedExpensesEstimationDto.getId()));

        if (!estimation.getType().equals("SALARY")){
            // TODO type update
            //estimation.setType(fixedExpensesEstimationDto.getFixedExpense().getType());
            estimation.setAmount(fixedExpensesEstimationDto.getFixedExpense().getAmount());
            estimation.setDescription(fixedExpensesEstimationDto.getFixedExpense().getDescription());
            repository.save(estimation);
            return Mapper.map(estimation, FixedExpensesEstimationDto.class);
        } else {
            // SALARY update
            estimation.setDescription(fixedExpensesEstimationDto.getFixedExpense().getDescription());
            estimation.setOvertimeHours(fixedExpensesEstimationDto.getFixedExpense().getOvertimeHours());
            // amount update
            var employee = new OrganizationMember(
                    null,
                    fixedExpensesEstimationDto.getFixedExpense().getEmployee().getWage(),
                    fixedExpensesEstimationDto.getFixedExpense().getEmployee().getWorkingHours(),
                    fixedExpensesEstimationDto.getFixedExpense().getEmployee().getOvertimeWage()
                    );
            employee.setId(fixedExpensesEstimationDto.getFixedExpense().getEmployee().getId());
            Salary salary = new Salary(employee, fixedExpensesEstimationDto.getFixedExpense().getOvertimeHours());
            estimation.setAmount(salary.getAmount());

            repository.save(estimation);
            setEstimation(estimation);
            return Mapper.map(estimation, FixedExpensesEstimationDto.class);
        }
    }

    @Override
    public void delete(Long id){
        FixedExpensesEstimation estimation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fixed expense estimation not found with ID: " + id));
        repository.delete(estimation);
    }
}
