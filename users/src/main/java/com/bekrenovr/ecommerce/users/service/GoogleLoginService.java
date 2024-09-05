package com.bekrenovr.ecommerce.users.service;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.users.config.OAuth2LoginProviderProperties;
import com.bekrenovr.ecommerce.users.config.OAuth2LoginProviderProperties.Providers;
import com.bekrenovr.ecommerce.users.dto.request.CustomerRequest;
import com.bekrenovr.ecommerce.users.exception.UsersApplicationExceptionReason;
import com.bekrenovr.ecommerce.users.util.GoogleApiClient;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class GoogleLoginService {
    private final OAuth2LoginProviderProperties.LoginProvider googleProperties;
    private final CustomerService customerService;

    public GoogleLoginService(CustomerService customerService, OAuth2LoginProviderProperties loginProviderProperties) {
        this.customerService = customerService;
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
                userInfo.getString("email")
        );
        try {
            customerService.createCustomer(customer, true);
        } catch(EcommerceApplicationException ex){
            if(ex.getReason().equals(UsersApplicationExceptionReason.EMAIL_ALREADY_EXISTS)){
                customerService.updateCustomer(customer);
            } else throw ex;
        }
        return accessTokenResponse.toString();
    }

    public String refreshAccessTokenGoogle(String refreshToken) {
        GoogleApiClient googleApiClient = new GoogleApiClient(googleProperties);
        return googleApiClient.refreshAccessToken(refreshToken);
    }
}
