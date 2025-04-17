package io.gebio.gebioback.core.exception;

import java.util.UUID;

public class UserIsNotOwnerOfBoard extends RuntimeException {

  public UserIsNotOwnerOfBoard(UUID boardId, UUID userId) {
    super(String.format("User %s is not owner of board %s", userId, boardId));
  }
}
