package com.eco.environet.education.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCompletionResponse {
    private Long lectureId;
    private Long userId;
    private double points;
}
