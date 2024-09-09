package com.bekrenovr.ecommerce.keycloakserver.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.services.util.ObjectMapperResolver;

import java.text.SimpleDateFormat;

public class CustomizedObjectMapperResolver extends ObjectMapperResolver {
    @Override
    public ObjectMapper getContext(Class<?> type) {
        ObjectMapper om = super.getContext(type);
        om.findAndRegisterModules();
        om.setDateFormat(new SimpleDateFormat());
        return om;
    }
}
