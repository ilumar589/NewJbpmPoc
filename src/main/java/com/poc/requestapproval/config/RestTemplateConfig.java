package com.poc.requestapproval.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    private static final String JBPM_USER = "admin";
    private static final String JBPM_PASSWORD = "admin";

    @Bean
    public RestTemplate authenticatedRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(JBPM_USER, JBPM_PASSWORD));

        return restTemplate;
    }
}
