package io.gebio.gebioback.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
  scanBasePackages = {
    "io.gebio.gebioback.rest.api.adapter",
    "io.gebio.gebioback.postgres",
    "io.gebio.gebioback.domain",
    "io.gebio.gebioback.bootstrap",
  }
)
@EnableJpaRepositories(
  basePackages = { "io.gebio.gebioback.postgres.repository" }
)
@EntityScan(basePackages = "io.gebio.gebioback.postgres.entity")
@EnableScheduling
public class GebioBackApplication {

  private static final Logger log = LoggerFactory.getLogger(
    GebioBackApplication.class
  );

  public static void main(String[] args) {
    SpringApplication.run(GebioBackApplication.class, args);
  }
}
