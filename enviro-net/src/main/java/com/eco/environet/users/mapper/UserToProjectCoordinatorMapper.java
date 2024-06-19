package com.eco.environet.users.mapper;

import com.eco.environet.users.dto.UserDto;
import com.eco.environet.volunteering.dto.ProjectCoordinatorDto;

public class UserToProjectCoordinatorMapper {
    public static ProjectCoordinatorDto mapToProjectCoordinatorDto(UserDto userDto) {
        ProjectCoordinatorDto coordinatorDto = new ProjectCoordinatorDto();
        coordinatorDto.setId(userDto.getId());
        coordinatorDto.setUsername(userDto.getUsername());
        coordinatorDto.setName(userDto.getName());
        coordinatorDto.setSurname(userDto.getSurname());
        coordinatorDto.setEmail(userDto.getEmail());
        coordinatorDto.setPhoneNumber(userDto.getPhoneNumber());
        return coordinatorDto;
    }
}
