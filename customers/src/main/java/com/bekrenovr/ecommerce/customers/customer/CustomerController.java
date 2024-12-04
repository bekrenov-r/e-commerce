package com.bekrenovr.ecommerce.customers.customer;

import com.bekrenovr.ecommerce.customers.customer.dto.CustomerRequest;
import com.bekrenovr.ecommerce.customers.customer.dto.CustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "CustomerController")
@Validated
public class CustomerController {
    private final CustomerService customerService;

    @Operation(summary = "Get customer by email")
    @ApiResponse(responseCode = "200", description = "Customer found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CustomerResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid email supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping
    public ResponseEntity<CustomerResponse> getByEmail(@RequestParam @NotBlank @Email String email) {
        return ResponseEntity.ok(customerService.getByEmail(email));
    }

    @Operation(summary = "Create customer")
    @ApiResponse(responseCode = "200", description = "Customer exists but is not registered; set 'registered' to true")
    @ApiResponse(responseCode = "201", description = "Customer created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request body supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Customer already exists and is registered",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CustomerRequest request){
        HttpStatus status = customerService.create(request);
        return ResponseEntity.status(status).build();
    }

    @Operation(summary = "Update customer")
    @ApiResponse(responseCode = "200", description = "Customer updated successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CustomerResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request body supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PutMapping
    public ResponseEntity<CustomerResponse> update(@Valid @RequestBody CustomerRequest request){
        return ResponseEntity.ok(customerService.update(request));
    }
}
