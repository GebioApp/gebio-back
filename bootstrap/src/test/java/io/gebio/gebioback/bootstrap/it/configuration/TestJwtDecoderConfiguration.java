package io.gebio.gebioback.bootstrap.it.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@TestConfiguration
public class TestJwtDecoderConfiguration {

  @Bean
  public JwtDecoder jwtDecoder() {
    return token -> Jwt.withTokenValue(token).build();
  }
}
