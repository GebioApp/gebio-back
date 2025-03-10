package io.gebio.gebioback.domain.port.in;

import io.gebio.gebioback.domain.model.Board;
import io.gebio.gebioback.domain.model.Card;
import java.util.UUID;

public interface CardFacade {
  Card addCardToBoard(UUID boardId, Card card);
  Card updateCardOnBoard(UUID boardId, Card card);
  Board deleteCardFromBoard(UUID boardId, UUID cardId);
}
