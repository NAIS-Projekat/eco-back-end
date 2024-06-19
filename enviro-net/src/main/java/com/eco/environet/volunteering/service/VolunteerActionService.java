package com.eco.environet.volunteering.service;

import com.eco.environet.users.model.RegisteredUser;
import com.eco.environet.volunteering.dto.*;
import com.eco.environet.volunteering.model.VolunteerAction;

import java.util.List;
import java.util.Optional;

public interface VolunteerActionService {

    List<VolunteerActionBasicInfoDto> getVolunteerActionsForRegisteredUser(Long userId);
    List<RegisteredUserDto> getRegisteredUsersForVolunteerAction(Long actionId);
    void signUpForVolunteerAction(Long userId, Long actionId);
    List<VolunteerAction> getAllVolunteerActions();
    Optional<VolunteerAction> getVolunteerActionById(Long id);
    List<VolunteerAction> getVolunteerActionsByProjectId(Long projectId);
    List<VolunteerActionBasicInfoDto> getVolunteerActionsByCreatorId(Long creatorId);
    List<VolunteerActionDto> getVolunteerActionsBySupervisorId(Long supervisorId);
    VolunteerAction createVolunteerAction(VolunteerActionDto volunteerAction);
    VolunteerAction updateVolunteerAction(VolunteerActionDto volunteerAction);
    void deleteVolunteerActionById(Long id);
    List<ProjectCoordinatorDto> getCoordinatorsByProjectId(Long projectId);
    int getParticipantCount(Long volunteerActionId);
    boolean isSupervisorForAction(Long userId, Long volunteerActionId);
    boolean hasUserAppliedForAction(Long userId, Long volunteerActionId);
    List<VolunteerActionDto> getVolunteerActionsForCalendar(Long userId);
    void withdrawFromAction(Long userId, Long volunteerActionId);
    VolunteerActionRegistrationDto getVolunteerActionRegistrationInfo(Long adctionId);
    void registerVolunteer(Long actionId, Long userId);
    void startVolunteerAction(Long actionId);
    void finishVolunteerAction(Long actionId);
    void assignPoints(Long volunteerActionId, Long volunteerId, Integer points);
}

