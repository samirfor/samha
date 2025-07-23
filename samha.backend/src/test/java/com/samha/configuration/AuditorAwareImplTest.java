package com.samha.configuration;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImplTest implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("TEST_AUDITOR");
    }
}