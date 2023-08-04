package com.ecommerce.itemsdata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.ecommerce.itemsdata.util.dev.ResponseSource;

@Configuration
public class ExceptionHandlingConfig {

    @Bean
    public ResponseSource responseSource(Environment environment){
        return new ResponseSource(environment);
    }

}
