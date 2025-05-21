package org.gs.incode.services.stringtransformation.persistance.jpa.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
    basePackages = "org.gs.incode.services.stringtransformation.persistance.jpa.repository")
@EntityScan(basePackages = "org.gs.incode.services.stringtransformation.persistance.jpa.entity")
public class JpaTestConfig {}
