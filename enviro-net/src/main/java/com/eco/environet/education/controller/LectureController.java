package com.eco.environet.education.controller;

import com.eco.environet.education.dto.LectureCategoryDto;
import com.eco.environet.education.dto.LectureCreationRequest;
import com.eco.environet.education.dto.LectureDto;
import com.eco.environet.education.services.LectureCategoryService;
import com.eco.environet.education.services.LectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lecture")
@RequiredArgsConstructor
@Tag(name = "Lecture", description = "Manage lectures")
public class LectureController {

    private final LectureService service;
    private final LectureCategoryService lectureCategoryService;

    @Operation(summary = "Create new lecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LectureDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                content = @Content)
    })
    @PostMapping(consumes = "application/json")
    @PreAuthorize("hasRole('EDUCATOR')")
    public ResponseEntity<LectureDto> create(
            @RequestBody LectureCreationRequest lecture
    ) {
        var result = service.create(lecture);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Fetch all lectures created by educator with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LectureDto.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                content = @Content)
    })
    @GetMapping()
    @PreAuthorize("hasRole('EDUCATOR')")
    public ResponseEntity<List<LectureDto>> findAllByCreatorId(@RequestParam Long creatorId) {
        var result = service.findAllByCreatorId(creatorId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Fetch all lectures")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LectureDto.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PreAuthorize("hasRole('REGISTERED_USER')")
    @GetMapping("/all")
    public ResponseEntity<List<LectureDto>> findAll() {
        var result = service.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Fetch lecture by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LectureDto.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PreAuthorize("hasAnyRole('REGISTERED_USER', 'EDUCATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<LectureDto> findById(@PathVariable Long id) {
        var result = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Fetch all lecture categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LectureCategoryDto.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PreAuthorize("hasAnyRole('REGISTERED_USER', 'EDUCATOR')")
    @GetMapping("/categories/all")
    public ResponseEntity<List<LectureCategoryDto>> findAllCategories() {
        var result = lectureCategoryService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Delete lecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PreAuthorize("hasAnyRole('EDUCATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
