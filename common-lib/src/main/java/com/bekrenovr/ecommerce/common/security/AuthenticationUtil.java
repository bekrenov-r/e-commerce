package com.bekrenovr.ecommerce.common.security;

import com.bekrenovr.ecommerce.common.util.RequestUtil;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class AuthenticationUtil {
    public static boolean requestHasAuthentication() {
        String authenticatedUserHeader = RequestUtil.getCurrentRequest().getHeader(SecurityConstants.AUTHENTICATED_USER_HEADER);
        return authenticatedUserHeader != null;
    }

    public static AuthenticatedUser getAuthenticatedUser() {
        if(!requestHasAuthentication()){
            throw new IllegalStateException("There is no authenticated user in request context");
        }
        String authenticatedUserJson = RequestUtil.getCurrentRequest().getHeader(SecurityConstants.AUTHENTICATED_USER_HEADER);
        DocumentContext json = JsonPath.parse(authenticatedUserJson);
        return new AuthenticatedUser(json.read("$.username"), json.read("$.authorities"));
    }
}
