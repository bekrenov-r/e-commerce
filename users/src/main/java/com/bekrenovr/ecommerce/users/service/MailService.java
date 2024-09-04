package com.bekrenovr.ecommerce.users.service;

import com.bekrenovr.ecommerce.users.model.Person;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${frontend.base-url}")
    private String frontendBaseUrl;

    @Value("${frontend.mapping.activate-account}")
    private String activateAccountMapping;

    @Value("${frontend.mapping.recover-password}")
    private String recoverPasswordMapping;

    @Value("${spring.mail.username}")
    private String senderAddress;

    private static final String ACTIVATION_EMAIL_TEMPLATE_PATH = "/email_templates/activation.txt";
    private static final String PASSWORD_RECOVERY_EMAIL_TEMPLATE_PATH = "/email_templates/password-recovery.txt";

    public void sendCustomerAccountActivationEmail(Person customer, String activationToken) {
        String contentTemplate = getContentTemplate(ACTIVATION_EMAIL_TEMPLATE_PATH);
        String url = frontendBaseUrl + activateAccountMapping + "?token=" + activationToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(customer.getEmail());
        message.setSubject("Activate your account");
        message.setFrom("SpaceFincher <" + senderAddress + ">");
        message.setText(contentTemplate.formatted(customer.getFirstName(), url));
        CompletableFuture.runAsync(() -> mailSender.send(message));
    }

    public void sendPasswordRecoveryEmail(Person user, String recoveryToken) {
        String contentTemplate = getContentTemplate(PASSWORD_RECOVERY_EMAIL_TEMPLATE_PATH);
        String url = frontendBaseUrl + recoverPasswordMapping + "?token=" + recoveryToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Recover your password");
        message.setFrom("SpaceFincher <" + senderAddress + ">");
        message.setText(contentTemplate.formatted(user.getFirstName(), url));
        CompletableFuture.runAsync(() -> mailSender.send(message));
    }

    private String getContentTemplate(String path) {
        var inputStream = getClass().getResourceAsStream(path);
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }
}
