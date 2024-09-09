package com.bekrenovr.ecommerce.keycloakserver.config;

import com.bekrenovr.ecommerce.keycloakserver.config.properties.KeycloakServerProperties;
import com.bekrenovr.ecommerce.keycloakserver.exception.EcommerceApplicationExceptionMapper;
import com.bekrenovr.ecommerce.keycloakserver.providers.RegularJsonConfigProviderFactory;
import com.bekrenovr.ecommerce.keycloakserver.util.CustomizedObjectMapperResolver;
import org.keycloak.Config;
import org.keycloak.exportimport.ExportImportManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.services.managers.ApplianceBootstrap;
import org.keycloak.services.managers.RealmManager;
import org.keycloak.services.resources.KeycloakApplication;
import org.keycloak.services.util.JsonConfigProviderFactory;
import org.keycloak.services.util.ObjectMapperResolver;
import org.keycloak.util.JsonSerialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class EmbeddedKeycloakApplication extends KeycloakApplication {
    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedKeycloakApplication.class);

    static KeycloakServerProperties keycloakServerProperties;

    public EmbeddedKeycloakApplication() {
        this.classes.add(EcommerceApplicationExceptionMapper.class);
        this.customizeObjectMapperResolver();
    }

    protected void loadConfig() {
        JsonConfigProviderFactory factory = new RegularJsonConfigProviderFactory();
        Config.init(factory.create()
                .orElseThrow(() -> new NoSuchElementException("No value present")));
    }

    @Override
    protected ExportImportManager bootstrap() {
        final ExportImportManager exportImportManager = super.bootstrap();
        createMasterRealmAdminUser();
        createEcommerceRealm();
        return exportImportManager;
    }

    private void createMasterRealmAdminUser() {

        KeycloakSession session = getSessionFactory().create();

        ApplianceBootstrap applianceBootstrap = new ApplianceBootstrap(session);

        KeycloakServerProperties.AdminUser admin = keycloakServerProperties.getAdminUser();

        try {
            session.getTransactionManager().begin();
            applianceBootstrap.createMasterRealmUser(admin.getUsername(), admin.getPassword());
            session.getTransactionManager().commit();
        } catch (Exception ex) {
            LOG.warn("Couldn't create keycloak master admin user: {}", ex.getMessage());
            session.getTransactionManager().rollback();
        }

        session.close();
    }

    private void createEcommerceRealm() {
        KeycloakSession session = getSessionFactory().create();

        try {
            session.getTransactionManager().begin();

            RealmManager manager = new RealmManager(session);
            Resource lessonRealmImportFile = new ClassPathResource(keycloakServerProperties.getRealmImportFile());

            manager.importRealm(
                    JsonSerialization.readValue(lessonRealmImportFile.getInputStream(), RealmRepresentation.class));

            session.getTransactionManager().commit();
        } catch (Exception ex) {
            LOG.warn("Failed to import Realm json file: {}", ex.getMessage());
            session.getTransactionManager().rollback();
        }

        session.close();
    }

    private void customizeObjectMapperResolver() {
        Collection<Object> objectMappers = this.singletons.stream()
                .filter(o -> o instanceof ObjectMapperResolver)
                .collect(Collectors.toSet());
        this.singletons.removeAll(objectMappers);
        this.singletons.add(new CustomizedObjectMapperResolver());
    }
}
