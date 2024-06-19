package com.eco.environet.projects.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamMemberCreationDto {

    @NotNull
    private Long projectId;

    @NotNull
    private Long userId;
}
