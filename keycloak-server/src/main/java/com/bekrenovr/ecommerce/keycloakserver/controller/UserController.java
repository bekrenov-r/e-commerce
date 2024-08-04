package com.bekrenovr.ecommerce.keycloakserver.controller;

import com.bekrenovr.ecommerce.keycloakserver.model.Role;
import com.bekrenovr.ecommerce.keycloakserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> addUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("role") Role role
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addUser(username, password, role));
    }

    @PostMapping("/activate")
    public void enableUser(@RequestParam("token") String token) {
        userService.enableUser(token);
    }
}
