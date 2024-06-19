package com.eco.environet.finance.model;

import com.eco.environet.users.model.OrganizationMember;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "fixedExpensesEstimationBuilder")
@Table(name="fixed_expenses_estimation", schema = "finance")
public class FixedExpensesEstimation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "budget_plan", nullable = false)
    private BudgetPlan budgetPlan;

    // Add fields from FixedExpenses
    @Column(name = "fixed_expense_id")
    private Long fixedExpenseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private FixedExpensesType type = FixedExpensesType.OTHER;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="startDate", column=@Column(name="start_date", nullable = false)),
            @AttributeOverride(name="endDate", column=@Column(name="end_date", nullable = false))
    })
    private DateRange period;

    @Column(name = "amount", nullable = false)
    private double amount = 0;

    @ManyToOne
    private OrganizationMember creator;

    @Column(name = "created_on", nullable = false)
    private Timestamp createdOn = Timestamp.from(Instant.now());

    @Column(name = "description")
    private String description;

    @ManyToOne
    private OrganizationMember employee;

    @Column(name = "overtime_hours")
    private double overtimeHours = 0;

    @Transient
    private Salary fixedExpense;

    public FixedExpensesEstimation(BudgetPlan budgetPlan, Salary salary) {
        this.budgetPlan = budgetPlan;
        this.fixedExpense = salary;

        this.fixedExpenseId = salary.getId();
        this.type = salary.getType();
        this.period = budgetPlan.getFiscalDateRange();
        this.amount = salary.getAmount();
        this.creator = salary.getCreator();
        this.createdOn = salary.getCreatedOn();
        this.description = salary.getDescription();
        this.employee = salary.getEmployee();
        this.overtimeHours = salary.getOvertimeHours();
    }

    public Salary getFixedExpense() {
        if (fixedExpense == null) {
            FixedExpenses expense = new FixedExpenses(
                    this.fixedExpenseId, this.type,
                    this.budgetPlan.getFiscalDateRange(), // TODO budgetPlan period
                    this.amount, this.creator, this.createdOn, this.description);
            fixedExpense = new Salary(
                    expense,
                    this.employee,
                    this.overtimeHours);
        }
        return fixedExpense;
    }
    public void setFixedExpense() {
        FixedExpenses fixed = new FixedExpenses(
                this.fixedExpenseId,
                this.type,
                this.period, this.amount, this.creator, this.createdOn, this.description);
        this.fixedExpense = new Salary(fixed, this.employee, this.overtimeHours);
        if (this.employee == null){
            this.fixedExpense = new Salary(fixed);
        }
    }
}
