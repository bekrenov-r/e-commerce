package com.bekrenovr.ecommerce.keycloakserver.service;

import com.bekrenovr.ecommerce.keycloakserver.config.SecondaryDatasourceConfigProperties;
import com.bekrenovr.ecommerce.keycloakserver.dao.EcommerceUserDao;
import com.bekrenovr.ecommerce.keycloakserver.exception.UserAlreadyExistsException;
import com.bekrenovr.ecommerce.keycloakserver.model.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {
    private final EcommerceUserDao userDao;

    public UserService(SecondaryDatasourceConfigProperties secondaryDatasourceConfigProperties) {
        this.userDao = new EcommerceUserDao(secondaryDatasourceConfigProperties);
    }

    public void addUser(String username, String password, Role role) {
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        if(userDao.existsByUsername(username))
            throw new UserAlreadyExistsException(username);
        userDao.addUser(username, encodedPassword, role);
    }
}
