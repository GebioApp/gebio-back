package io.gebio.gebioback.domain.port.in;

import io.gebio.gebioback.domain.model.Card;
import java.util.UUID;

public interface CardFacade {
  Card addCardToBoard(UUID boardId, Card card);
  Card updateCardOnBoard(Card card);
  Card deleteCardFromBoard(UUID cardId);
}
