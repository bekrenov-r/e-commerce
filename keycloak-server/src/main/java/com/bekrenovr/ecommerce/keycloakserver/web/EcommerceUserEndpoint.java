package com.bekrenovr.ecommerce.keycloakserver.web;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.common.security.Role;
import com.bekrenovr.ecommerce.keycloakserver.dao.ActivationTokenDao;
import com.bekrenovr.ecommerce.keycloakserver.model.ActivationToken;
import com.bekrenovr.ecommerce.keycloakserver.providers.userstorage.EcommerceUserStorageProvider;
import com.bekrenovr.ecommerce.keycloakserver.providers.userstorage.EcommerceUserStorageProviderFactory;
import com.bekrenovr.ecommerce.keycloakserver.util.KeycloakCacheCleaner;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.*;
import org.keycloak.protocol.oidc.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.services.Urls;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.managers.AuthenticationSessionManager;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.keycloak.sessions.RootAuthenticationSessionModel;
import org.keycloak.storage.UserStorageProvider;

import java.util.UUID;

import static com.bekrenovr.ecommerce.keycloakserver.exception.KeycloakApplicationExceptionReason.ACTIVATION_TOKEN_NOT_FOUND;

public class EcommerceUserEndpoint {
    private final KeycloakSession session;
    private final RealmModel realmModel;
    private final ActivationTokenDao activationTokenDao;
    private final EcommerceUserStorageProvider userStorage;
    private final KeycloakCacheCleaner cacheCleaner;

    public EcommerceUserEndpoint(KeycloakSession session) {
        this.session = session;
        this.realmModel = session.realms().getRealm("e-commerce");
        this.activationTokenDao = new ActivationTokenDao();
        this.cacheCleaner = new KeycloakCacheCleaner();
        this.userStorage = (EcommerceUserStorageProvider)session.getComponentProvider(UserStorageProvider.class, EcommerceUserStorageProviderFactory.COMPONENT_ID);
    }

    @POST
    @Path("")
    @Produces({MediaType.TEXT_PLAIN})
    public Response addUser(@QueryParam("username") String username,
                            @QueryParam("password") String rawPassword,
                            @QueryParam("role") Role role,
                            @QueryParam("firstName") String firstName) {
        System.out.println(firstName);
        userStorage.addUser(username, rawPassword, role, firstName);
        String activationToken = createActivationToken(username);
        return Response.status(Response.Status.CREATED)
                .entity(activationToken)
                .build();
    }

    @GET
    @Path("/activation-token")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getActivationTokenForUser(@QueryParam("username") String username) {
        if(!activationTokenDao.existsByUsername(username)){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        ActivationToken token = activationTokenDao.findByUsername(username);
        return Response.status(Response.Status.OK)
                .entity(token.getToken())
                .build();
    }

    @POST
    @Path("/enable")
    @Produces(MediaType.APPLICATION_JSON)
    public Response enableUser(@QueryParam("token") String token) {
        UserModel enabledUser = doEnableUser(token);
        AccessTokenResponse response = generateAccessToken(enabledUser);
        return Response.status(Response.Status.OK)
                .entity(response)
                .build();
    }

    private UserModel doEnableUser(String token) {
        if(!activationTokenDao.existsByToken(token))
            throw new EcommerceApplicationException(ACTIVATION_TOKEN_NOT_FOUND, token);
        ActivationToken activationToken = activationTokenDao.findByToken(token);
        UserModel enabledUser = userStorage.enableUser(realmModel, activationToken.getUsername());
        activationTokenDao.delete(activationToken.getUsername());
        cacheCleaner.cleanUsersCache();
        return enabledUser;
    }

    private String createActivationToken(String username) {
        ActivationToken activationToken = new ActivationToken(
                username, RandomStringUtils.random(20, true, true)
        );
        activationTokenDao.save(activationToken);
        return activationToken.getToken();
    }

    private AccessTokenResponse generateAccessToken(UserModel user){
        ClientConnection clientConnection = session.getContext().getConnection();
        RealmModel realm = session.realms().getRealm("e-commerce");
        ClientModel client = session.clients().getClientByClientId(realm, "ecommerce-api");
        EventBuilder eventBuilder = new EventBuilder(realm, session);
        UserSessionModel userSession = session.sessions().createUserSession(
                UUID.randomUUID().toString(), realmModel, user,
                user.getUsername(), clientConnection.getRemoteAddr(),
                "client_auth", false, null, null,
                UserSessionModel.SessionPersistenceState.PERSISTENT
        );
        RootAuthenticationSessionModel rootAuthSession = (new AuthenticationSessionManager(session))
                .createAuthenticationSession(realmModel, false);
        AuthenticationSessionModel authSession = rootAuthSession.createAuthenticationSession(client);
        authSession.setAuthenticatedUser(user);
        authSession.setProtocol("openid-connect");
        authSession.setClientNote("iss", Urls.realmIssuer(this.session.getContext().getUri().getBaseUri(), realm.getName()));
        AuthenticationManager.setClientScopesInSession(authSession);
        ClientSessionContext clientSessionCtx = TokenManager.attachAuthenticationSession(this.session, userSession, authSession);

        TokenManager tokenManager = new TokenManager();
        return tokenManager
                .responseBuilder(realm, client, eventBuilder, session, userSession, clientSessionCtx)
                .generateAccessToken()
                .generateRefreshToken()
                .generateIDToken()
                .generateAccessTokenHash()
                .build();
    }
}
