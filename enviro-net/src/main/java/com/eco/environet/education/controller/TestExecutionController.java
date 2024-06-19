package com.eco.environet.education.controller;

import com.eco.environet.education.dto.TestCompletionRequest;
import com.eco.environet.education.dto.TestCompletionResponse;
import com.eco.environet.education.dto.TestExecutionDto;
import com.eco.environet.education.services.TestExecutionService;
import com.eco.environet.users.model.User;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/test-execution")
@Tag(name = "Test", description = "Manage test execution")
public class TestExecutionController {

    private final TestExecutionService service;

    @Operation(summary = "Start new test execution")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TestExecutionDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content)
    })
    @PostMapping("/start/{lectureId}")
    @PreAuthorize("hasRole('REGISTERED_USER')")
    public ResponseEntity<TestExecutionDto> start(
            @PathVariable Long lectureId
    ) {
        UserDetails userDetails =  (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var result = service.create(lectureId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Complete the test and get results")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TestCompletionResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @PutMapping(value = "/complete", consumes = "application/json")
    @PreAuthorize("hasRole('REGISTERED_USER')")
    public ResponseEntity<TestCompletionResponse> complete(
            @RequestBody TestCompletionRequest testCompletionRequest
            ) {
        UserDetails userDetails =  (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var result = service.finishTest(testCompletionRequest, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Fetch a test execution for a lecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TestExecutionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @GetMapping("/lecture")
    @PreAuthorize("hasRole('REGISTERED_USER')")
    public ResponseEntity<TestExecutionDto> findByUserIdAndLectureId(@RequestParam Long lectureId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var result = service.findByUserIdAndLectureId(user.getId(), lectureId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Fetch an active test for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TestExecutionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @GetMapping("/unfinished")
    @PreAuthorize("hasRole('REGISTERED_USER')")
    public ResponseEntity<TestExecutionDto> findUnfinished() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var result = service.findByFinishedAndUserId(false, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Fetch all finished tests for lecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TestExecutionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @GetMapping("/lecture/finished")
    @PreAuthorize("hasRole('REGISTERED_USER')")
    public ResponseEntity<List<TestExecutionDto>> findAllFinishedForLectureId(@RequestParam Long lectureId) {
        var result = service.findAllByFinishedAndLectureId(true, lectureId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Fetch all finished tests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TestExecutionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @GetMapping("/finished")
    @PreAuthorize("hasRole('REGISTERED_USER')")
    public ResponseEntity<List<TestExecutionDto>> findAllFinished() {
        var result = service.findAllByFinished(true);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
