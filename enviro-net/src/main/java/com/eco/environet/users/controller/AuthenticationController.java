package com.eco.environet.users.controller;

import com.eco.environet.users.dto.*;
import com.eco.environet.users.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authorization", description = "Log In and Register")
public class AuthenticationController {

    private final AuthenticationService service;

    @Operation(summary = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registered",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Email or username already exist",
                    content = @Content) })
    @PostMapping(value="/register", consumes="application/json")
    public ResponseEntity<AuthenticationResponse> registerUser(
            @RequestBody RegisterRequest request
    ) {
        var result = service.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Authenticate user by username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged in",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Username or password incorrect",
                    content = @Content) })
    @PostMapping(value = "/authenticate", consumes = "application/json")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        var result = service.authenticate(request);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Register new organization member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registered",
                    content = { @Content(mediaType = "text/plain; charset=utf-8",
                            schema = @Schema(implementation = AuthenticationResponse.class)) }),
            @ApiResponse(responseCode = "409", description = "Email already exist",
                    content = @Content) })
    @PostMapping(value="/register/members", consumes="application/json")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> registerOrganizationMember(
            @RequestBody RegisterRequest request
    ) {
        service.registerOrganizationMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Finalize organization member registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member registration finalized"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping(value="/verify/members", consumes="application/json")
    public ResponseEntity<Void> finalizeRegistration(
            @RequestBody VerifyMemberRequest request
    ) {
        service.verifyOrganizationMember(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Check if the provided old password is valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Old password is valid"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping(value="/check-old-password", consumes="application/json")
    public ResponseEntity<Boolean> checkOldPasswordMatch(@RequestBody AuthenticationRequest request) {
        boolean isValid = service.checkOldPasswordMatch(request);
        return ResponseEntity.ok().body(isValid);
    }

    @Operation(summary = "Change password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping(value="/change-password")
    public ResponseEntity<AuthenticationResponse> changePassword(@Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = service.changePassword(request);
        return ResponseEntity.ok(response);
    }
}
