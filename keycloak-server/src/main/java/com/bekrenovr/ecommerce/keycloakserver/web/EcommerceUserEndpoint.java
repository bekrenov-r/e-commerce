package com.bekrenovr.ecommerce.keycloakserver.web;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.keycloakserver.dao.ActivationTokenDao;
import com.bekrenovr.ecommerce.keycloakserver.dao.EcommerceUserDao;
import com.bekrenovr.ecommerce.keycloakserver.model.ActivationToken;
import com.bekrenovr.ecommerce.keycloakserver.model.Role;
import com.bekrenovr.ecommerce.keycloakserver.util.KeycloakCacheCleaner;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.utils.SHAPasswordEncoder;

import static com.bekrenovr.ecommerce.keycloakserver.exception.KeycloakApplicationExceptionReason.ACTIVATION_TOKEN_NOT_FOUND;
import static com.bekrenovr.ecommerce.keycloakserver.exception.KeycloakApplicationExceptionReason.USER_ALREADY_EXISTS;

public class EcommerceUserEndpoint {
    private final KeycloakSession session;
    private final ActivationTokenDao activationTokenDao;
    private final EcommerceUserDao userDao;
    private final KeycloakCacheCleaner cacheCleaner;

    public EcommerceUserEndpoint(KeycloakSession session) {
        this.session = session;
        this.activationTokenDao = new ActivationTokenDao();
        this.userDao = new EcommerceUserDao();
        this.cacheCleaner = new KeycloakCacheCleaner();
    }

    @POST
    @Path("")
    @Produces({MediaType.TEXT_PLAIN})
    public Response addUser(@QueryParam("username") String username,
                            @QueryParam("password") String password,
                            @QueryParam("role") Role role) {
        doAddUser(username, password, role);
        String activationToken = createActivationToken(username);
        return Response.status(Response.Status.CREATED)
                .entity(activationToken)
                .build();
    }

    @POST
    @Path("/enable")
    @Produces(MediaType.APPLICATION_JSON)
    public Response enableUser(@QueryParam("token") String token) {
        doEnableUser(token);
        return Response.status(Response.Status.OK)
                .entity("")
                .build();
    }

    private void doAddUser(String username, String password, Role role) {
        String encodedPassword = new SHAPasswordEncoder(256).encode(password);
        if(userDao.existsByUsername(username))
            throw new EcommerceApplicationException(USER_ALREADY_EXISTS, username);
        userDao.addUser(username, encodedPassword, role);
    }

    private void doEnableUser(String token) {
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
