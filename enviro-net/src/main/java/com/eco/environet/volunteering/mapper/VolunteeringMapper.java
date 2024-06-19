package com.eco.environet.volunteering.mapper;

import com.eco.environet.users.dto.UserDto;
import com.eco.environet.users.model.RegisteredUser;
import com.eco.environet.volunteering.dto.ProjectCoordinatorDto;
import com.eco.environet.volunteering.dto.RegisteredUserDto;
import com.eco.environet.volunteering.dto.VolunteerActionBasicInfoDto;
import com.eco.environet.volunteering.dto.VolunteerActionDto;
import com.eco.environet.volunteering.model.VolunteerAction;

public class VolunteeringMapper {

    public static VolunteerActionBasicInfoDto mapToBasicInfoDto(VolunteerAction action) {
        VolunteerActionBasicInfoDto basicInfoDto = new VolunteerActionBasicInfoDto();
        // Map properties from VolunteerAction to VolunteerActionBasicInfoDto
        basicInfoDto.setId(action.getId());
        basicInfoDto.setTitle(action.getTitle());
        if(action.getProject() == null){
            basicInfoDto.setProjectName("INDEPENDENT");
        }
        else{
            basicInfoDto.setProjectName(action.getProject().getName());
        }

        basicInfoDto.setStatus(action.getStatus());
        return basicInfoDto;
    }

    public static VolunteerActionDto mapToDto(VolunteerAction action) {
        VolunteerActionDto dto = new VolunteerActionDto();
        dto.setId(action.getId());
        dto.setTitle(action.getTitle());
        dto.setProjectId(action.getProject() != null ? action.getProject().getId() : null);
        dto.setSupervisorId(action.getSupervisor().getId());
        dto.setCreatorId(action.getCreator().getId());
        dto.setParticipantCount(action.getParticipantCount());
        dto.setApplicationLimit(action.isApplicationLimit());
        dto.setDifficulty(action.getDifficulty());
        dto.setDescription(action.getDescription());
        dto.setDate(action.getDate());
        dto.setDurationHours(action.getDurationHours());
        dto.setType(action.getType());
        dto.setStatus(action.getStatus());
        dto.setBudget(action.getBudget());
        dto.setProjectDependency(action.isProjectDependency());
        return dto;
    }
    public static RegisteredUserDto toRegisteredUserDto(RegisteredUser registeredUser) {
        if (registeredUser == null) {
            return null;
        }
        return RegisteredUserDto.builder()
                .id(registeredUser.getId())
                .email(registeredUser.getEmail())
                .username(registeredUser.getUsername())
                .name(registeredUser.getName())
                .surname(registeredUser.getSurname())
                .phoneNumber(registeredUser.getPhoneNumber())
                .points(registeredUser.getPoints())
                .build();
    }
}
