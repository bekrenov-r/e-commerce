package com.bekrenovr.ecommerce.common.util;


import com.bekrenovr.ecommerce.common.security.Role;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class TestUtil {
    public static String getResourceAsString(String path) {
        try(InputStream is = TestUtil.class.getResourceAsStream(path)){
            return new String(is.readAllBytes());
        } catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }

    public static String getAuthenticatedUserJSON(String username, Collection<Role> authorities) {
        String authoritiesStr = authorities.stream()
                .map(Role::name)
                .map(s -> "\"" + s + "\"")
                .collect(Collectors.joining(", "));
        return """
                {
                    "username": "%s",
                    "authorities": [%s]
                }""".formatted(username, authoritiesStr);
    }
}
