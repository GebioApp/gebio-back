package io.gebio.gebioback.domain.service;

import io.gebio.gebioback.domain.model.Board;
import io.gebio.gebioback.domain.model.Card;
import io.gebio.gebioback.domain.port.in.CardFacade;
import io.gebio.gebioback.domain.port.out.CardRepositoryPort;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CardService implements CardFacade {

  private final CardRepositoryPort cardRepositoryPort;

  public CardService(CardRepositoryPort cardRepositoryPort) {
    this.cardRepositoryPort = cardRepositoryPort;
  }

  @Override
  public Card addCardToBoard(UUID boardId, Card card) {
    return cardRepositoryPort.addCardToBoard(boardId, card);
  }

  @Override
  public Board updateCardOnBoard(UUID boardId, Card card) {
    return cardRepositoryPort.updateBoardWithUpdatedCardForBoardId(
      boardId,
      card
    );
  }
}
