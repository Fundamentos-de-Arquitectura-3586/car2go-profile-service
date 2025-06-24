package com.pe.platform.shared.infrastructure.persistence.jpa.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration for JPA Auditing
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {
}
