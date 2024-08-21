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

    @Value("${frontend-base-url}")
    private String frontendBaseUrl;

    @Value("${spring.mail.username}")
    private String senderAddress;

    private static final String ACTIVATION_EMAIL_TEMPLATE_PATH = "/email_templates/activation.txt";

    public void sendCustomerAccountActivationEmail(Person customer, String activationToken) {
        String contentTemplate = getContentTemplate(ACTIVATION_EMAIL_TEMPLATE_PATH);
        String url = frontendBaseUrl + "/activate-account?token=" + activationToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(customer.getEmail());
        message.setSubject("Activate your account");
        message.setFrom("SpaceFincher <" + senderAddress + ">");
        message.setText(contentTemplate.formatted(customer.getFirstName(), url));
        CompletableFuture.runAsync(() -> mailSender.send(message));
    }

    private String getContentTemplate(String path) {
        var inputStream = getClass().getResourceAsStream(path);
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }
}
