package com.eco.environet.finance.controller;

import com.eco.environet.finance.dto.FixedExpensesDto;
import com.eco.environet.finance.services.FixedExpensesService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fixed-expenses")
@RequiredArgsConstructor
@Tag(name = "Fixed Expenses", description = "Manage Fixed Expenses")
public class FixedExpensesController {
    private final FixedExpensesService service;

    @Operation(summary = "Generate last month Salary Expenses or display if already exists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Last month  Expenses fetched!",
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
    @GetMapping(value = "/salary")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<Page<FixedExpensesDto>> lastMonthSalaryExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "period.startDate") String sortField,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String sortDirection,
            @RequestParam(name = "creatorId", required = true) Long creatorId
    ) {
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        var result = service.lastMonthSalaryExpenses(creatorId, pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Create new fixed expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New fixed expense created!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedExpensesDto.class)) }),
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
    public ResponseEntity<FixedExpensesDto> createNewFixedExpense(@RequestBody FixedExpensesDto newExpenseDto) {
        var result = service.create(newExpenseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Get all fixed expenses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all fixed expenses",
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
    public ResponseEntity<Page<FixedExpensesDto>> getAllFixedExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "type") String sortField,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(name = "period", required = false) String period,
            @RequestParam(name = "types", required = false) List<String> types,
            @RequestParam(name = "employees", required = false) List<Long> employees,
            @RequestParam(name = "creators", required = false) List<Long> creators
    ) {
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        var result = service.findAll(period, types, employees, creators, pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Get fixed expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched fixed expense",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedExpensesDto.class)) }),
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
    public ResponseEntity<FixedExpensesDto> getFixedExpense(@PathVariable Long id){
        var result = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Accountant updates salary expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salary expense updated!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedExpensesDto.class)) }),
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
    @PutMapping(value = "/update/salary")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<FixedExpensesDto> updateSalaryExpense(@RequestBody FixedExpensesDto fixedExpensesDto) {
        var result = service.updateSalaryExpense(fixedExpensesDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @Operation(summary = "Accountant updates fixed expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fixed expense updated!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedExpensesDto.class)) }),
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
    public ResponseEntity<FixedExpensesDto> updateFixedExpense(@RequestBody FixedExpensesDto fixedExpensesDto) {
        var result = service.update(fixedExpensesDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Accountant can delete fixed expense by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fixed expense deleted successfully"),
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
    public ResponseEntity<Void> deleteFixedExpense(@PathVariable Long id, @RequestBody FixedExpensesDto fixedExpenseDto) {
        // Check if authenticated user matches the author of fixed expense
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if (!currentUsername.equals(fixedExpenseDto.getCreator().getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        service.delete(fixedExpenseDto.getId());
        return ResponseEntity.noContent().build();
    }
}
