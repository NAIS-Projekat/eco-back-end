package com.eco.environet.education.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestExecutionDto {
    private Long userId;
    private Long lectureId;
    private double points;
    private Boolean finished;
}
