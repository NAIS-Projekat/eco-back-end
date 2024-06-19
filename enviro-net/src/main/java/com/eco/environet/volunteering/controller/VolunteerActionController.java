package com.eco.environet.volunteering.controller;

import com.eco.environet.users.dto.UserInfoDto;
import com.eco.environet.users.model.RegisteredUser;
import com.eco.environet.users.model.Role;
import com.eco.environet.users.model.User;
import com.eco.environet.users.services.UserService;
import com.eco.environet.volunteering.dto.*;
import com.eco.environet.volunteering.mapper.VolunteeringMapper;
import com.eco.environet.volunteering.model.VolunteerAction;
import com.eco.environet.volunteering.service.PDFService;
import com.eco.environet.volunteering.service.VolunteerActionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/volunteer-actions")
@Tag(name = "Volunteer action", description = "Overview volunteer actions")
public class VolunteerActionController {

    @Autowired
    private VolunteerActionService volunteerActionService;
    @Autowired
    private UserService userService;
    @Autowired
    private PDFService pdfService;
    //GET VOLUNTEER ACTIONS ON WHICH USER SIGNED UP
    @Operation(summary = "Fetch volunteer actions for given volunteer id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisteredUser.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)
    })
    @PreAuthorize("hasRole('REGISTERED_USER')")
    @GetMapping("/volunteer-actions/{userId}")
    public ResponseEntity<List<VolunteerActionBasicInfoDto>>  getVolunteerActionsForRegisteredUser(@PathVariable Long userId) {
        var volunteerActions = volunteerActionService.getVolunteerActionsForRegisteredUser(userId);
        if(volunteerActions==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(volunteerActions);
        }
    }

    //GET SINGED UP USERS
    @Operation(summary = "Fetch signed up users for given volunteer action id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisteredUser.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)
    })
    @PreAuthorize("hasRole('PROJECT_COORDINATOR')")
    @GetMapping("/registered-users/{actionId}")
    public ResponseEntity<List<RegisteredUserDto>> getRegisteredUsersForVolunteerAction(@PathVariable Long actionId) {
        var registeredUsers = volunteerActionService.getRegisteredUsersForVolunteerAction(actionId);
        if(registeredUsers==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(registeredUsers);
        }
    }

    //SIGN UP FOR VOLUNTEER ACTION
    @Operation(summary = "Signing up for the volunteer action")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PreAuthorize("hasRole('REGISTERED_USER')")
    @PostMapping("signup/{actionId}/{userId}")
    public ResponseEntity<Void> signUpForVolunteerAction(@PathVariable Long actionId, @PathVariable Long userId) {
        volunteerActionService.signUpForVolunteerAction(userId, actionId);
        return ResponseEntity.ok().build();
    }

    //GET ALL VOLUNTEER ACTIONS
    @Operation(summary = "Fetch all volunteer actions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VolunteerAction.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PreAuthorize("hasRole('PROJECT_COORDINATOR')")
    @GetMapping
    public ResponseEntity<List<VolunteerAction>> getAllVolunteerActions() {
        var volunteerActions = volunteerActionService.getAllVolunteerActions();
        return ResponseEntity.status(HttpStatus.OK).body(volunteerActions);
    }

    //GET VOLUNTEER ACTION BY ID
    @Operation(summary = "Fetch volunteer action by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VolunteerAction.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PreAuthorize("hasRole('REGISTERED_USER') or hasRole('PROJECT_COORDINATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<VolunteerActionDto> getVolunteerActionById(@PathVariable Long id) {
        return volunteerActionService.getVolunteerActionById(id)
                .map(volunteerAction -> {
                    VolunteerActionDto volunteerActionDto = VolunteeringMapper.mapToDto(volunteerAction);
                    return new ResponseEntity<>(volunteerActionDto, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //GET VOLUNTEER ACTIONS BY PROJECT ID
    @Operation(summary = "Fetch volunteer actions by project id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VolunteerAction.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PreAuthorize("hasRole('PROJECT_COORDINATOR')")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<VolunteerAction>> getByProjectId(@PathVariable Long projectId) {
        var volunteerActions = volunteerActionService.getVolunteerActionsByProjectId(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(volunteerActions);
    }


    //GET VOLUNTEER ACTIONS BY CREATOR ID
    @Operation(summary = "Fetch volunteer actions by creator id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VolunteerAction.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PreAuthorize("hasRole('PROJECT_COORDINATOR')")
    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<VolunteerActionBasicInfoDto>> getByCreatorId(@PathVariable Long creatorId) {
        var volunteerActions = volunteerActionService.getVolunteerActionsByCreatorId(creatorId);
        return ResponseEntity.status(HttpStatus.OK).body(volunteerActions);
    }

//    //GET VOLUNTEER ACTIONS BY SUPERVISOR ID
//    @Operation(summary = "Fetch volunteer actions by supervisor id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "OK",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = VolunteerAction.class))}),
//            @ApiResponse(responseCode = "404", description = "Not Found",
//                    content = @Content)
//    })
//    @PreAuthorize("hasRole('PROJECT_COORDINATOR')")
//    @GetMapping("/supervisor/{supervisorId}")
//    public ResponseEntity<List<VolunteerActionDto>> getBySupervisorId(@PathVariable Long supervisorId) {
//        var volunteerActions = volunteerActionService.getVolunteerActionsBySupervisorId(supervisorId);
//        return ResponseEntity.status(HttpStatus.OK).body(volunteerActions);
//    }

    //CREATE VOLUNTEER ACTION
    @Operation(summary = "Create new volunteer action")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VolunteerAction.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content)
    })
    @PostMapping(consumes = "application/json")
    @PreAuthorize("hasRole('PROJECT_COORDINATOR')")
    public ResponseEntity<VolunteerAction> create(@RequestBody VolunteerActionDto volunteerAction) {
        var result = volunteerActionService.createVolunteerAction(volunteerAction);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    //UPDATE VOLUNTEER ACTION
    @Operation(summary = "Update volunteer action")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VolunteerActionDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content)
    })
    @PutMapping(consumes = "application/json")
    @PreAuthorize("hasRole('PROJECT_COORDINATOR')")
    public ResponseEntity<VolunteerAction> update(@RequestBody VolunteerActionDto volunteerAction) {
        var result = volunteerActionService.updateVolunteerAction(volunteerAction);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    //DELETE VOLUNTEER ACTION
    @Operation(summary = "Delete volunteer action")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PreAuthorize("hasAnyRole('PROJECT_COORDINATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        volunteerActionService.deleteVolunteerActionById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Fetch coordinators for a given project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched coordinators", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/{projectId}/coordinators")
    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('PROJECT_COORDINATOR')")
    public ResponseEntity<List<ProjectCoordinatorDto>> getCoordinatorsByProjectId(@PathVariable Long projectId) {
        List<ProjectCoordinatorDto> coordinators = volunteerActionService.getCoordinatorsByProjectId(projectId);
        return ResponseEntity.ok(coordinators);
    }

    @Operation(summary = "Fetch number of signed up volunteers for a given action id")
    @GetMapping("/participant-count/{volunteerActionId}/{userId}")
    @PreAuthorize("hasRole('REGISTERED_USER') or hasRole('PROJECT_COORDINATOR')")
    public VolunteerActionDetailsDto getParticipantCount(@PathVariable Long userId, @PathVariable Long volunteerActionId) {
        VolunteerActionDetailsDto volunteerActionDetailsDto = new VolunteerActionDetailsDto();
        volunteerActionDetailsDto.setParticipantCount(volunteerActionService.getParticipantCount(volunteerActionId));
        UserInfoDto user = userService.findUser(userId);
        if(user.getRole().equals("Project Coordinator")){
            volunteerActionDetailsDto.setHasApplied(false);
            boolean isSupervisor = volunteerActionService.isSupervisorForAction(userId, volunteerActionId);
            volunteerActionDetailsDto.setIsSupervisor(isSupervisor);
        }
        else if(user.getRole().equals("Registered User")){
            volunteerActionDetailsDto.setIsSupervisor(false);
            boolean hasApplied = volunteerActionService.hasUserAppliedForAction(userId, volunteerActionId);
            volunteerActionDetailsDto.setHasApplied(hasApplied);
        }
        return volunteerActionDetailsDto;
    }
    @Operation(summary = "Fetch volunteer actions for volunteer calendar view")
    @GetMapping("/calendar/{userId}")
    @PreAuthorize("hasRole('REGISTERED_USER') or hasRole('PROJECT_COORDINATOR')")
    public ResponseEntity<List<VolunteerActionDto>> getVolunteerActionsForCalendar(@PathVariable Long userId) {
        List<VolunteerActionDto> actions = volunteerActionService.getVolunteerActionsForCalendar(userId);
        return ResponseEntity.ok(actions);
    }

    @Operation(summary = "Fetch volunteer actions for coordinator calendar view")
    @GetMapping("/coordinator/calendar/{userId}")
    @PreAuthorize("hasRole('REGISTERED_USER') or hasRole('PROJECT_COORDINATOR')")
    public ResponseEntity<List<VolunteerActionDto>> getVolunteerActionsForCoordinatorCalendar(@PathVariable Long userId) {
        List<VolunteerActionDto> actions = volunteerActionService.getVolunteerActionsBySupervisorId(userId);
        return ResponseEntity.ok(actions);
    }

    @Operation(summary = "Withdraw from volunteer action")
    @DeleteMapping("/{volunteerActionId}/withdraw/{userId}")
    @PreAuthorize("hasRole('REGISTERED_USER')")
    public ResponseEntity<?> withdrawFromAction(@PathVariable Long volunteerActionId, @PathVariable Long userId) {
        volunteerActionService.withdrawFromAction(userId, volunteerActionId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Get Dto for registration opening")
    @GetMapping("/open-registrations/{id}")
    @PreAuthorize("hasRole('PROJECT_COORDINATOR')")
    public ResponseEntity<VolunteerActionRegistrationDto> getVolunteerActionDetails(@PathVariable Long id) {
        VolunteerActionRegistrationDto details = volunteerActionService.getVolunteerActionRegistrationInfo(id);
        return ResponseEntity.ok(details);
    }

    @Operation(summary = "Register volunteer who appeared")
    @PostMapping("/{actionId}/register/{userId}")
    @PreAuthorize("hasRole('PROJECT_COORDINATOR')")
    public ResponseEntity<Void> registerVolunteer(@PathVariable Long actionId, @PathVariable Long userId) {
        volunteerActionService.registerVolunteer(actionId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Start volunteer action")
    @PostMapping("/start/{actionId}")
    @PreAuthorize("hasRole('PROJECT_COORDINATOR')")
    public ResponseEntity<Void> startVolunteerAction(@PathVariable Long actionId) {
        volunteerActionService.startVolunteerAction(actionId);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Finish volunteer action")
    @PostMapping("/finish/{actionId}")
    @PreAuthorize("hasRole('PROJECT_COORDINATOR')")
    public ResponseEntity<Void> finishVolunteerAction(@PathVariable Long actionId) {
        volunteerActionService.finishVolunteerAction(actionId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Assign points to a user")
    @PostMapping("/assign-points/{actionId}")
    @PreAuthorize("hasRole('PROJECT_COORDINATOR')")
    public ResponseEntity<Void> assignPoints(@PathVariable Long actionId, @RequestBody AssignPointsRequestDto request) {
        volunteerActionService.assignPoints(actionId, request.getVolunteerId(), request.getAddedPoints());
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Generate PDF statistics")
    @GetMapping("/report/{actionId}")
    @PreAuthorize("hasRole('PROJECT_COORDINATOR')")
    public ResponseEntity<?> generateVolunteerActionReport(@PathVariable Long actionId) {
        return pdfService.generateVolunteerActionReport(actionId);
    }

}
