package com.ecommerce.bekrenovr.authorizationserver.integration;

import com.ecommerce.bekrenovr.authorizationserver.service.MailService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

public abstract class BaseIT {
    TestRestTemplate restTemplate;

    @MockBean
    MailService mailService;

    BaseIT(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
