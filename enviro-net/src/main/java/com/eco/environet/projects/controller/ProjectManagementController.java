package com.eco.environet.projects.controller;

import com.eco.environet.projects.dto.ProjectCreationDto;
import com.eco.environet.projects.dto.ProjectDto;
import com.eco.environet.projects.dto.ProjectUpdateDto;
import com.eco.environet.projects.service.ProjectManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/projects")
@Tag(name = "Project Creation", description = "Manage Projects")
public class ProjectManagementController {

    private final ProjectManagementService projectManagementService;

    @Operation(summary = "Create project with basic info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created project with basic info", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping
    @PreAuthorize("hasRole('PROJECT_MANAGER') and #projectDto.managerId == authentication.principal.id")
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody ProjectCreationDto projectDto) {
        ProjectDto result = projectManagementService.create(projectDto);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Fetch project with basic info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created project with basic info", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping("/{projectId}")
    @PreAuthorize("hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ProjectDto> getProject(@PathVariable Long projectId) {
        ProjectDto result = projectManagementService.get(projectId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Update project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated project", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "text/plain"))
    })
    @PutMapping("/{projectId}")
    @PreAuthorize("hasRole('PROJECT_MANAGER') and @projectRepository.findById(#projectId).orElse(null)?.manager?.id == authentication.principal.id")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long projectId, @Valid @RequestBody ProjectUpdateDto updateDto) {
        ProjectDto result = projectManagementService.update(projectId, updateDto);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Delete project draft or archive ongoing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project deleted or archived", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "text/plain"))
    })
    @DeleteMapping("/{projectId}")
    @PreAuthorize("hasRole('PROJECT_MANAGER') and @projectRepository.findById(#projectId).orElse(null)?.manager?.id == authentication.principal.id")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectManagementService.delete(projectId);
        return ResponseEntity.ok().build();
    }
}
