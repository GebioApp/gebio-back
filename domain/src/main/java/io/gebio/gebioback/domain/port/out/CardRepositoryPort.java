package io.gebio.gebioback.domain.port.out;

import io.gebio.gebioback.domain.model.Board;
import io.gebio.gebioback.domain.model.Card;
import java.util.UUID;

public interface CardRepositoryPort {
  Card addCardToBoard(UUID boardId, Card card);
  Board updateBoardWithUpdatedCardForBoardId(UUID boardId, Card card);
}
