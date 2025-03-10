package io.gebio.gebioback.domain.port.out;

import io.gebio.gebioback.domain.model.Card;
import java.util.UUID;

public interface CardRepositoryPort {
  Card addCardToBoard(UUID boardId, Card card);
  Card updateCardOnBoard(UUID boardId, Card card);
}
