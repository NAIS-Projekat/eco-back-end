package com.eco.environet.volunteering.dto;


import com.eco.environet.volunteering.model.VolunteerActionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerActionBasicInfoDto {
    private Long id;
    private String title;
    private String projectName;
    private VolunteerActionStatus status;
}
