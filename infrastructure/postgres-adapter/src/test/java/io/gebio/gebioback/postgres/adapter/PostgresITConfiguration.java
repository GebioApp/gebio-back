package io.gebio.gebioback.postgres.adapter;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(
  basePackages = { "io.gebio.gebioback.postgres.repository" }
)
@EntityScan(basePackages = { "io.gebio.gebioback.postgres.entity" })
public class PostgresITConfiguration {}
