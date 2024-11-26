package com.bekrenovr.ecommerce.keycloakserver.providers.userstorage;


import com.bekrenovr.ecommerce.keycloakserver.repository.EcommerceUserRepository;
import com.bekrenovr.ecommerce.keycloakserver.util.EntityManagerFactoryHolder;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.models.utils.SHAPasswordEncoder;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.List;

import static com.bekrenovr.ecommerce.keycloakserver.providers.userstorage.EcommerceUserStorageProviderConstants.*;

@Slf4j
public class EcommerceUserStorageProviderFactory
        implements UserStorageProviderFactory<EcommerceUserStorageProvider> {
    public static final String COMPONENT_ID = "105762f9-a8eb-4a2b-a8b1-648b97b67194";
    public static final String PROVIDER_ID = "ecommerce-user-provider";
    protected List<ProviderConfigProperty> configMetadata;
    private final EntityManagerFactory entityManagerFactory;

    public EcommerceUserStorageProviderFactory() {
        log.info("Initializing EcommerceUserStorageProviderFactory...");
        this.entityManagerFactory = EntityManagerFactoryHolder.getEntityManagerFactory();
        this.initializeMetadata();
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public EcommerceUserStorageProvider create(KeycloakSession keycloakSession, ComponentModel model) {
        log.info("Creating EcommerceUserStorageProvider...");
        EcommerceUserRepository userRepository = new EcommerceUserRepository(entityManagerFactory.createEntityManager());
        return new EcommerceUserStorageProvider(keycloakSession, model, new SHAPasswordEncoder(256), userRepository);
    }

    @Override
    public EcommerceUserStorageProvider create(KeycloakSession keycloakSession) {
        log.info("Creating EcommerceUserStorageProvider...");
        ComponentModel model = KeycloakModelUtils.componentModelGetter("e-commerce", COMPONENT_ID)
                .apply(keycloakSession.getKeycloakSessionFactory());
        EcommerceUserRepository userRepository = new EcommerceUserRepository(entityManagerFactory.createEntityManager());
        return new EcommerceUserStorageProvider(keycloakSession, model, new SHAPasswordEncoder(256), userRepository);
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configMetadata;
    }

    private void initializeMetadata() {
        this.configMetadata = ProviderConfigurationBuilder.create()
                .property()
                .name(CONFIG_KEY_JDBC_DRIVER)
                .label("JDBC Driver Class")
                .type(ProviderConfigProperty.STRING_TYPE)
                .helpText("Fully qualified class name of the JDBC driver")
                .add()
                .property()
                .name(CONFIG_KEY_JDBC_URL)
                .label("JDBC URL")
                .type(ProviderConfigProperty.STRING_TYPE)
                .helpText("JDBC URL used to connect to the user database")
                .add()
                .property()
                .name(CONFIG_KEY_DB_USERNAME)
                .label("Database User")
                .type(ProviderConfigProperty.STRING_TYPE)
                .helpText("Username used to connect to the database")
                .add()
                .property()
                .name(CONFIG_KEY_DB_PASSWORD)
                .label("Database Password")
                .type(ProviderConfigProperty.STRING_TYPE)
                .helpText("Password used to connect to the database")
                .secret(true)
                .add()
                .property()
                .name(CONFIG_KEY_VALIDATION_QUERY)
                .label("SQL Validation Query")
                .type(ProviderConfigProperty.STRING_TYPE)
                .helpText("SQL query used to validate a connection")
                .add()
                .build();
    }
}
