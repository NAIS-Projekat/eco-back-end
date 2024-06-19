package com.eco.environet.finance.model;

import com.eco.environet.users.model.OrganizationMember;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Entity
@NoArgsConstructor
@DiscriminatorValue("SALARY")
@Builder(builderMethodName = "salaryBuilder")
public class Salary extends FixedExpenses {
    @ManyToOne
    private OrganizationMember employee;

    @Column(name = "overtime_hours")
    private double overtimeHours = 0;

    public Salary(FixedExpenses fixedExpenses, OrganizationMember employee, double overtimeHours) {
        super(fixedExpenses.getId(), fixedExpenses.getType(), fixedExpenses.getPeriod(), fixedExpenses.getAmount(), fixedExpenses.getCreator(), fixedExpenses.getCreatedOn(), fixedExpenses.getDescription());
        this.employee = employee;
        this.overtimeHours = overtimeHours;
        this.setAmount(calculateAmount()); // Calculate amount
    }
    public Salary(FixedExpenses fixedExpenses) {
        super(fixedExpenses.getId(), fixedExpenses.getType(), fixedExpenses.getPeriod(), fixedExpenses.getAmount(), fixedExpenses.getCreator(), fixedExpenses.getCreatedOn(), fixedExpenses.getDescription());
        this.employee = null;
        this.overtimeHours = 0;
    }
    public Salary(OrganizationMember employee, double overtimeHours) {
        super();
        this.employee = employee;
        this.overtimeHours = overtimeHours;
        this.setAmount(calculateAmount()); // Calculate amount
    }
    public Salary(Salary salary, OrganizationMember employee, double overtimeHours){
        super(salary.getId(), salary.getType(), salary.getPeriod(), salary.getAmount(), salary.getCreator(), salary.getCreatedOn(), salary.getDescription());
        this.employee = employee;
        this.overtimeHours = overtimeHours;
        this.setAmount(calculateAmount()); // Calculate amount
    }

    // Method to calculate amount
    private double calculateAmount() {
        double amount = 0;
        if (employee != null) {
            double regularPayment = employee.getWage() * employee.getWorkingHours() * this.getPeriod().getWorkingDays();
            double overtimePayment = employee.getOvertimeWage() * overtimeHours;
            amount = regularPayment + overtimePayment;
        }
        return new BigDecimal(amount).setScale(4, RoundingMode.HALF_UP).doubleValue();
    }
}
