package com.eco.environet.education.model;

import com.eco.environet.education.model.compositeKeys.TestExecutionId;
import com.eco.environet.users.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "test_execution", schema = "education")
@IdClass(TestExecutionId.class)
public class TestExecution {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Column(name = "points")
    private double points;

    @Column(name = "finished", nullable = false)
    private Boolean finished;
}
