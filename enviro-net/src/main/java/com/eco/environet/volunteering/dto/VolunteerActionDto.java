package com.eco.environet.volunteering.dto;


import com.eco.environet.volunteering.model.Difficulty;
import com.eco.environet.volunteering.model.VolunteerActionStatus;
import com.eco.environet.volunteering.model.VolunteerActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerActionDto {
    private Long id;
    private String title;
    private Long projectId;
    private Long supervisorId;
    private Long creatorId;
    private Integer participantCount;
    private boolean applicationLimit;
    private Difficulty difficulty;
    private String description;
    private Timestamp date;
    private float durationHours;
    private VolunteerActionType type;
    private VolunteerActionStatus status;
    private double budget;
    private boolean projectDependency;
}
