package com.goodquestion.edutrek_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
