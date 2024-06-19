package com.eco.environet.education.controller;

import com.eco.environet.education.dto.EducatorQuestionDto;
import com.eco.environet.education.dto.UserQuestionDto;
import com.eco.environet.education.services.QuestionService;
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
@RequestMapping("/api/question")
@RequiredArgsConstructor
@Tag(name = "Question", description = "Manage questions")
public class QuestionController {

    private final QuestionService service;

    @Operation(summary = "Create new question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EducatorQuestionDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content)
    })
    @PostMapping(consumes = "application/json")
    @PreAuthorize("hasRole('EDUCATOR')")
    public ResponseEntity<EducatorQuestionDto> create(
            @RequestBody EducatorQuestionDto question
    ) {
        var result = service.create(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Fetch all questions for lecture with id for educators")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EducatorQuestionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @GetMapping("/educator/lecture")
    @PreAuthorize("hasRole('EDUCATOR')")
    public ResponseEntity<List<EducatorQuestionDto>> findAllByCreatorIdForEducator(@RequestParam Long lectureId) {
        var result = service.findAllByLectureIdForEducator(lectureId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Fetch all questions for lecture with id for registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserQuestionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @GetMapping("/user/lecture")
    @PreAuthorize("hasRole('REGISTERED_USER')")
    public ResponseEntity<List<UserQuestionDto>> findAllByCreatorIdForUser(@RequestParam Long lectureId) {
        var result = service.findAllByLectureIdForUser(lectureId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Delete question")
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
