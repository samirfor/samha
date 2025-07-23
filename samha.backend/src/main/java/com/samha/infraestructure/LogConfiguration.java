package com.samha.infraestructure;

import com.samha.persistence.log.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
@EnableTransactionManagement
public class LogConfiguration {

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

}
