package io.gebio.gebioback.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
  scanBasePackages = { "io.gebio.gebioback.rest.api.adapter" }
)
public class GebioBackApplication {

  public static void main(String[] args) {
    SpringApplication.run(GebioBackApplication.class, args);
  }
}
