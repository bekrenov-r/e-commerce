package com.bekrenovr.ecommerce.users.service;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.users.dto.request.CustomerRegistrationRequest;
import com.bekrenovr.ecommerce.users.dto.request.CustomerRequest;
import com.bekrenovr.ecommerce.users.exception.UsersApplicationExceptionReason;
import com.bekrenovr.ecommerce.users.model.entity.Customer;
import com.bekrenovr.ecommerce.users.proxy.KeycloakProxy;
import com.bekrenovr.ecommerce.users.repository.CustomerRepository;
import com.bekrenovr.ecommerce.users.util.GoogleApiClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final KeycloakProxy keycloakProxy;
    private final MailService mailService;
    private final GoogleApiClient googleApiClient;

    @Transactional
    public void registerCustomerBasic(CustomerRegistrationRequest request) {
        customerService.createCustomer(request, true);
        ResponseEntity<String> response = keycloakProxy.createKeycloakUser(
                request.getEmail(), request.getPassword(), "CUSTOMER", request.getFirstName()
        );
        mailService.sendCustomerAccountActivationEmail(request, response.getBody());
    }

    public void resendActivationEmail(String email) {
        Customer customer = customerRepository.findByEmailOrThrowDefault(email);
        String activationToken = keycloakProxy.getActivationTokenForUser(customer.getEmail()).getBody();
        mailService.sendCustomerAccountActivationEmail(customer, activationToken);
    }

    public String registerCustomerGoogle(String code) {
        JSONObject accessTokenResponse = googleApiClient.getAccessTokenResponse(code);
        String accessToken = accessTokenResponse.getString("access_token");
        JSONObject userInfo = googleApiClient.getGoogleUserInfo(accessToken);
        CustomerRequest customer = new CustomerRequest(
                userInfo.getString("given_name"),
                userInfo.getString("family_name"),
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
}
