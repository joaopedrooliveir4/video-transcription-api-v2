package com.transcription.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.transcription.infra.persistence.repository")
@EntityScan("com.transcription.infra.persistence.entity")
public class JpaConfig {
}