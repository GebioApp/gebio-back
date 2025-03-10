package io.gebio.gebioback.domain.port.out;

import io.gebio.gebioback.domain.model.Board;
import io.gebio.gebioback.domain.model.Card;
import java.util.Optional;
import java.util.UUID;

public interface BoardRepositoryPort {
  Board save(Board board);

  Optional<Board> findById(UUID boardId);

  Card updateBoardWithNewCardForBoardId(UUID boardId, Card card);

  Board updateBoardWithUpdatedCardForBoardId(UUID boardId, Card card);

  Board deleteBoardWithDeletedCard(UUID boardId, UUID cardId);
}
