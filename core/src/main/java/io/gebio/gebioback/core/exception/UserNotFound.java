package io.gebio.gebioback.core.exception;

import java.util.UUID;

public class UserNotFound extends RuntimeException {

  public UserNotFound(UUID userId) {
    super("User with id %s was not found".formatted(userId));
  }
}
