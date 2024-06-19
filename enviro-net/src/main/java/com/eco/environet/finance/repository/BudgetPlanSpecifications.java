package com.eco.environet.finance.repository;

import com.eco.environet.finance.model.BudgetPlan;
import com.eco.environet.finance.model.BudgetPlanStatus;
import com.eco.environet.finance.model.DateRange;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
public class BudgetPlanSpecifications {
    private BudgetPlanSpecifications() {}

    public static Specification<BudgetPlan> nameLike(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
    public static Specification<BudgetPlan> afterStartDate(DateRange dateRange) {
        return (root, query, criteriaBuilder) -> {
            if (dateRange.getStartDate() != null) {
                return criteriaBuilder.greaterThanOrEqualTo(
                        criteriaBuilder.function("DATE", LocalDate.class, root.get("fiscalDateRange").get("startDate")),
                        dateRange.getStartDate());
            } else {
                return criteriaBuilder.conjunction(); // Always true if startDate is null
            }
        };
    }
    public static Specification<BudgetPlan> beforeEndDate(DateRange dateRange) {
        return (root, query, criteriaBuilder) -> {
            if (dateRange.getEndDate() != null) {
                return criteriaBuilder.lessThanOrEqualTo(
                        criteriaBuilder.function("DATE", LocalDate.class, root.get("fiscalDateRange").get("endDate")),
                        dateRange.getEndDate());
            } else {
                return criteriaBuilder.conjunction(); // Always true if endDate is null
            }
        };
    }
    public static Specification<BudgetPlan> statusIn(List<BudgetPlanStatus> statusList, Long currentUserId) {
        return (root, query, criteriaBuilder) -> {
            if (statusList.contains(BudgetPlanStatus.DRAFT)) {
                return criteriaBuilder.or(
                        criteriaBuilder.and(
                                root.get("status").in(statusList),
                                criteriaBuilder.notEqual(root.get("status"), BudgetPlanStatus.DRAFT),
                                criteriaBuilder.notEqual(root.get("status"), BudgetPlanStatus.CLOSED)
                        ), criteriaBuilder.and(
                                criteriaBuilder.equal(root.get("status"), BudgetPlanStatus.DRAFT),
                                criteriaBuilder.equal(root.get("author").get("id"), currentUserId),
                                criteriaBuilder.notEqual(root.get("status"), BudgetPlanStatus.CLOSED)
                        )
                );
            } else {
                return root.get("status").in(statusList);
            }
        };
    }
    public static Specification<BudgetPlan> authorIn(List<Long> authorList){
        return (root, query, criteriaBuilder) -> {
            if (authorList != null && !authorList.isEmpty()) {
                return root.get("author").get("id").in(authorList);
            }
            else {
                return criteriaBuilder.conjunction(); // Always true if authorList is null
            }
        };
    }
}
