package com.bekrenovr.ecommerce.users.controller;

import com.bekrenovr.ecommerce.users.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/recover-password", params = "email")
    public void sendEmailForPasswordRecovery(@RequestParam @NotBlank @Email String email) {
        userService.sendEmailForPasswordRecovery(email);
    }

    @PostMapping(value = "/recover-password", params = "token")
    public void recoverPassword(@RequestParam @NotBlank String token, @RequestParam("password") String newPassword) {
        userService.recoverPassword(token, newPassword);
    }
}
