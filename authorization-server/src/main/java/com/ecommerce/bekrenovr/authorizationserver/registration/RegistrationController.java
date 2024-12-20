package com.ecommerce.bekrenovr.authorizationserver.registration;

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
@RequestMapping("/registration")
@RequiredArgsConstructor
@Validated
@Tag(name = "RegistrationController")
public class RegistrationController {
    private final RegistrationService registrationService;

    @Operation(summary = "Register customer")
    @ApiResponse(responseCode = "200", description = "Customer registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request body supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Customer already exists",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping("/customer")
    public ResponseEntity<Void> registerCustomer(@RequestBody @Valid CustomerRegistrationRequest request) {
        registrationService.registerCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Resend email with account activation code")
    @ApiResponse(responseCode = "200", description = "Email sent")
    @ApiResponse(responseCode = "400", description = "Invalid parameter supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Customer or activation token not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping("/customer/resend-email")
    public void resendActivationEmail(@RequestParam @NotBlank @Email String email) {
        registrationService.resendActivationEmail(email);
    }

    @Operation(summary = "Activate account")
    @ApiResponse(responseCode = "200", description = "Account activated successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(ref = "#/components/schemas/AccessTokenResponseSchema")))
    @ApiResponse(responseCode = "400", description = "Invalid parameter supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Activation token not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping("/customer/activate-account")
    public ResponseEntity<?> activateAccount(@RequestParam @NotBlank String token) {
        return registrationService.activateAccount(token);
    }
}
