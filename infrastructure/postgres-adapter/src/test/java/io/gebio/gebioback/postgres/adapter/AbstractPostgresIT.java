package io.gebio.gebioback.postgres.adapter;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@Testcontainers
@ContextConfiguration(
  loader = AnnotationConfigContextLoader.class,
  classes = PostgresITConfiguration.class
)
public class AbstractPostgresIT {

  @Container
  static PostgreSQLContainer postgresSQLContainer = new PostgreSQLContainer<>(
    "postgres:17"
  ).withDatabaseName("gebio-postgres-it");

  @DynamicPropertySource
  static void updateProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresSQLContainer::getJdbcUrl);
    registry.add(
      "spring.datasource.password",
      postgresSQLContainer::getPassword
    );
    registry.add(
      "spring.datasource.username",
      postgresSQLContainer::getUsername
    );
    registry.add("spring.liquibase.enabled", () -> true); // Ensure Liquibase is enabled
  }
}
