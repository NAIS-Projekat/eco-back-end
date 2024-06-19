package com.eco.environet.finance.repository;

import com.eco.environet.finance.model.DateRange;
import com.eco.environet.finance.model.FixedExpenses;
import com.eco.environet.finance.model.FixedExpensesType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class FixedExpensesSpecifications {
    private FixedExpensesSpecifications () {}
    public static Specification<FixedExpenses> afterStartDate(DateRange dateRange) {
        return (root, query, criteriaBuilder) -> {
            if (dateRange.getStartDate() != null) {
                return criteriaBuilder.greaterThanOrEqualTo(
                                criteriaBuilder.function("DATE", LocalDate.class, root.get("period").get("startDate")),
                                dateRange.getStartDate());
            } else {
                return criteriaBuilder.conjunction(); // Always true if startDate is null
            }
        };
    }

    public static Specification<FixedExpenses> beforeEndDate(DateRange dateRange) {
        return (root, query, criteriaBuilder) -> {
            if (dateRange.getEndDate() != null) {
                return criteriaBuilder.lessThanOrEqualTo(
                                criteriaBuilder.function("DATE", LocalDate.class, root.get("period").get("endDate")),
                                dateRange.getEndDate());
            } else {
                return criteriaBuilder.conjunction(); // Always true if endDate is null
            }
        };
    }
    public static Specification<FixedExpenses> typeIn(List<FixedExpensesType> typeList){
        return ((root, query, criteriaBuilder) -> root.get("type").in(typeList));
    }
    public static Specification<FixedExpenses> creatorIn(List<Long> creatorList){
        return (root, query, criteriaBuilder) -> {
            if (creatorList != null && !creatorList.isEmpty()) {
                return root.get("creator").get("id").in(creatorList);
            }
            else {
                return criteriaBuilder.conjunction(); // Always true if creatorList is null
            }
        };
    }
    public static Specification<FixedExpenses> employeeIn(List<Long> employeeList){
        return (root, query, criteriaBuilder) -> {
            if (employeeList != null && !employeeList.isEmpty()) {
                return root.get("employee").get("id").in(employeeList);
            }
            else {
                return criteriaBuilder.conjunction(); // Always true if creatorList is null
            }
        };
    }
}
