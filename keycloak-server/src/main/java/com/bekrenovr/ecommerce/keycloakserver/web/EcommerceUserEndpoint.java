package com.bekrenovr.ecommerce.keycloakserver.web;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.common.security.Role;
import com.bekrenovr.ecommerce.keycloakserver.model.PasswordCredentialInput;
import com.bekrenovr.ecommerce.keycloakserver.model.TokenType;
import com.bekrenovr.ecommerce.keycloakserver.model.entity.Token;
import com.bekrenovr.ecommerce.keycloakserver.providers.userstorage.EcommerceUserStorageProvider;
import com.bekrenovr.ecommerce.keycloakserver.providers.userstorage.EcommerceUserStorageProviderFactory;
import com.bekrenovr.ecommerce.keycloakserver.repository.TokenRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.keycloak.common.ClientConnection;
import org.keycloak.credential.CredentialInput;
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

import java.util.Optional;
import java.util.UUID;

import static com.bekrenovr.ecommerce.keycloakserver.exception.KeycloakApplicationExceptionReason.*;

public class EcommerceUserEndpoint {
    private final KeycloakSession session;
    private final RealmModel realmModel;
    private final TokenRepository tokenRepository;
    private final EcommerceUserStorageProvider userStorage;

    public EcommerceUserEndpoint(KeycloakSession session) {
        this.session = session;
        this.realmModel = session.realms().getRealm("e-commerce");
        this.tokenRepository = new TokenRepository();
        this.userStorage = (EcommerceUserStorageProvider)session.getComponentProvider(UserStorageProvider.class, EcommerceUserStorageProviderFactory.COMPONENT_ID);
    }

    @POST
    @Path("")
    @Produces({MediaType.TEXT_PLAIN})
    public Response addUser(@QueryParam("username") String username,
                            @QueryParam("password") String rawPassword,
                            @QueryParam("role") Role role,
                            @QueryParam("firstName") String firstName) {
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
        Token token = tokenRepository.findByUsernameAndType(username, TokenType.ACTIVATION)
                .orElseThrow(() -> new EcommerceApplicationException(ACTIVATION_TOKEN_NOT_FOUND, username));
        return Response.status(Response.Status.OK)
                .entity(token.getValue())
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

    @POST
    @Path("/recover-password")
    @Produces(MediaType.APPLICATION_JSON)
    public Response recoverPassword(@QueryParam("token") String username, @QueryParam("password") String newPassword) {
        doRecoverPassword(username, newPassword);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/recover-password/token")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPasswordRecoveryToken(@QueryParam("username") String username, @QueryParam("token") String token) {
        doCreatePasswordRecoveryToken(username, token);
        return Response.status(Response.Status.CREATED).build();
    }

    private UserModel doEnableUser(String token) {
        Token activationToken = tokenRepository.findByValueAndType(token, TokenType.ACTIVATION)
                .orElseThrow(() -> new EcommerceApplicationException(ACTIVATION_TOKEN_NOT_FOUND, token));
        UserModel enabledUser = userStorage.enableUser(realmModel, activationToken.getUsername());
        tokenRepository.removeByUsernameAndType(activationToken.getUsername(), TokenType.ACTIVATION);
        return enabledUser;
    }

    private String createActivationToken(String username) {
        Token token = new Token(
                username, RandomStringUtils.random(20, true, true), TokenType.ACTIVATION
        );
        tokenRepository.create(token);
        return token.getValue();
    }

    private void doRecoverPassword(String token, String newPassword) {
        tokenRepository.findByValueAndType(token, TokenType.PASSWORD_RECOVERY)
                .ifPresentOrElse(recoveryToken -> {
                    CredentialInput passwordInput =
                            new PasswordCredentialInput(recoveryToken.getUsername(), newPassword);
                    UserModel user = userStorage.getUserByUsername(realmModel, recoveryToken.getUsername());
                    userStorage.updateCredential(realmModel, user, passwordInput);
                    tokenRepository.removeAllByUsernameAndType(recoveryToken.getUsername(), TokenType.PASSWORD_RECOVERY);
                }, () -> {
                    throw new EcommerceApplicationException(PASSWORD_RECOVERY_TOKEN_NOT_FOUND, token);
                });
    }

    private void doCreatePasswordRecoveryToken(String username, String token) {
        Optional.ofNullable(userStorage.getUserByUsername(realmModel, username))
                .ifPresentOrElse(user -> {
                    if(!user.isEnabled())
                        throw new EcommerceApplicationException(USER_DISABLED, user.getUsername());
                    Token recoveryToken = new Token(user.getUsername(), token, TokenType.PASSWORD_RECOVERY);
                    tokenRepository.create(recoveryToken);
                }, () -> {
                    throw new EcommerceApplicationException(USER_NOT_FOUND, username);
                });
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
