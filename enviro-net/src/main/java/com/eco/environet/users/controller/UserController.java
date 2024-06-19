package com.eco.environet.users.controller;

import com.eco.environet.users.dto.UserInfoDto;
import com.eco.environet.users.mapper.UserToProjectCoordinatorMapper;
import com.eco.environet.users.model.Role;
import com.eco.environet.volunteering.dto.ProjectCoordinatorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.eco.environet.users.dto.UserDto;
import com.eco.environet.users.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Manage registered users and organization members")
public class UserController {

    private final UserService service;

    @Operation(summary = "Get user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched user",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserInfoDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @GetMapping(value="/get-user/{id}")
    public ResponseEntity<UserInfoDto> getUser(@PathVariable Long id) {
        var result = service.findUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all organization members",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @PutMapping(value="/update-user/{id}")
    public ResponseEntity<UserInfoDto> updateUser(@PathVariable Long id, @RequestBody UserInfoDto userInfoDto) {
        // Fetch the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        var result = service.updateUser(userInfoDto);

        // Check if the authenticated user matches the requested user
        if (!result.getUsername().equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Update user email by clicking on confirmation link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all organization members",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content)})
    @PutMapping(value="/update-email/{email}")
    public ResponseEntity<UserInfoDto> updateEmail(@PathVariable String email, @RequestParam("token") String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        var result = service.updateUserEmail(currentUsername, email, token);

        // Check if the authenticated user matches the requested user
        if (!result.getUsername().equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Get all organization members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all organization members",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @GetMapping(value="/members")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'BOARD_MEMBER', 'ACCOUNTANT')")
    public ResponseEntity<Page<UserDto>> getAllOrganizationMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "surname", required = false) String surname,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "sort", required = false, defaultValue = "surname") String sortField,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String sortDirection
    ) {
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        var result = service.findAllOrganizationMembers(name, surname, email, pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Get all users with roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all users with roles",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto[].class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @GetMapping(value="/user-roles")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'BOARD_MEMBER', 'ACCOUNTANT')")
    public ResponseEntity<List<UserDto>> getAllUsersByRoles(@RequestParam(name = "roles", required = false) List<String> roles) {
        var result = service.findAllUsersByRoles(roles);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Get all project coordinators")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all users with roles",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto[].class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @GetMapping(value="/project-coordinators")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'BOARD_MEMBER', 'ACCOUNTANT', 'PROJECT_COORDINATOR')")
    public ResponseEntity<List<ProjectCoordinatorDto>> getAllUsersByRoles() {
        var result = service.findAllUsersByRoles(Arrays.asList("PROJECT_COORDINATOR"));

        List<ProjectCoordinatorDto> projectCoordinators = result.stream()
                .map(UserToProjectCoordinatorMapper::mapToProjectCoordinatorDto)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(projectCoordinators);
    }

    @Operation(summary = "Remove an organization member by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Member removed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/members/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> removeOrganizationMember(@PathVariable Long id) {
        service.removeOrganizationMember(id);
        return ResponseEntity.noContent().build();
    }
}
