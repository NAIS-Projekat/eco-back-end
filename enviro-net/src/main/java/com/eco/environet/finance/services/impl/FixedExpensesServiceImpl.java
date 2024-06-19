package com.eco.environet.finance.services.impl;

import com.eco.environet.finance.dto.EmployeeDto;
import com.eco.environet.finance.dto.FixedExpensesDto;
import com.eco.environet.finance.model.DateRange;
import com.eco.environet.finance.model.FixedExpenses;
import com.eco.environet.finance.model.FixedExpensesType;
import com.eco.environet.finance.model.Salary;
import com.eco.environet.finance.repository.FixedExpensesRepository;
import com.eco.environet.finance.repository.FixedExpensesSpecifications;
import com.eco.environet.finance.repository.SalaryRepository;
import com.eco.environet.finance.services.FixedExpensesService;
import com.eco.environet.users.model.OrganizationMember;
import com.eco.environet.users.model.Role;
import com.eco.environet.users.repository.OrganizationMemberRepository;
import com.eco.environet.util.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FixedExpensesServiceImpl  implements FixedExpensesService {
    @Value("${baseFrontUrl}")
    private String baseFrontUrl;
    private final ObjectMapper objectMapper;
    private final FixedExpensesRepository repository;
    private final SalaryRepository salaryRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    @Override
    public Page<FixedExpensesDto> lastMonthSalaryExpenses(Long creatorId, Pageable pageable) {
        LocalDate endDate = LocalDate.now().withDayOfMonth(1);
        LocalDate startDate = endDate.minusMonths(1);
        DateRange period = new DateRange(startDate, endDate);

        // Check if salary expenses already exist for the period
        List<Salary> existingSalaryExpenses = salaryRepository.findByPeriod(startDate, endDate);
        if (!existingSalaryExpenses.isEmpty()) {
            List<FixedExpensesDto> existingDtos = new ArrayList<>();
            for (Salary expense : existingSalaryExpenses) {
                EmployeeDto creatorDto = new EmployeeDto();
                creatorDto.setId(expense.getCreator().getId());
                FixedExpensesDto dto = new FixedExpensesDto(
                        expense.getId(), expense.getType().toString(), expense.getPeriod(), expense.getAmount(), creatorDto, expense.getCreatedOn(), expense.getDescription(), null, expense.getOvertimeHours());
                // TODO - fix bug
                // doesn't fetch first employee, the others are fetched correctly ???
                if (expense.getEmployee() != null){
                    dto.setEmployee(new EmployeeDto(expense.getEmployee().getId(), expense.getEmployee().getUsername(), expense.getEmployee().getName(), expense.getEmployee().getSurname(), expense.getEmployee().getEmail(), expense.getEmployee().getWage(), expense.getEmployee().getWorkingHours(), expense.getEmployee().getOvertimeWage()));
                }
                existingDtos.add(dto);
            }
            // fetch other non salary expenses
            List<FixedExpenses> existingFixedExpenses = repository.findNonSalaryByPeriod(startDate, endDate);
            if (!existingFixedExpenses.isEmpty()){
                for (FixedExpenses expense : existingFixedExpenses){
                    FixedExpensesDto dto = Mapper.map(expense, FixedExpensesDto.class);
                    existingDtos.add(dto);
                }
            }

            return paginate(existingDtos, pageable);
        }

        // Create new salary for every active employee for last month
        OrganizationMember creatorMember = organizationMemberRepository.findById(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("Creator not found with ID: " + creatorId));
        if (!creatorMember.getRole().equals(Role.ACCOUNTANT)){
            throw new IllegalArgumentException("Invalid creator provided: " + creatorMember);
        }
        OrganizationMember creator = new OrganizationMember();
        creator.setId(creatorId);

        List<OrganizationMember> employees = organizationMemberRepository.findAllActiveOrganizationMembers();
        List<FixedExpensesDto> result = new ArrayList<>();
        for (OrganizationMember employee : employees){
            var newSalary = createSalaryForEmployee(period, creator, employee);
            result.add(newSalary);
        }
        return paginate(result, pageable);
    }
    private FixedExpensesDto createSalaryForEmployee(DateRange period, OrganizationMember creator, OrganizationMember employee){
        FixedExpenses newExpense = FixedExpenses.fixedExpensesBuilder()
                .type(FixedExpensesType.SALARY)
                .period(period)
                .amount(0)
                .creator(creator)
                .createdOn(new Timestamp(System.currentTimeMillis()))
                .description(employee.getName() + " " + employee.getSurname() + " - Salary for period " + period.getStartDate().toString() + " to " + period.getStartDate().toString())
                .build();
        Salary newSalary = new Salary(newExpense, employee, 0);
        salaryRepository.save(newSalary);
        newExpense.setId(newSalary.getId());
        var dto = Mapper.map(newExpense, FixedExpensesDto.class);
        dto.setEmployee(new EmployeeDto(employee.getId(), employee.getUsername(), employee.getName(), employee.getSurname(), employee.getEmail(), employee.getWage(), employee.getWorkingHours(), employee.getOvertimeWage()));
        return dto;
    }
    private Page<FixedExpensesDto> paginate(List<FixedExpensesDto> result, Pageable pageable){
        // Implement pagination
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<FixedExpensesDto> pagedResult;

        if (result.size() < startItem) {
            pagedResult = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, result.size());
            pagedResult = result.subList(startItem, toIndex);
        }
        return new PageImpl<>(pagedResult, pageable, result.size());
    }

    @Override
    public FixedExpensesDto create(FixedExpensesDto newFixedExpenseDto){
        // Determine the period for the last month
        LocalDate endDate = LocalDate.now().withDayOfMonth(1);
        LocalDate startDate = endDate.minusMonths(1);
        DateRange period = new DateRange(startDate, endDate);

        OrganizationMember creator = new OrganizationMember();
        creator.setId(newFixedExpenseDto.getCreator().getId());

        FixedExpensesType fixedExpensesType;
        try {
            fixedExpensesType = FixedExpensesType.valueOf(newFixedExpenseDto.getType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid type provided: " + newFixedExpenseDto.getType());
        }
        if (fixedExpensesType == FixedExpensesType.SALARY){
            throw new IllegalArgumentException("Invalid type provided! Type can't be SALARY!");
        }

        FixedExpenses newExpense = FixedExpenses.fixedExpensesBuilder()
                .type(fixedExpensesType)
                .period(period)
                .amount(newFixedExpenseDto.getAmount())
                .creator(creator)
                .createdOn(new Timestamp(System.currentTimeMillis()))
                .description(newFixedExpenseDto.getDescription())
                .build();
        repository.save(newExpense);
        return Mapper.map(newExpense, FixedExpensesDto.class);
    }

    @Override
    public Page<FixedExpensesDto> findAll(String period, List<String> types, List<Long> employees, List<Long> creators, Pageable pageable) {
        Specification<FixedExpenses> spec = getSpecification(period, types, employees, creators);
        Page<FixedExpenses> all = repository.findAll(spec, pageable);
        Page<FixedExpensesDto> allDtos = Mapper.mapPage(all, FixedExpensesDto.class);
        return allDtos;
    }
    private Specification<FixedExpenses> getSpecification(String period, List<String> types, List<Long> employees, List<Long> creators){
        DateRange dateRange = getDateRange(period);
        List<FixedExpensesType> typeList = getTypesList(types);
        return Specification.where(
                FixedExpensesSpecifications.typeIn(typeList))
                .and(FixedExpensesSpecifications.afterStartDate(dateRange))
                .and(FixedExpensesSpecifications.beforeEndDate(dateRange))
                .and(FixedExpensesSpecifications.employeeIn(employees))
                .and(FixedExpensesSpecifications.creatorIn(creators));
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
    public FixedExpensesDto findById(Long id){
        FixedExpenses expense = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fixed expense not found with ID: " + id));
        return Mapper.map(expense, FixedExpensesDto.class);
    }

    @Override
    public FixedExpensesDto updateSalaryExpense(FixedExpensesDto salaryExpenseDto){
        if (!salaryExpenseDto.getType().equals("SALARY")){
            throw new IllegalArgumentException("Invalid type provided! Type must be SALARY!");
        }
        if (salaryExpenseDto.getEmployee() == null){
            throw new IllegalArgumentException("Employee can't be null!");
        }
        // update employee - OrganizationMember info: wage, workingHours, overtimeWage
        OrganizationMember employee = organizationMemberRepository.findById(salaryExpenseDto.getEmployee().getId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + salaryExpenseDto.getEmployee().getId()));
        employee.setWage(salaryExpenseDto.getEmployee().getWage());
        employee.setWorkingHours(salaryExpenseDto.getEmployee().getWorkingHours());
        employee.setOvertimeWage(salaryExpenseDto.getEmployee().getOvertimeWage());
        organizationMemberRepository.save(employee);

        // update salary
        Salary salary = salaryRepository.findById(salaryExpenseDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Salary expense not found with ID: " + salaryExpenseDto.getId()));
        Salary newSalary = new Salary(salary, employee, salaryExpenseDto.getOvertimeHours());
        newSalary.setDescription(salaryExpenseDto.getDescription());
        salaryRepository.save(newSalary);

        var creatorDto = new EmployeeDto(newSalary.getCreator().getId(), newSalary.getCreator().getUsername(), newSalary.getCreator().getName(), newSalary.getCreator().getSurname(), newSalary.getCreator().getEmail(), newSalary.getCreator().getWage(), newSalary.getCreator().getWorkingHours(), newSalary.getCreator().getOvertimeWage());
        var employeeDto = new EmployeeDto(newSalary.getEmployee().getId(), newSalary.getEmployee().getUsername(), newSalary.getEmployee().getName(), newSalary.getEmployee().getSurname(), newSalary.getEmployee().getEmail(), newSalary.getEmployee().getWage(), newSalary.getEmployee().getWorkingHours(), newSalary.getEmployee().getOvertimeWage());
        return new FixedExpensesDto(newSalary.getId(), newSalary.getType().toString(), newSalary.getPeriod(), newSalary.getAmount(), creatorDto, newSalary.getCreatedOn(), newSalary.getDescription(), employeeDto, newSalary.getOvertimeHours());
    }

    @Override
    public FixedExpensesDto update(FixedExpensesDto fixedExpenseDto){
        FixedExpenses updatedExpense = repository.findById(fixedExpenseDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Fixed expense not found with ID: " + fixedExpenseDto.getId()));
        FixedExpensesType fixedExpensesType;
        try {
            fixedExpensesType = FixedExpensesType.valueOf(fixedExpenseDto.getType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid type provided: " + fixedExpenseDto.getType());
        }

        if (!fixedExpenseDto.getType().equals("SALARY")){
            updatedExpense.setAmount(fixedExpenseDto.getAmount());
        }
        updatedExpense.setType(fixedExpensesType);
        updatedExpense.setDescription(fixedExpenseDto.getDescription());
        repository.save(updatedExpense);
        return Mapper.map(updatedExpense, FixedExpensesDto.class);
    }

    @Override
    public void delete(Long id){
        FixedExpenses expense = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fixed expense not found with ID: " + id));
        repository.delete(expense);
    }
}
