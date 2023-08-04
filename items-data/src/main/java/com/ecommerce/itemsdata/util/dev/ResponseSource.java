package com.ecommerce.itemsdata.util.dev;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;

@RequiredArgsConstructor
public class ResponseSource {

    private final Environment environment;

    @Override
    public String toString(){
        String applicationName = environment.getProperty("spring.application.name");
        String port = environment.getProperty("server.port");
        return String.format("name=%s, port=%s", applicationName, port);
    }
}
