package com.eco.environet.finance.repository;

import com.eco.environet.finance.model.OrganizationGoal;
import com.eco.environet.finance.model.DateRange;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class OrganizationGoalSpecifications {
    private OrganizationGoalSpecifications () {}
    public static Specification<OrganizationGoal> titleLike(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title != null && !title.isEmpty()) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
            }
            else {
                return criteriaBuilder.conjunction(); // Always true if title is null
            }
        };
    }
    public static Specification<OrganizationGoal> afterStartDate(DateRange dateRange) {
        return (root, query, criteriaBuilder) -> {
            if (dateRange.getStartDate() != null) {
                return criteriaBuilder.greaterThanOrEqualTo(
                        criteriaBuilder.function("DATE", LocalDate.class, root.get("validPeriod").get("startDate")),
                        dateRange.getStartDate());
            } else {
                return criteriaBuilder.conjunction(); // Always true if startDate is null
            }
        };
    }

    public static Specification<OrganizationGoal> beforeEndDate(DateRange dateRange) {
        return (root, query, criteriaBuilder) -> {
            if (dateRange.getEndDate() != null) {
                return criteriaBuilder.lessThanOrEqualTo(
                        criteriaBuilder.function("DATE", LocalDate.class, root.get("validPeriod").get("endDate")),
                        dateRange.getEndDate());
            } else {
                return criteriaBuilder.conjunction(); // Always true if endDate is null
            }
        };
    }
    public static Specification<OrganizationGoal> statusIn(List<String> statusList){
        return ((root, query, criteriaBuilder) -> {
            if (statusList != null && !statusList.isEmpty()) {
                return root.get("status").in(statusList);
            }
            else {
                return criteriaBuilder.conjunction(); // Always true if creatorList is null
            }
        });
    }
    public static Specification<OrganizationGoal> creatorIn(List<Long> creatorList){
        return (root, query, criteriaBuilder) -> {
            if (creatorList != null && !creatorList.isEmpty()) {
                return root.get("creator").get("id").in(creatorList);
            }
            else {
                return criteriaBuilder.conjunction(); // Always true if creatorList is null
            }
        };
    }
}
