package com.eco.environet.finance.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.eco.environet.users.model.User;

@Data
@Entity
@NoArgsConstructor
@Builder
@Table(name="organization_goal", schema = "finance")
public class OrganizationGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "rationale")
    private String rationale;

    @Column(name = "priority")
    private int priority;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private OrganizationGoalStatus status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="startDate", column=@Column(name="start_date", nullable = false)),
            @AttributeOverride(name="endDate", column=@Column(name="end_date"))
    })
    private DateRange validPeriod;

    @ManyToOne
    private User creator;

    public OrganizationGoal(Long id, String title, String description, String rationale, int priority, OrganizationGoalStatus status, DateRange validPeriod, User user){
        this.id = id;
        this.title = title;
        this.description = description;
        this.rationale = rationale;
        this.priority = priority;
        this.status = status;
        this.validPeriod = validPeriod;
        this.creator = user;
    }
}
