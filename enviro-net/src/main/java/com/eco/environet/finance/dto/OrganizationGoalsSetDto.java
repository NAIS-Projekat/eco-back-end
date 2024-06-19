package com.eco.environet.finance.dto;

import com.eco.environet.finance.model.DateRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class OrganizationGoalsSetDto {
    private DateRange validPeriod;
    private List<OrganizationGoalDto> goals;
    private String status;// set it to be goals[0].status

    public OrganizationGoalsSetDto(DateRange period, List<OrganizationGoalDto> goals, String status){
        this.validPeriod = period;
        this.goals = goals;
        this.status = goals.get(0).getStatus();
    }
}
