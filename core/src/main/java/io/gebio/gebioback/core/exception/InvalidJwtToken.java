package io.gebio.gebioback.core.exception;

import org.springframework.stereotype.Component;

@Component
public class InvalidJwtToken extends RuntimeException {

  public InvalidJwtToken() {
    super("Authentication is missing or invalid");
  }
}
