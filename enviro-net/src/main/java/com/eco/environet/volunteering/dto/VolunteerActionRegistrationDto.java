package com.eco.environet.volunteering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerActionRegistrationDto {
    private VolunteerActionDto volunteerAction;
    private List<RegisteredUserDto> signedUpVolunteers;
    private List<RegisteredUserDto> appearedVolunteers;
    private int totalSignedUp;
    private int totalAppeared;
}
