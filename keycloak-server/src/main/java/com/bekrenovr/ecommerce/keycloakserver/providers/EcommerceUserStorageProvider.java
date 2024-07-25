package com.bekrenovr.ecommerce.keycloakserver.providers;

import com.bekrenovr.ecommerce.keycloakserver.dao.EcommerceUserDao;
import com.bekrenovr.ecommerce.keycloakserver.model.EcommerceUser;
import com.bekrenovr.ecommerce.keycloakserver.model.EcommerceUserAdapter;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public class EcommerceUserStorageProvider implements UserStorageProvider, UserLookupProvider, UserQueryProvider, CredentialInputValidator {

    private KeycloakSession keycloakSession;
    private ComponentModel componentModel;
    private PasswordEncoder passwordEncoder;
    private EcommerceUserDao ecommerceUserDao;

    public EcommerceUserStorageProvider(KeycloakSession keycloakSession, ComponentModel componentModel, PasswordEncoder passwordEncoder) {
        this.keycloakSession = keycloakSession;
        this.componentModel = componentModel;
        this.passwordEncoder = passwordEncoder;
        this.ecommerceUserDao = new EcommerceUserDao(componentModel);
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
        String encryptedPassword = ecommerceUserDao.getPasswordByUsername(userModel.getUsername());
        if (encryptedPassword == null)
            return false;
        return passwordEncoder.matches(credentialInput.getChallengeResponse(), encryptedPassword);
    }

    @Override
    public UserModel getUserById(RealmModel realmModel, String id) {
        log.info("getUserById({})", id);
        StorageId storageId = new StorageId(id);
        return getUserByUsername(realmModel, storageId.getExternalId());
    }

    @Override
    public UserModel getUserByUsername(RealmModel realmModel, String username) {
        log.info("getUserByUsername({})", username);
        EcommerceUser user = ecommerceUserDao.getUserByUsername(username);
        return user != null ? mapUser(realmModel, user) : null;
    }

    @Override
    public UserModel getUserByEmail(RealmModel realmModel, String email) {
        log.info("getUserByEmail({})", email);
        return getUserByUsername(realmModel, email);
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realmModel, Map<String, String> map, Integer integer, Integer integer1) {
        return ecommerceUserDao.getAllUsers()
                .stream()
                .map(user -> mapUser(realmModel, user));
    }

    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realmModel, GroupModel groupModel, Integer integer, Integer integer1) {
        return null;
    }

    @Override
    public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realmModel, String s, String s1) {
        return null;
    }

    private UserModel mapUser(RealmModel realm, EcommerceUser user) {
        return new EcommerceUserAdapter(keycloakSession, realm, componentModel, user);
    }
}
