package io.gebio.gebioback.domain.port.out;

import io.gebio.gebioback.domain.model.Card;
import java.util.UUID;

public interface CardRepositoryPort {
  Card updateBoardWithNewCardForBoardId(UUID boardId, Card card);
}
