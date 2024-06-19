package com.eco.environet.projects.model;

import com.eco.environet.projects.model.id.TeamMemberId;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@IdClass(TeamMemberId.class)
@Table(name = "team_members", schema = "projects")
public class TeamMember {

    @Id
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;
}