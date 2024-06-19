package com.eco.environet.finance.controller;

import com.eco.environet.finance.dto.OrganizationGoalDto;
import com.eco.environet.finance.dto.OrganizationGoalsSetDto;
import com.eco.environet.finance.services.OrganizationGoalService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goal")
@RequiredArgsConstructor
@Tag(name = "Organization Goals", description = "Board Members manage organization goals")
public class OrganizationGoalController {
    private final OrganizationGoalService service;

    @Operation(summary = "Create new organization goal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New organization goal created!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrganizationGoalDto.class)) }),
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
    @PostMapping(value = "/create", consumes = "application/json")
    @PreAuthorize("hasRole('BOARD_MEMBER')")
    public ResponseEntity<OrganizationGoalDto> createNewGoal(@RequestBody OrganizationGoalDto newGoalDto) {
        var result = service.create(newGoalDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Get all organization goals")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all organization goals!",
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
    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('BOARD_MEMBER')")
    public ResponseEntity<Page<OrganizationGoalsSetDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "period", required = false) String period,
            @RequestParam(name = "statuses", required = false) List<String> statuses,
            @RequestParam(name = "creators", required = false) List<Long> creators,
            @RequestParam(name = "sort", required = false, defaultValue = "validPeriod.startDate") String sortField,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String sortDirection
    ) {
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        var result = service.findAll(title, period, statuses, creators, pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Get current organization goals")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched current organization goals set!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrganizationGoalsSetDto.class)) }),
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
    @GetMapping(value = "/current")
    public ResponseEntity<OrganizationGoalsSetDto> getAllCurrent() {
        var result = service.findCurrent();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Get organization goal by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched organization goal!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrganizationGoalDto.class)) }),
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
    @GetMapping(value = "/get/{id}")
    @PreAuthorize("hasRole('BOARD_MEMBER')")
    public ResponseEntity<OrganizationGoalDto> getGoal(@PathVariable Long id) {
        var result = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Update organization goal by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated organization goal!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrganizationGoalDto.class)) }),
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
    @PutMapping(value = "/update")
    @PreAuthorize("hasRole('BOARD_MEMBER')")
    public ResponseEntity<OrganizationGoalDto> update(@RequestBody OrganizationGoalDto newGoalDto) {
        var result = service.update(newGoalDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Delete organization goal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted organization goal!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrganizationGoalDto.class)) }),
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
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('BOARD_MEMBER')")
    public ResponseEntity<OrganizationGoalDto> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Publish new valid organization goal set")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Published new valid organization goal set!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrganizationGoalsSetDto.class)) }),
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
    @PutMapping(value = "/publish")
    @PreAuthorize("hasRole('BOARD_MEMBER')")
    public ResponseEntity<OrganizationGoalsSetDto> publish(@RequestBody OrganizationGoalsSetDto newGoalsSetDto) {
        var result = service.publish(newGoalsSetDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
