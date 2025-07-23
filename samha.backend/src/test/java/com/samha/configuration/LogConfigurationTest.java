package com.samha.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class LogConfigurationTest {

    @Bean
    AuditorAware<String> testAuditorProvider() {
        return new AuditorAwareImplTest();
    }

}