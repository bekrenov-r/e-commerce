package com.bekrenovr.ecommerce.orders.config;

import com.bekrenovr.ecommerce.catalog.model.SizeFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Value("${custom.strategy.shoes-size.min}")
    private int shoesSizeMin;

    @Value("${custom.strategy.shoes-size.max}")
    private int shoesSizeMax;

    @Bean
    public SizeFactory sizeFactory(){
        return new SizeFactory(shoesSizeMin, shoesSizeMax);
    }
}
