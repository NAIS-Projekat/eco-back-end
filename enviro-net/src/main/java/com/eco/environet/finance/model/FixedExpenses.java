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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "expense_type")
@Builder(builderMethodName = "fixedExpensesBuilder")
@Table(name="fixed_expenses", schema = "finance")
public class FixedExpenses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

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
}
