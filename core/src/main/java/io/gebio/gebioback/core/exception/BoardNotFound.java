package io.gebio.gebioback.core.exception;

import java.util.UUID;

public class BoardNotFound extends RuntimeException {

  public BoardNotFound(UUID boardId) {
    super(String.format("Board with id %s was not found", boardId));
  }
}
