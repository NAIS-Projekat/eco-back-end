package com.eco.environet.finance.repository;

import com.eco.environet.finance.model.FixedExpensesEstimation;
import com.eco.environet.finance.model.FixedExpensesType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class FixedExpensesEstimationSpecifications {
    private FixedExpensesEstimationSpecifications (){}
    public static Specification<FixedExpensesEstimation> budgetPlanIn(Long budgetPlanId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("budgetPlan").get("id"), budgetPlanId);
    }
    public static Specification<FixedExpensesEstimation> typeIn(List<FixedExpensesType> typeList){
        return ((root, query, criteriaBuilder) -> root.get("type").in(typeList));
    }
    public static Specification<FixedExpensesEstimation> employeeIn(List<Long> employeeList){
        return (root, query, criteriaBuilder) -> {
            if (employeeList != null && !employeeList.isEmpty()) {
                return root.get("employee").get("id").in(employeeList);
            }
            else {
                return criteriaBuilder.conjunction(); // Always true if employeeList is null
            }
        };
    }
}
