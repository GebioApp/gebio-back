package io.gebio.gebioback.core.exception;

import java.util.UUID;

public class CardNotFound extends RuntimeException {

  public CardNotFound(UUID cardId) {
    super("Card with id %s was not found".formatted(cardId));
  }
}
