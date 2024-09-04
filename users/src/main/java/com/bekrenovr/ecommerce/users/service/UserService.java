package com.bekrenovr.ecommerce.users.service;

import com.bekrenovr.ecommerce.users.model.Person;
import com.bekrenovr.ecommerce.users.proxy.KeycloakProxy;
import com.bekrenovr.ecommerce.users.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final MailService mailService;
    private final PersonRepository personRepository;
    private final KeycloakProxy keycloakProxy;

    @Transactional
    public void sendEmailForPasswordRecovery(String email) {
        Person person = personRepository.findByEmailOrThrowDefault(email);
        String recoveryToken = RandomStringUtils.random(20, true, true);
        keycloakProxy.createPasswordRecoveryToken(person.getEmail(), recoveryToken);
        mailService.sendPasswordRecoveryEmail(person, recoveryToken);
    }

    public void recoverPassword(String token, String newPassword) {
        keycloakProxy.recoverPassword(token, newPassword);
    }
}
