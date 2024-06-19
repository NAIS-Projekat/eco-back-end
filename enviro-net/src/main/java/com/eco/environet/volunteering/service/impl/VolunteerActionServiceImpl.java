package com.eco.environet.volunteering.service.impl;
import com.eco.environet.finance.model.BudgetPlan;
import com.eco.environet.finance.model.BudgetPlanStatus;
import com.eco.environet.projects.model.Project;
import com.eco.environet.projects.model.TeamMember;
import com.eco.environet.projects.repository.TeamMemberRepository;
import com.eco.environet.users.dto.UserInfoDto;
import com.eco.environet.users.model.RegisteredUser;
import com.eco.environet.users.model.Role;
import com.eco.environet.users.model.User;
import com.eco.environet.users.repository.RegisteredUserRepository;
import com.eco.environet.users.repository.UserRepository;
import com.eco.environet.users.services.UserService;
import com.eco.environet.volunteering.dto.*;
import com.eco.environet.volunteering.mapper.VolunteeringMapper;
import com.eco.environet.volunteering.model.VolunteerAction;
import com.eco.environet.volunteering.model.VolunteerActionRegisteredUser;
import com.eco.environet.volunteering.model.VolunteerActionStatus;
import com.eco.environet.volunteering.model.compositeKeys.VolunteerActionRegisteredUserId;
import com.eco.environet.volunteering.repository.VolunteerActionRegisteredUserRepository;
import com.eco.environet.volunteering.repository.VolunteerActionRepository;
import com.eco.environet.volunteering.service.VolunteerActionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@Transactional
@Service
public class VolunteerActionServiceImpl implements VolunteerActionService {

    @Autowired
    private VolunteerActionRepository volunteerActionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamMemberRepository teamMemberRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private VolunteerActionRegisteredUserRepository volunteerActionRegisteredUserRepository;
    @Autowired
    private RegisteredUserRepository registeredUserRepository;
    @Override
    public List<VolunteerActionBasicInfoDto> getVolunteerActionsForRegisteredUser(Long userId) {
        Optional<RegisteredUser> registeredUserOptional = userRepository.findRegisteredUserById(userId);
        if (registeredUserOptional.isPresent()) {
            RegisteredUser registeredUser = registeredUserOptional.get();
            Set<VolunteerActionRegisteredUser> volunteerActionRegisteredUsers = registeredUser.getVolunteerActions();

            List<VolunteerActionBasicInfoDto> volunteerActions = volunteerActionRegisteredUsers.stream()
                    .map(VolunteerActionRegisteredUser::getVolunteerAction)
                    .map(VolunteeringMapper::mapToBasicInfoDto)
                    .collect(Collectors.toList());

            return volunteerActions;
        } else {
            return Collections.emptyList();
        }
        //return registeredUser.map(user -> new ArrayList<>(user.getVolunteerActions())).orElse(null);
    }



    @Override
    public List<RegisteredUserDto> getRegisteredUsersForVolunteerAction(Long actionId) {
        Optional<VolunteerAction> volunteerActionOptional = volunteerActionRepository.findById(actionId);
        if (volunteerActionOptional.isPresent()) {
            VolunteerAction volunteerAction = volunteerActionOptional.get();
            Set<VolunteerActionRegisteredUser> volunteerActionRegisteredUsers = volunteerAction.getRegisteredUsers();

            List<RegisteredUserDto> registeredUsers = volunteerActionRegisteredUsers.stream()
                    .map(VolunteerActionRegisteredUser::getRegisteredUser)
                    .map(VolunteeringMapper::toRegisteredUserDto)
                    .collect(Collectors.toList());

            return registeredUsers;
        } else {
            // Handle the case where the action is not found
            return Collections.emptyList();
        }
    }


    @Override
    public void signUpForVolunteerAction(Long userId, Long actionId) {
        Optional<RegisteredUser> registeredUserOptional = userRepository.findRegisteredUserById(userId);
        Optional<VolunteerAction> volunteerActionOptional = volunteerActionRepository.findById(actionId);

        if (registeredUserOptional.isEmpty() || volunteerActionOptional.isEmpty()) {
            return;
        }

        RegisteredUser registeredUser = registeredUserOptional.get();
        VolunteerAction volunteerAction = volunteerActionOptional.get();

        VolunteerActionRegisteredUserId id = new VolunteerActionRegisteredUserId(volunteerAction.getId(), registeredUser.getId());
        VolunteerActionRegisteredUser volunteerActionRegisteredUser = new VolunteerActionRegisteredUser();
        volunteerActionRegisteredUser.setId(id);
        volunteerActionRegisteredUser.setVolunteerAction(volunteerAction);
        volunteerActionRegisteredUser.setRegisteredUser(registeredUser);
        volunteerActionRegisteredUser.setAppeared(false); // Assuming default value

        volunteerActionRegisteredUserRepository.save(volunteerActionRegisteredUser);
    }

    /*@Override
    public void signUpForVolunteerAction(Long userId, Long actionId) {

        Optional<RegisteredUser> registeredUser = userRepository.findRegisteredUserById(userId);
        Optional<VolunteerAction> volunteerAction = volunteerActionRepository.findById(actionId);
        if (registeredUser.isEmpty() || volunteerAction.isEmpty()) {
            return;
        }
        RegisteredUser user = registeredUser.get();
        VolunteerAction action = volunteerAction.get();

        user.getVolunteerActions().add(action);
        action.getRegisteredUsers().add(user);

        userRepository.save(user);
        volunteerActionRepository.save(action);
    }*/

    @Override
    public List<VolunteerAction> getAllVolunteerActions() {
        return volunteerActionRepository.findAll();
    }

    @Override
    public Optional<VolunteerAction> getVolunteerActionById(Long id) {
        return volunteerActionRepository.findById(id);
    }

    @Override
    public List<VolunteerAction> getVolunteerActionsByProjectId(Long projectId) {
        return volunteerActionRepository.findByProjectId(projectId);
    }

    @Override
    public List<VolunteerActionBasicInfoDto> getVolunteerActionsByCreatorId(Long creatorId) {
        List<VolunteerAction> volunteerActions = volunteerActionRepository.findByCreatorId(creatorId);
        List<VolunteerActionBasicInfoDto> basicInfoList = volunteerActions.stream()
                .map(VolunteeringMapper::mapToBasicInfoDto)
                .collect(Collectors.toList());

        return basicInfoList;
    }

    @Override
    public List<VolunteerActionDto> getVolunteerActionsBySupervisorId(Long supervisorId) {
        List<VolunteerActionStatus> statuses = Arrays.asList(
                VolunteerActionStatus.UPCOMING,
                VolunteerActionStatus.ONGOING,
                VolunteerActionStatus.FINISHED
        );

        List<VolunteerAction> volunteerActions = volunteerActionRepository.findBySupervisorId(supervisorId);

        List<VolunteerAction> filteredActions = volunteerActions.stream()
                .filter(action -> statuses.contains(action.getStatus()))
                .collect(Collectors.toList());

        filteredActions.sort(Comparator.comparing(VolunteerAction::getDate));

        return filteredActions.stream()
                .map(VolunteeringMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public VolunteerAction createVolunteerAction(VolunteerActionDto newVolunteerAction) {
        Project project = null;
        if (newVolunteerAction.getProjectId() != null) {
            project = new Project();
            project.setId(newVolunteerAction.getProjectId());
        }
        else{
            newVolunteerAction.setProjectDependency(false);
        }
        User creator = new User();
        creator.setId(newVolunteerAction.getCreatorId());
        creator.setRole(Role.PROJECT_COORDINATOR);
        User supervisor = new User();
        supervisor.setRole(Role.PROJECT_COORDINATOR);
        supervisor.setId(newVolunteerAction.getSupervisorId());
        VolunteerAction volunteerAction = VolunteerAction.builder()
                .title(newVolunteerAction.getTitle())
                .description(newVolunteerAction.getDescription())
                .status(newVolunteerAction.getStatus())
                .participantCount(newVolunteerAction.getParticipantCount())
                .difficulty(newVolunteerAction.getDifficulty())
                .date(newVolunteerAction.getDate())
                .durationHours(newVolunteerAction.getDurationHours())
                .type(newVolunteerAction.getType())
                .budget(newVolunteerAction.getBudget())
                .project(project)
                .creator(creator)
                .supervisor(supervisor)
                .applicationLimit(newVolunteerAction.isApplicationLimit())
                .projectDependency(newVolunteerAction.isProjectDependency())
                .build();
        return volunteerActionRepository.save(volunteerAction);
    }

    @Override
    public VolunteerAction updateVolunteerAction(VolunteerActionDto newVolunteerAction) {
        Project project = new Project();
        project.setId(newVolunteerAction.getProjectId());
        User creator = new User();
        creator.setId(newVolunteerAction.getCreatorId());
        creator.setRole(Role.PROJECT_COORDINATOR);
        User supervisor = new User();
        supervisor.setRole(Role.PROJECT_COORDINATOR);
        supervisor.setId(newVolunteerAction.getSupervisorId());
        VolunteerAction volunteerAction = VolunteerAction.builder()
                .id(newVolunteerAction.getId())
                .title(newVolunteerAction.getTitle())
                .description(newVolunteerAction.getDescription())
                .status(newVolunteerAction.getStatus())
                .participantCount(newVolunteerAction.getParticipantCount())
                .difficulty(newVolunteerAction.getDifficulty())
                .date(newVolunteerAction.getDate())
                .durationHours(newVolunteerAction.getDurationHours())
                .type(newVolunteerAction.getType())
                .budget(newVolunteerAction.getBudget())
                .project(project)
                .creator(creator)
                .supervisor(supervisor)
                .applicationLimit(newVolunteerAction.isApplicationLimit())
                .projectDependency(newVolunteerAction.isProjectDependency())
                .build();
        return volunteerActionRepository.save(volunteerAction);
    }

    @Override
    public void deleteVolunteerActionById(Long id) {
        Optional<VolunteerAction> optionalAction = volunteerActionRepository.findById(id);
        if (optionalAction.isPresent()) {
            VolunteerAction action = optionalAction.get();
            action.setStatus(VolunteerActionStatus.DELETED);
            volunteerActionRepository.save(action);
        } else {
            throw new EntityNotFoundException("Volunteer action not found with id: " + id);
        }
    }

    public List<ProjectCoordinatorDto> getCoordinatorsByProjectId(Long projectId) {
        List<TeamMember> teamMembers = teamMemberRepository.findByProjectId(projectId);
        return teamMembers.stream()
                .map(teamMember -> {
                    UserInfoDto userInfo = userService.findUser(teamMember.getUserId());
                    return convertToProjectCoordinatorDto(userInfo);
                })
                .collect(Collectors.toList());
    }

    @Override
    public int getParticipantCount(Long volunteerActionId) {
        return volunteerActionRegisteredUserRepository.countByVolunteerActionId(volunteerActionId);
    }

    @Override
    public boolean isSupervisorForAction(Long userId, Long volunteerActionId) {
        Optional<VolunteerAction> optionalVolunteerAction = volunteerActionRepository.findById(volunteerActionId);
        if (optionalVolunteerAction.isPresent()) {
            VolunteerAction action = optionalVolunteerAction.get();

            // Check if the action has a supervisor
            if (action.getSupervisor() != null) {
                // Compare supervisor's ID with the given user's ID
                return action.getSupervisor().getId().equals(userId);
            }
        }
        return false;
    }

    @Override
    public boolean hasUserAppliedForAction(Long userId, Long volunteerActionId) {
        return volunteerActionRegisteredUserRepository.existsByUserIdAndVolunteerActionId(userId, volunteerActionId);
    }

    @Override
    public List<VolunteerActionDto> getVolunteerActionsForCalendar(Long userId) {
        // Fetch upcoming volunteer actions
        List<VolunteerAction> upcomingActions = volunteerActionRepository.findUpcomingActions();

        // Fetch past volunteer actions that the user participated in
        List<VolunteerAction> pastActions = volunteerActionRegisteredUserRepository.findPastActionsByUserId(userId);

        // Merge upcoming and past actions
        List<VolunteerAction> allActions = new ArrayList<>();
        allActions.addAll(upcomingActions);
        allActions.addAll(pastActions);

        // Filter out actions that are not UPCOMING or FINISHED
        allActions = allActions.stream()
                .filter(action -> action.getStatus() == VolunteerActionStatus.UPCOMING || action.getStatus() == VolunteerActionStatus.FINISHED)
                .collect(Collectors.toList());

        // Sort actions by date
        allActions.sort(Comparator.comparing(VolunteerAction::getDate));

        // Convert to VolunteerActionDto
        List<VolunteerActionDto> result = allActions.stream()
                .map(VolunteeringMapper::mapToDto)
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public void withdrawFromAction(Long userId, Long volunteerActionId) {
        volunteerActionRegisteredUserRepository.deleteByUserIdAndVolunteerActionId(userId, volunteerActionId);
    }

    @Override
    public VolunteerActionRegistrationDto getVolunteerActionRegistrationInfo(Long actionId) {
        VolunteerAction volunteerAction = volunteerActionRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("Volunteer action not found"));

        // Fetch the list of volunteers who have signed up and those who have appeared
        List<VolunteerActionRegisteredUser> signedUpVolunteers = volunteerActionRegisteredUserRepository.findByVolunteerActionId(actionId);
        List<VolunteerActionRegisteredUser> appearedVolunteers = volunteerActionRegisteredUserRepository.findByVolunteerActionIdAndAppearedTrue(actionId);

        // Map the entities to DTOs
        List<RegisteredUserDto> signedUpVolunteersDto = signedUpVolunteers.stream()
                .map(registeredUser -> new RegisteredUserDto(
                        registeredUser.getRegisteredUser().getId(),
                        registeredUser.getRegisteredUser().getEmail(),
                        registeredUser.getRegisteredUser().getUsername(),
                        registeredUser.getRegisteredUser().getName(),
                        registeredUser.getRegisteredUser().getSurname(),
                        registeredUser.getRegisteredUser().getPhoneNumber(),
                        registeredUser.getRegisteredUser().getPoints()
                ))
                .collect(Collectors.toList());

        List<RegisteredUserDto> appearedVolunteersDto = appearedVolunteers.stream()
                .map(registeredUser -> {
                    RegisteredUserDto dto = new RegisteredUserDto(
                            registeredUser.getRegisteredUser().getId(),
                            registeredUser.getRegisteredUser().getEmail(),
                            registeredUser.getRegisteredUser().getUsername(),
                            registeredUser.getRegisteredUser().getName(),
                            registeredUser.getRegisteredUser().getSurname(),
                            registeredUser.getRegisteredUser().getPhoneNumber(),
                            registeredUser.getRegisteredUser().getPoints()
                    );

                    // Fetch points earned during the specific volunteer action for the current registered user
                    Integer pointsEarned = volunteerActionRegisteredUserRepository.findByVolunteerActionIdAndUserId(actionId, registeredUser.getRegisteredUser().getId()).getPoints();
                    if(pointsEarned == null){
                        pointsEarned = 0;
                    }
                    // Set the points earned during this volunteer action in the DTO
                    dto.setPoints(pointsEarned);

                    return dto;
                })
                .collect(Collectors.toList());


        // Calculate totals
        int totalSignedUp = signedUpVolunteers.size();
        int totalAppeared = appearedVolunteers.size();

        // Map VolunteerAction to VolunteerActionDto using VolunteeringMapper (assuming this method exists)
        VolunteerActionDto volunteerActionDto = VolunteeringMapper.mapToDto(volunteerAction);

        // Create and return the DTO
        return new VolunteerActionRegistrationDto(
                volunteerActionDto,
                signedUpVolunteersDto,
                appearedVolunteersDto,
                totalSignedUp,
                totalAppeared
        );
    }

    @Override
    public void registerVolunteer(Long actionId, Long userId) {
        VolunteerActionRegisteredUser registeredUser = new VolunteerActionRegisteredUser();
        registeredUser.setVolunteerActionId(actionId);
        registeredUser.setUserId(userId);
        registeredUser.setAppeared(true); // Set appeared to true as they are registering as appeared

        volunteerActionRegisteredUserRepository.save(registeredUser);
    }

    @Override
    public void startVolunteerAction(Long actionId) {
        VolunteerAction volunteerAction = volunteerActionRepository.findById(actionId).orElseThrow(() -> new EntityNotFoundException("VolunteerAction not found"));
        volunteerAction.setStatus(VolunteerActionStatus.ONGOING);
        volunteerAction.setStartTime(Timestamp.valueOf(LocalDateTime.now()));
        volunteerActionRepository.save(volunteerAction);
    }

    @Override
    public void finishVolunteerAction(Long actionId) {
        VolunteerAction volunteerAction = volunteerActionRepository.findById(actionId).orElseThrow(() -> new EntityNotFoundException("VolunteerAction not found"));
        volunteerAction.setStatus(VolunteerActionStatus.FINISHED);
        volunteerAction.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
        volunteerActionRepository.save(volunteerAction);

        List<VolunteerActionRegisteredUser> absentUsers = volunteerActionRegisteredUserRepository.findByVolunteerActionIdAndAppearedFalse(actionId);
        for (VolunteerActionRegisteredUser user : absentUsers) {
            int newPoints = -10; // Ensure points cannot be negative
            user.setPoints(newPoints);
            volunteerActionRegisteredUserRepository.save(user);

            // Update total points in the registered user table
            RegisteredUser registeredUser = user.getRegisteredUser();
            int totalPoints = registeredUser.getPoints();
            int updatedTotalPoints = Math.max(0, totalPoints - 10); // Ensure total points cannot be negative
            registeredUser.setPoints(updatedTotalPoints);
            registeredUserRepository.save(registeredUser);
        }
        List<VolunteerActionRegisteredUser> usersWithPoints = volunteerActionRegisteredUserRepository.findByVolunteerActionIdAndPointsGreaterThanZero(actionId);


        // Add points to their total points
        for (VolunteerActionRegisteredUser user : usersWithPoints) {
            RegisteredUser registeredUser = user.getRegisteredUser();
            registeredUser.setPoints(registeredUser.getPoints() + user.getPoints());
            registeredUserRepository.save(registeredUser);
        }
    }

    @Override
    public void assignPoints(Long volunteerActionId, Long volunteerId, Integer points) {
        VolunteerActionRegisteredUser registeredUser = volunteerActionRegisteredUserRepository.findByVolunteerActionIdAndUserId(volunteerActionId, volunteerId);
        registeredUser.setPoints((registeredUser.getPoints() != null ? registeredUser.getPoints() : 0) + points);
        volunteerActionRegisteredUserRepository.save(registeredUser);
    }

    private ProjectCoordinatorDto convertToProjectCoordinatorDto(UserInfoDto userInfo) {
        ProjectCoordinatorDto coordinatorDto = new ProjectCoordinatorDto();
        coordinatorDto.setId(userInfo.getId());
        coordinatorDto.setUsername(userInfo.getUsername());
        coordinatorDto.setName(userInfo.getName());
        coordinatorDto.setSurname(userInfo.getSurname());
        coordinatorDto.setEmail(userInfo.getEmail());
        coordinatorDto.setPhoneNumber(userInfo.getPhoneNumber());
        // Map other fields as needed
        return coordinatorDto;
    }

}
