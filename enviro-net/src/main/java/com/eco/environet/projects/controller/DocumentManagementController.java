package com.eco.environet.projects.controller;

import com.eco.environet.projects.dto.DocumentCreationDto;
import com.eco.environet.projects.dto.DocumentDto;
import com.eco.environet.projects.service.DocumentManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/projects/{projectId}/documents")
@Tag(name = "Document Creation", description = "Manage project documentation")
public class DocumentManagementController {

    private final DocumentManagementService documentManagementService;

    @Operation(summary = "Add project document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document added", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('PROJECT_MANAGER') and @projectRepository.findById(#projectId).orElse(null)?.manager?.id == authentication.principal.id")
    public ResponseEntity<DocumentDto> uploadDocument(
            @PathVariable Long projectId,
            @ModelAttribute @Valid DocumentCreationDto documentDto
    ) throws IOException {
        DocumentDto result = documentManagementService.uploadDocument(projectId, documentDto);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Delete project document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document deleted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "text/plain"))
    })
    @DeleteMapping(value = "/{documentId}")
    @PreAuthorize("hasRole('PROJECT_MANAGER') and @projectRepository.findById(#projectId).orElse(null)?.manager?.id == authentication.principal.id")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long projectId,
            @PathVariable Long documentId
    ) {
        documentManagementService.deleteDocument(projectId, documentId);
        return ResponseEntity.ok().build();
    }
}
