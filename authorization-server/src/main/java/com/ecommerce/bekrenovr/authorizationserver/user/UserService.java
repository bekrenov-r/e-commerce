package com.ecommerce.bekrenovr.authorizationserver.user;

import com.bekrenovr.ecommerce.common.model.Person;
import com.bekrenovr.ecommerce.common.security.Role;
import com.ecommerce.bekrenovr.authorizationserver.feign.CustomersProxy;
import com.ecommerce.bekrenovr.authorizationserver.feign.KeycloakProxy;
import com.ecommerce.bekrenovr.authorizationserver.support.MailService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final MailService mailService;
    private final CustomersProxy customersProxy;
    private final KeycloakProxy keycloakProxy;

    public void sendEmailForPasswordRecovery(String email) {
        Person person = customersProxy.getCustomerByEmail(email).getBody();
        String recoveryToken = RandomStringUtils.random(20, true, true);
        try {
            keycloakProxy.createPasswordRecoveryToken(person.getEmail(), recoveryToken);
        } catch(FeignException.NotFound ex) {
            String tempPassword = RandomStringUtils.random(20, true, true);
            String activationToken = keycloakProxy
                    .createKeycloakUser(person.getEmail(), tempPassword, Role.CUSTOMER.name(), person.getFirstName())
                    .getBody();
            keycloakProxy.enableUser(activationToken);
            keycloakProxy.createPasswordRecoveryToken(person.getEmail(), recoveryToken);
        }
        mailService.sendPasswordRecoveryEmail(person, recoveryToken);
    }

    public void recoverPassword(String token, String newPassword) {
        keycloakProxy.recoverPassword(token, newPassword);
    }
}
