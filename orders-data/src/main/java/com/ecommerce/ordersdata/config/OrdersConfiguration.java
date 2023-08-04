package com.ecommerce.ordersdata.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("orders-data")
public class OrdersConfiguration {

    private String max;

    public String getMax() {
        return max;
    }

    public void setMax(String maxTotalPrice) {
        this.max = maxTotalPrice;
    }
}
