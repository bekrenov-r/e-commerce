package com.ecommerce.bekrenovr.authorizationserver.service;

import com.ecommerce.bekrenovr.authorizationserver.dto.request.CustomerRequest;
import com.ecommerce.bekrenovr.authorizationserver.proxy.CustomerServiceProxy;
import com.ecommerce.bekrenovr.authorizationserver.util.GoogleApiClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {
    private final CustomerServiceProxy customerServiceProxy;
    private final GoogleApiClient googleApiClient;

    public String getAccessTokenGoogle(String code) {
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
            // do nothing because CONFLICT means that user is already created
        }
        return accessTokenResponse.toString();
    }

    public String refreshAccessTokenGoogle(String refreshToken) {
        return googleApiClient.refreshAccessToken(refreshToken);
    }
}
