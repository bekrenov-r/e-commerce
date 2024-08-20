package com.bekrenovr.ecommerce.common.util.auth;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AuthenticationUtil {
    private static final String AUTHENTICATED_USER = "Authenticated-User";
    public static boolean requestHasAuthentication() {
        String authenticatedUserHeader = getCurrentRequest().getHeader(AUTHENTICATED_USER);
        return authenticatedUserHeader != null;
    }

    public static AuthenticatedUser getAuthenticatedUser() {
        if(!requestHasAuthentication()){
            throw new IllegalStateException("There is no authenticated user in request context");
        }
        String authenticatedUserJson = getCurrentRequest().getHeader(AUTHENTICATED_USER);
        DocumentContext json = JsonPath.parse(authenticatedUserJson);
        return new AuthenticatedUser(json.read("$.username"), json.read("$.authorities"));
    }

    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
