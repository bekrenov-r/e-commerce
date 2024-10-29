package com.bekrenovr.ecommerce.customers.util;

import com.bekrenovr.ecommerce.common.util.TestUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomerJsonBuilder {
    private static final String JSON_RESOURCE_PATH = "/json/customer.json";

    private JSONObject json;

    private CustomerJsonBuilder() throws JSONException {
        this.json = new JSONObject(TestUtil.getResourceAsString(JSON_RESOURCE_PATH));
    }

    public static CustomerJsonBuilder create() throws JSONException {
        return new CustomerJsonBuilder();
    }

    public CustomerJsonBuilder firstName(String firstName) throws JSONException {
        json.put("firstName", firstName);
        return this;
    }

    public CustomerJsonBuilder lastName(String lastName) throws JSONException {
        json.put("lastName", lastName);
        return this;
    }

    public CustomerJsonBuilder email(String email) throws JSONException {
        json.put("email", email);
        return this;
    }

    public JSONObject build() {
        return json;
    }
}
