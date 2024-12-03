package com.ecommerce.bekrenovr.authorizationserver.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "UserController")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Recover password / Send email with password recovery token",
            description = "When invoked with 'email' query param, sends email with password recovery token; with 'token' and 'password' params - resets password"
    )
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "400", description = "Invalid parameter supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "User not found / Password recovery token not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "User is disabled",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @Parameters({
            @Parameter(name = "email", description = "Required unless both 'token' and 'password' are present"),
            @Parameter(name = "token", description = "Required if 'email' is not present"),
            @Parameter(name = "password", description = "Required if 'email' is not present"),
    })
    @PostMapping(value = "/recover-password")
    public void passwordRecovery(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "password", required = false) String newPassword
    ) {
        if(email != null && !email.isBlank()) {
            userService.sendEmailForPasswordRecovery(email);
        } else {
            userService.recoverPassword(token, newPassword);
        }
    }
}
