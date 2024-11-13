package com.ecommerce.bekrenovr.authorizationserver.support;

import com.ecommerce.bekrenovr.authorizationserver.config.OAuth2LoginProviderProperties;
import com.ecommerce.bekrenovr.authorizationserver.config.OAuth2LoginProviderProperties.Providers;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Component
public class GoogleApiClient {
    private final OAuth2LoginProviderProperties.LoginProvider googleProperties;

    public GoogleApiClient(OAuth2LoginProviderProperties oauth2Properties) {
        this.googleProperties = oauth2Properties.get(Providers.GOOGLE.name());
    }

    public JSONObject getAccessTokenResponse(String grantCode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", grantCode);
        params.add("redirect_uri", googleProperties.getRedirectUri());
        params.add("client_id", googleProperties.getClientId());
        params.add("client_secret", googleProperties.getClientSecret());
        Arrays.stream(googleProperties.getScopes()).forEach(s -> params.add("scope", s));
        params.add("grant_type", googleProperties.getGrantType());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

        String url = "https://oauth2.googleapis.com/token";
        try {
            String response = restTemplate.postForObject(url, requestEntity, String.class);
            return new JSONObject(response);
        } catch(HttpClientErrorException.BadRequest ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public String refreshAccessToken(String refreshToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("refresh_token", refreshToken);
        params.add("client_id", googleProperties.getClientId());
        params.add("client_secret", googleProperties.getClientSecret());
        params.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

        String url = "https://oauth2.googleapis.com/token";
        try {
            return restTemplate.postForObject(url, requestEntity, String.class);
        } catch(HttpClientErrorException.BadRequest ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
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
