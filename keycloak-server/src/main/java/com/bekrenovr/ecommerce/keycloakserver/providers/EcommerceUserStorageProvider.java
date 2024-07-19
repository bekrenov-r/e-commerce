package com.bekrenovr.ecommerce.keycloakserver.providers;

import com.bekrenovr.ecommerce.keycloakserver.model.EcommerceUser;
import com.bekrenovr.ecommerce.keycloakserver.util.DbUtil;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public class EcommerceUserStorageProvider implements UserStorageProvider, UserLookupProvider, UserQueryProvider, CredentialInputValidator {

    private KeycloakSession keycloakSession;
    private ComponentModel componentModel;
    private PasswordEncoder passwordEncoder;

    public EcommerceUserStorageProvider(KeycloakSession keycloakSession, ComponentModel componentModel, PasswordEncoder passwordEncoder) {
        this.keycloakSession = keycloakSession;
        this.componentModel = componentModel;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void close() {
        log.info("close()");
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        log.info("supportsCredentialType()");
        return PasswordCredentialModel.TYPE.endsWith(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String credentialType) {
        log.info("isConfiguredFor()");
        return supportsCredentialType(credentialType);
    }

    @Override
    public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        log.info("isValid()");
        try(Connection connection = DbUtil.getConnection(componentModel)) {
            PreparedStatement ps = connection.prepareStatement("select password from users where username = ?");
            ps.setString(1, userModel.getUsername());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String encryptedPassword = rs.getString("password");
                return passwordEncoder.matches(credentialInput.getChallengeResponse(), encryptedPassword);
            } else {
                return false;
            }
        } catch(SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }

    @Override
    public UserModel getUserById(RealmModel realmModel, String s) {
        log.info("getUserById({})", s);
        return null;
    }

    @Override
    public UserModel getUserByUsername(RealmModel realmModel, String username) {
        log.info("getUserByUsername({})", username);
        try(Connection connection = DbUtil.getConnection(componentModel)) {
            PreparedStatement ps = connection.prepareStatement("select * from users where users.username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new EcommerceUser(keycloakSession, realmModel, componentModel, rs.getString("username"));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }

    @Override
    public UserModel getUserByEmail(RealmModel realmModel, String email) {
        log.info("getUserByEmail({})", email);
        return getUserByUsername(realmModel, email);
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realmModel, Map<String, String> map, Integer integer, Integer integer1) {
        return null;
    }

    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realmModel, GroupModel groupModel, Integer integer, Integer integer1) {
        return null;
    }

    @Override
    public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realmModel, String s, String s1) {
        return null;
    }
}
