package com.eco.environet.projects.controller;

import com.eco.environet.projects.dto.*;
import com.eco.environet.projects.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/projects")
@Tag(name = "Project Overview", description = "Overview projects and its documentation")
public class ProjectOverviewController {

    private final ProjectService projectService;

    @Operation(summary = "Fetch projects with basic info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched projects with basic info", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping
    @PreAuthorize("hasRole('PROJECT_MANAGER')")
    public ResponseEntity<Page<ProjectDto>> fetchProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "sort", required = false, defaultValue = "surname") String sortField,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String sortDirection
    ) {
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        var result = projectService.findAllProjects(name, pageRequest);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Fetch project with basic info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched project with basic info", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/{projectId}")
    @PreAuthorize("hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ProjectDto> fetchProject(@PathVariable Long projectId) {

        var result = projectService.getProject(projectId);
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "Fetch documents by project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched documents by project", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/{projectId}/documents")
    @PreAuthorize("hasRole('PROJECT_MANAGER')")
    public ResponseEntity<List<DocumentDto>> getDocumentsByProject(@PathVariable Long projectId) {
        List<DocumentDto> documents = projectService.getDocuments(projectId);
        return ResponseEntity.ok(documents);
    }

    @Operation(summary = "Fetch projects by project coordinator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched projects with basic info", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/coordinator/{coordinatorId}")
    @PreAuthorize("hasRole('PROJECT_COORDINATOR')")
    public ResponseEntity<List<ProjectDto>> fetchProjectsByCoordinator(
            @PathVariable Long coordinatorId
    ) {

        List<ProjectDto> projects = projectService.findProjectsByMemberId(coordinatorId);
        return ResponseEntity.ok(projects);
    }

}
