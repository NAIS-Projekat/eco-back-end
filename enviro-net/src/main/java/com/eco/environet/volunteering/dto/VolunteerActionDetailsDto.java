package com.eco.environet.volunteering.dto;

import com.eco.environet.volunteering.model.VolunteerActionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerActionDetailsDto {
    private Integer participantCount;
    private Boolean isSupervisor;
    private Boolean hasApplied;
}
