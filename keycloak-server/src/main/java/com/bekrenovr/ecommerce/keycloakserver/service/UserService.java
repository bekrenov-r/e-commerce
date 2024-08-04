package com.bekrenovr.ecommerce.keycloakserver.service;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.keycloakserver.config.SecondaryDatasourceConfigProperties;
import com.bekrenovr.ecommerce.keycloakserver.dao.ActivationTokenDao;
import com.bekrenovr.ecommerce.keycloakserver.dao.EcommerceUserDao;
import com.bekrenovr.ecommerce.keycloakserver.model.ActivationToken;
import com.bekrenovr.ecommerce.keycloakserver.model.Role;
import com.bekrenovr.ecommerce.keycloakserver.util.KeycloakCacheCleaner;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.bekrenovr.ecommerce.keycloakserver.exception.KeycloakApplicationExceptionReason.ACTIVATION_TOKEN_NOT_FOUND;
import static com.bekrenovr.ecommerce.keycloakserver.exception.KeycloakApplicationExceptionReason.USER_ALREADY_EXISTS;

public class UserService {
    private final EcommerceUserDao userDao;
    private final ActivationTokenDao activationTokenDao;
    private final KeycloakCacheCleaner cacheCleaner;

    public UserService(SecondaryDatasourceConfigProperties secondaryDatasourceConfigProperties,
                       KeycloakCacheCleaner cacheCleaner) {
        this.userDao = new EcommerceUserDao(secondaryDatasourceConfigProperties);
        this.activationTokenDao = new ActivationTokenDao(secondaryDatasourceConfigProperties);
        this.cacheCleaner = cacheCleaner;
    }

    public String addUser(String username, String password, Role role) {
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        if(userDao.existsByUsername(username))
            throw new EcommerceApplicationException(USER_ALREADY_EXISTS, username);
        userDao.addUser(username, encodedPassword, role);
        return createActivationToken(username);
    }

    public void enableUser(String token) {
        if(!activationTokenDao.exists(token))
            throw new EcommerceApplicationException(ACTIVATION_TOKEN_NOT_FOUND, token);
        ActivationToken activationToken = activationTokenDao.findByToken(token);
        userDao.enableUser(activationToken.getUsername());
        activationTokenDao.delete(activationToken.getUsername());
        cacheCleaner.cleanUsersCache();
    }

    private String createActivationToken(String username) {
        ActivationToken activationToken = new ActivationToken(
                username, RandomStringUtils.random(20, true, true)
        );
        activationTokenDao.save(activationToken);
        return activationToken.getToken();
    }
}
