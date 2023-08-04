package com.ecommerce.ordersdata.config;

import com.ecommerce.common.ResponseSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ExceptionHandlingConfig {
    @Bean
    public ResponseSource responseSource(Environment environment){
        return new ResponseSource(environment);
    }

}
