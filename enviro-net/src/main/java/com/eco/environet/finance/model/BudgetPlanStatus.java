package com.eco.environet.finance.model;

import java.util.Arrays;
import java.util.List;

public enum BudgetPlanStatus {
    DRAFT,
    PENDING,
    APPROVED,
    REJECTED,
    ARCHIVED,
    CLOSED;

    public boolean isActiveBudgetPlanStatus() {
        List<BudgetPlanStatus> statusList = Arrays.asList(DRAFT, PENDING);
        return statusList.contains(this);
    }
    public static List<BudgetPlanStatus> getAllValidStatus(){
        return Arrays.asList(DRAFT, PENDING, APPROVED, REJECTED);
    }
}
