package io.gebio.gebioback.core.exception;

import java.util.UUID;

public class CardToUpdateNotPresentOnBoard extends RuntimeException {

  public CardToUpdateNotPresentOnBoard(UUID cardId) {
    super(String.format("Card with id %s was not found on board", cardId));
  }
}
