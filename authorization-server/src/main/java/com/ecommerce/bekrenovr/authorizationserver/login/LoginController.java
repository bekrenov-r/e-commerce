package com.ecommerce.bekrenovr.authorizationserver.login;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
public class LoginController {
    private final KeycloakLoginService keycloakLoginService;
    private final GoogleLoginService googleLoginService;

    @PostMapping("/basic")
    public ResponseEntity<String> accessTokenBasic(
            @RequestParam("grant_type") String grantType, HttpServletRequest request
    ) throws MissingServletRequestParameterException {
        return switch (grantType) {
            case "password" -> processAccessTokenRequestBasic(request);
            case "refresh_token" -> processRefreshTokenRequestBasic(request);
            default -> throw new IllegalArgumentException("grant_type is not supported");
        };
    }

    @PostMapping("/google")
    public ResponseEntity<String> accessTokenGoogle(
            @RequestParam("grant_type") String grantType, HttpServletRequest request
    ) throws MissingServletRequestParameterException {
        return switch(grantType) {
            case "authorization_code" -> processAccessTokenRequestGoogle(request);
            case "refresh_token" -> processRefreshTokenRequestGoogle(request);
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
