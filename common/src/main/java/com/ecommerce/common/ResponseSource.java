package com.ecommerce.common;

import org.springframework.core.env.Environment;

public record ResponseSource(
        Environment environment
) {

    @Override
    public String toString(){
        String applicationName = environment.getProperty("spring.application.name");
        String port = environment.getProperty("server.port");
        return String.format("name=%s, port=%s", applicationName, port);
    }
}
