package com.eco.environet.volunteering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignPointsRequestDto {
    private Long volunteerId;
    private Integer addedPoints;
}
