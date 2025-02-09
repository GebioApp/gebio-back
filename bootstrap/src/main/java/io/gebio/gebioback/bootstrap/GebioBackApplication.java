package io.gebio.gebioback.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
  scanBasePackages = {
    "io.gebio.gebioback.rest.api.adapter",
    "io.gebio.gebioback.postgres",
    "io.gebio.gebioback.domain",
  }
)
@EnableJpaRepositories(
  basePackages = { "io.gebio.gebioback.postgres.repository" }
)
@EntityScan(basePackages = "io.gebio.gebioback.postgres.entity")
public class GebioBackApplication {

  public static void main(String[] args) {
    SpringApplication.run(GebioBackApplication.class, args);
  }
}
