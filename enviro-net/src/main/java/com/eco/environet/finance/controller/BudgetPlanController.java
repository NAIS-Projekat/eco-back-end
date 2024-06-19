package com.eco.environet.finance.controller;

import com.eco.environet.finance.dto.BudgetPlanDto;
import com.eco.environet.finance.services.BudgetPlanService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import java.util.List;

@RestController
@RequestMapping("/api/budget-plan")
@RequiredArgsConstructor
@Tag(name = "Budget Plan", description = "Manage budget plans")
public class BudgetPlanController {
    private final BudgetPlanService service;

    @Operation(summary = "Create new budget plan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New budget plan created!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetPlanDto.class)) }),
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
    public ResponseEntity<BudgetPlanDto> createNewBudgetPlan(@RequestBody BudgetPlanDto newBudgetPlanDto) {
        var result = service.create(newBudgetPlanDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Get all budget plans")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all budget plans",
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
    @GetMapping(value="/all-plans")
    @PreAuthorize("hasAnyRole('BOARD_MEMBER', 'ACCOUNTANT')")
    public ResponseEntity<Page<BudgetPlanDto>> getAllBudgetPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(name="id") Long id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "period", required = false) String period,
            @RequestParam(name = "statuses", required = false) List<String> statuses,
            @RequestParam(name = "authors", required = false) List<Long> authors,
            @RequestParam(name = "sort", required = false, defaultValue = "name") String sortField,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String sortDirection
    ) {
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        var result = service.findAll(id, name, period, statuses, authors, pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Get budget plan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetPlanDto.class)) }),
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
    @GetMapping(value="/get-budget/{id}")
    @PreAuthorize("hasAnyRole('BOARD_MEMBER', 'ACCOUNTANT')")
    public ResponseEntity<BudgetPlanDto> getBudgetPlan(@PathVariable Long id){
        var result = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Accountant updates budget plan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Budget plan updated!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetPlanDto.class)) }),
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
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<BudgetPlanDto> updateNewBudgetPlan(@RequestBody BudgetPlanDto budgetPlanDto) {
        // Check if authenticated user matches the author of budget plan
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if (!currentUsername.equals(budgetPlanDto.getAuthor().getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var result = service.update(budgetPlanDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Accountant can archive a budget plan by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Budget plan archived successfully"),
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
    @PutMapping("/archive")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<Void> archiveBudgetPlan(@RequestBody BudgetPlanDto budgetPlanDto) {
        // Check if authenticated user matches the author of budget plan
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if (!currentUsername.equals(budgetPlanDto.getAuthor().getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        service.archive(budgetPlanDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Accountant closes a budget plan by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Budget plan closed successfully"),
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
    @PutMapping("/close")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<Void> closeBudgetPlan(@RequestBody BudgetPlanDto budgetPlanDto) {
        // Check if authenticated user matches the author of budget plan
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if (!currentUsername.equals(budgetPlanDto.getAuthor().getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        service.close(budgetPlanDto);
        return ResponseEntity.noContent().build();
    }
}
