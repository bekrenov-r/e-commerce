package com.ecommerce.bekrenovr.authorizationserver.service;

import com.bekrenovr.ecommerce.common.model.Person;
import com.ecommerce.bekrenovr.authorizationserver.proxy.CustomerServiceProxy;
import com.ecommerce.bekrenovr.authorizationserver.proxy.KeycloakProxy;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final MailService mailService;
    private final CustomerServiceProxy customerServiceProxy;
    private final KeycloakProxy keycloakProxy;

    public void sendEmailForPasswordRecovery(String email) {
        Person person = customerServiceProxy.getCustomerByEmail(email).getBody();
        String recoveryToken = RandomStringUtils.random(20, true, true);
        keycloakProxy.createPasswordRecoveryToken(person.getEmail(), recoveryToken);
        mailService.sendPasswordRecoveryEmail(person, recoveryToken);
    }

    public void recoverPassword(String token, String newPassword) {
        keycloakProxy.recoverPassword(token, newPassword);
    }
}
