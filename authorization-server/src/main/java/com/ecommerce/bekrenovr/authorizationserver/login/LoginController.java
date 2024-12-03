package com.ecommerce.bekrenovr.authorizationserver.login;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Tag(name = "LoginController")
public class LoginController {
    private final KeycloakLoginService keycloakLoginService;
    private final GoogleLoginService googleLoginService;

    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
    private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

    @Operation(summary = "Basic authentication")
    @ApiResponse(responseCode = "200", description = "Access token granted",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(ref = "#/components/schemas/AccessTokenResponseSchema")))
    @ApiResponse(responseCode = "400", description = "Invalid parameter(s) supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "401", description = "Invalid credentials",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @Parameters({
            @Parameter(name = "grant_type", schema = @Schema(allowableValues = {GRANT_TYPE_PASSWORD, GRANT_TYPE_REFRESH_TOKEN})),
            @Parameter(name = "username"),
            @Parameter(name = "password"),
            @Parameter(name = "refresh_token")
    })
    @PostMapping("/basic")
    public ResponseEntity<String> accessTokenBasic(
            @RequestParam("grant_type") String grantType, HttpServletRequest request
    ) throws MissingServletRequestParameterException {
        return switch (grantType) {
            case GRANT_TYPE_PASSWORD -> processAccessTokenRequestBasic(request);
            case GRANT_TYPE_REFRESH_TOKEN -> processRefreshTokenRequestBasic(request);
            default -> throw new IllegalArgumentException("grant_type is not supported");
        };
    }

    @Operation(summary = "Google authentication")
    @ApiResponse(responseCode = "200", description = "Access token granted",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(ref = "#/components/schemas/AccessTokenResponseSchema")))
    @ApiResponse(responseCode = "400", description = "Invalid parameter(s) supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "401", description = "Invalid credentials",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @Parameters({
            @Parameter(name = "grant_type", schema = @Schema(allowableValues = {GRANT_TYPE_AUTHORIZATION_CODE, GRANT_TYPE_REFRESH_TOKEN})),
            @Parameter(name = "code"),
            @Parameter(name = "refresh_token")
    })
    @PostMapping("/google")
    public ResponseEntity<String> accessTokenGoogle(
            @RequestParam("grant_type") String grantType, HttpServletRequest request
    ) throws MissingServletRequestParameterException {
        return switch(grantType) {
            case GRANT_TYPE_AUTHORIZATION_CODE -> processAccessTokenRequestGoogle(request);
            case GRANT_TYPE_REFRESH_TOKEN -> processRefreshTokenRequestGoogle(request);
            default -> throw new IllegalArgumentException("grant_type is not supported");
        };
    }

    private ResponseEntity<String> processAccessTokenRequestBasic(HttpServletRequest request) throws MissingServletRequestParameterException {
        String username = Optional.ofNullable(request.getParameter("username"))
                .orElseThrow(() -> new MissingServletRequestParameterException("username", String.class.getTypeName()));
        String password = Optional.ofNullable(request.getParameter("password"))
                .orElseThrow(() -> new MissingServletRequestParameterException("password", String.class.getTypeName()));
        return keycloakLoginService.getAccessTokenBasic(username, password);
    }

    private ResponseEntity<String> processRefreshTokenRequestBasic(HttpServletRequest request) throws MissingServletRequestParameterException {
        String refreshToken = Optional.ofNullable(request.getParameter("refresh_token"))
                .orElseThrow(() -> new MissingServletRequestParameterException("refresh_token", String.class.getTypeName()));
        return keycloakLoginService.refreshAccessTokenBasic(refreshToken);
    }

    private ResponseEntity<String> processAccessTokenRequestGoogle(HttpServletRequest request) throws MissingServletRequestParameterException {
        String code = Optional.ofNullable(request.getParameter("code"))
                .orElseThrow(() -> new MissingServletRequestParameterException("code", String.class.getTypeName()));
        return ResponseEntity.ok(googleLoginService.getAccessTokenGoogle(code));
    }

    private ResponseEntity<String> processRefreshTokenRequestGoogle(HttpServletRequest request) throws MissingServletRequestParameterException {
        String code = Optional.ofNullable(request.getParameter("refresh_token"))
                .orElseThrow(() -> new MissingServletRequestParameterException("refresh_token", String.class.getTypeName()));
        return ResponseEntity.ok(googleLoginService.refreshAccessTokenGoogle(code));
    }
}
