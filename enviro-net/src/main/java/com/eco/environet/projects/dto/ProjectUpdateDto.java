package com.eco.environet.projects.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProjectUpdateDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Duration in months is required")
    @Positive(message = "Duration in months must be a positive number")
    private int durationMonths;

    @NotNull(message = "Budget is required")
    @Positive(message = "Budget must be a positive number")
    private double budget;
}
