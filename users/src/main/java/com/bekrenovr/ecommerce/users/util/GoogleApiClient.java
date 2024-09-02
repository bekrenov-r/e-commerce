package com.bekrenovr.ecommerce.users.util;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class GoogleApiClient {
    @Value("${spring.security.oauth2.resourceserver.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.resourceserver.registration.google.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.resourceserver.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.resourceserver.registration.google.scope}")
    private String[] scopes;

    public JSONObject getAccessTokenResponse(String grantCode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", grantCode);
        params.add("redirect_uri", redirectUri);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        Arrays.stream(scopes).forEach(s -> params.add("scope", s));
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

        String url = "https://oauth2.googleapis.com/token";
        String response = restTemplate.postForObject(url, requestEntity, String.class);
        System.out.println(response);

        return new JSONObject(response);
    }

    public JSONObject getGoogleUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        return new JSONObject(response.getBody());
    }
}
