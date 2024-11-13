package com.ecommerce.bekrenovr.authorizationserver.login;

import com.ecommerce.bekrenovr.authorizationserver.feign.CustomersProxy;
import com.ecommerce.bekrenovr.authorizationserver.registration.CustomerRequest;
import com.ecommerce.bekrenovr.authorizationserver.support.GoogleApiClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {
    private final CustomersProxy customersProxy;
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
            customersProxy.createCustomer(customer);
        } catch(FeignException.Conflict ex){
            // do nothing because CONFLICT means that user is already created
        }
        return accessTokenResponse.toString();
    }

    public String refreshAccessTokenGoogle(String refreshToken) {
        return googleApiClient.refreshAccessToken(refreshToken);
    }
}
