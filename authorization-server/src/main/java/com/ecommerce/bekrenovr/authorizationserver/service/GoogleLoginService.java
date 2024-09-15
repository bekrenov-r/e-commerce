package com.ecommerce.bekrenovr.authorizationserver.service;

import com.ecommerce.bekrenovr.authorizationserver.config.OAuth2LoginProviderProperties;
import com.ecommerce.bekrenovr.authorizationserver.config.OAuth2LoginProviderProperties.Providers;
import com.ecommerce.bekrenovr.authorizationserver.dto.request.CustomerRequest;
import com.ecommerce.bekrenovr.authorizationserver.proxy.CustomerServiceProxy;
import com.ecommerce.bekrenovr.authorizationserver.util.GoogleApiClient;
import feign.FeignException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class GoogleLoginService {
    private final OAuth2LoginProviderProperties.LoginProvider googleProperties;
    private final CustomerServiceProxy customerServiceProxy;

    public GoogleLoginService(CustomerServiceProxy customerServiceProxy, OAuth2LoginProviderProperties loginProviderProperties) {
        this.customerServiceProxy = customerServiceProxy;
        this.googleProperties = loginProviderProperties.get(Providers.GOOGLE.name());
    }

    public String getAccessTokenGoogle(String code) {
        GoogleApiClient googleApiClient = new GoogleApiClient(googleProperties);
        JSONObject accessTokenResponse = googleApiClient.getAccessTokenResponse(code);
        String accessToken = accessTokenResponse.getString("access_token");
        JSONObject userInfo = googleApiClient.getGoogleUserInfo(accessToken);
        CustomerRequest customer = new CustomerRequest(
                userInfo.optString("given_name"),
                userInfo.optString("family_name"),
                userInfo.getString("email"),
                true
        );
        try {
            customerServiceProxy.createCustomer(customer);
        } catch(FeignException.Conflict ex){
            customerServiceProxy.updateCustomer(customer);
        }
        return accessTokenResponse.toString();
    }

    public String refreshAccessTokenGoogle(String refreshToken) {
        GoogleApiClient googleApiClient = new GoogleApiClient(googleProperties);
        return googleApiClient.refreshAccessToken(refreshToken);
    }
}
