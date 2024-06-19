package com.eco.environet.projects.controller;

import com.eco.environet.projects.dto.AssignmentDto;
import com.eco.environet.projects.dto.DocumentDto;
import com.eco.environet.projects.dto.TeamMemberCreationDto;
import com.eco.environet.projects.dto.TeamMemberDto;
import com.eco.environet.projects.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/projects/{projectId}/team")
@Tag(name = "Project Team Management", description = "Manage project team")
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "Fetch potential team members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched potential team members", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/available")
    @PreAuthorize("hasRole('PROJECT_MANAGER') and @projectRepository.findById(#projectId).orElse(null)?.manager?.id == authentication.principal.id")
    public ResponseEntity<List<TeamMemberDto>> fetchAvailableOrganizationMember(@PathVariable Long projectId)
    {
        var result = teamService.findAvailableMembers(projectId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Fetch team members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched team members", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping
    @PreAuthorize("hasRole('PROJECT_MANAGER') and @projectRepository.findById(#projectId).orElse(null)?.manager?.id == authentication.principal.id")
    public ResponseEntity<List<TeamMemberDto>> fetchTeamMembers(@PathVariable Long projectId)
    {
        var result = teamService.findTeamMembers(projectId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Add team member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Added team member", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping
    @PreAuthorize("hasRole('PROJECT_MANAGER') and @projectRepository.findById(#teamMemberDto.projectId).orElse(null)?.manager?.id == authentication.principal.id")
    public ResponseEntity<Void> addTeamMember(@RequestBody TeamMemberCreationDto teamMemberDto)
    {
        teamService.addTeamMember(teamMemberDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove team member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Removed team member", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "text/plain"))
    })
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('PROJECT_MANAGER')")
    public ResponseEntity<Void> removeTeamMember(@PathVariable Long projectId, @PathVariable Long userId) {
        teamService.removeTeamMember(projectId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Assign team members to documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assigned team members", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "text/plain"))
    })
    @PutMapping("/assignment")
    @PreAuthorize("hasRole('PROJECT_MANAGER') and @projectRepository.findById(#projectId).orElse(null)?.manager?.id == authentication.principal.id")
    public ResponseEntity<DocumentDto> assignTeamMember(@PathVariable Long projectId, @RequestBody AssignmentDto assignmentDto)
    {
        var result = teamService.assignTeamMembers(projectId, assignmentDto);
        return ResponseEntity.ok(result);
    }
}