package com.bekrenovr.ecommerce.orders.util;

import com.bekrenovr.ecommerce.common.util.TestUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;
import java.util.function.Consumer;

public class OrderJsonBuilder {
    private static final String JSON_RESOURCE_PATH = "/json/order.json";
    private JSONObject order;

    private OrderJsonBuilder() throws JSONException {
        String template = TestUtil.getResourceAsString(JSON_RESOURCE_PATH);
        this.order = new JSONObject(template);
    }

    public static OrderJsonBuilder create() throws JSONException {
        return new OrderJsonBuilder();
    }

    public OrderJsonBuilder itemEntry(UUID itemId, String size, int quantity) throws JSONException {
        JSONObject entryJson = new JSONObject()
                .put("itemId", itemId)
                .put("size", size)
                .put("quantity", quantity);
        order.getJSONArray("itemEntries").put(entryJson);
        return this;
    }

    public OrderJsonBuilder customer(Consumer<CustomerJsonBuilder> customizer) throws JSONException {
        var customerBuilder = CustomerJsonBuilder.create(this);
        customizer.accept(customerBuilder);
        return customerBuilder.build();
    }

    public JSONObject build() {
        return order;
    }

    public static class CustomerJsonBuilder {
        private static final String JSON_RESOURCE_PATH = "/json/customer.json";
        private JSONObject customer;
        private OrderJsonBuilder orderJsonBuilder;

        private CustomerJsonBuilder(OrderJsonBuilder orderJsonBuilder) throws JSONException {
            String template = TestUtil.getResourceAsString(JSON_RESOURCE_PATH);
            this.customer = new JSONObject(template);
            this.orderJsonBuilder = orderJsonBuilder;
        }

        public static CustomerJsonBuilder create(OrderJsonBuilder orderJsonBuilder) throws JSONException {
            return new CustomerJsonBuilder(orderJsonBuilder);
        }

        public CustomerJsonBuilder firstName(String firstName) {
            try {
                customer.put("firstName", firstName);
            } catch(JSONException ex) {
                throw new RuntimeException(ex);
            }
            return this;
        }

        public CustomerJsonBuilder lastName(String lastName) {
            try {
                customer.put("lastName", lastName);
            } catch(JSONException ex) {
                throw new RuntimeException(ex);
            }
            return this;
        }

        public CustomerJsonBuilder email(String email) {
            try {
                customer.put("email", email);
            } catch(JSONException ex) {
                throw new RuntimeException(ex);
            }
            return this;
        }

        public OrderJsonBuilder build() throws JSONException {
            orderJsonBuilder.order.put("customer", this.customer);
            return orderJsonBuilder;
        }
    }
}
