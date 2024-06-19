package com.eco.environet.finance.controller;

import com.eco.environet.finance.dto.FixedExpensesEstimationDto;
import com.eco.environet.finance.services.FixedExpensesEstimationService;
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
@RequestMapping("/api/fixed-expenses-estimation")
@RequiredArgsConstructor
@Tag(name = "Fixed Expenses Estimation", description = "Manage Fixed Expenses Estimations for Budget Planning")
public class FixedExpensesEstimationController {
    private final FixedExpensesEstimationService service;

    @Operation(summary = "Generate Fixed Expenses Estimations for BudgetPlan or display if already exists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fixed Expenses Estimations fetched!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedExpensesEstimationDto[].class)) }),
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
    @GetMapping(value = "/generate")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<List<FixedExpensesEstimationDto>> generate(
            @RequestParam(name = "budgetPlanId", required = true) Long budgetPlanId
    ) {
        var result = service.generateEstimationForBudgetPlan(budgetPlanId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Create new fixed expense estimation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New fixed expense estimation created!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedExpensesEstimationDto.class)) }),
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
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<FixedExpensesEstimationDto> createNewFixedExpense(@RequestBody FixedExpensesEstimationDto newExpenseDto) {
        var result = service.create(newExpenseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Get all filtered fixed expenses estimation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all filtered fixed expenses estimation",
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
    @GetMapping(value="/all")
    @PreAuthorize("hasAnyRole('BOARD_MEMBER', 'ACCOUNTANT')")
    public ResponseEntity<Page<FixedExpensesEstimationDto>> getAllFixedExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(name = "budgetPlanId", required = true) Long budgetPlanId,
            @RequestParam(name = "types", required = false) List<String> types,
            @RequestParam(name = "employees", required = false) List<Long> employees,
            @RequestParam(name = "sort", required = false, defaultValue = "type") String sortField,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String sortDirection
    ) {
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        var result = service.findAll(budgetPlanId, types, employees, pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Get fixed expense estimation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched fixed expense estimation",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedExpensesEstimationDto.class)) }),
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
    @GetMapping(value="/get/{id}")
    @PreAuthorize("hasAnyRole('BOARD_MEMBER', 'ACCOUNTANT')")
    public ResponseEntity<FixedExpensesEstimationDto> getFixedExpense(@PathVariable Long id){
        var result = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Accountant updates fixed expense estimation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fixed expense estimation updated!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedExpensesEstimationDto.class)) }),
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
    public ResponseEntity<FixedExpensesEstimationDto> updateFixedExpense(@RequestBody FixedExpensesEstimationDto FixedExpensesEstimationDto) {
        var result = service.update(FixedExpensesEstimationDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Accountant can delete fixed expense estimation by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fixed expense estimation deleted successfully"),
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
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<Void> deleteFixedExpense(@PathVariable Long id, @RequestBody FixedExpensesEstimationDto fixedExpenseDto) {
        service.delete(fixedExpenseDto.getId());
        return ResponseEntity.noContent().build();
    }
}
