package com.ecommerce.bekrenovr.authorizationserver.util;

import com.bekrenovr.ecommerce.common.util.TestUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomerRegistrationRequestJsonBuilder {
    private static final String JSON_RESOURCE_PATH = "/json/customer-registration.json";
    private JSONObject json;

    private CustomerRegistrationRequestJsonBuilder() throws JSONException {
        String template = TestUtil.getResourceAsString(JSON_RESOURCE_PATH);
        this.json = new JSONObject(template);
    }

    public static CustomerRegistrationRequestJsonBuilder create() throws JSONException {
        return new CustomerRegistrationRequestJsonBuilder();
    }

    public CustomerRegistrationRequestJsonBuilder firstName(String firstName) throws JSONException {
        json.put("firstName", firstName);
        return this;
    }

    public CustomerRegistrationRequestJsonBuilder lastName(String lastName) throws JSONException {
        json.put("lastName", lastName);
        return this;
    }

    public CustomerRegistrationRequestJsonBuilder email(String email) throws JSONException {
        json.put("email", email);
        return this;
    }

    public CustomerRegistrationRequestJsonBuilder password(String password) throws JSONException {
        json.put("password", password);
        return this;
    }

    public JSONObject build() {
        return json;
    }
}
