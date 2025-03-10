package io.gebio.gebioback.domain.service;

import io.gebio.gebioback.domain.model.Card;
import io.gebio.gebioback.domain.port.in.CardFacade;
import io.gebio.gebioback.domain.port.out.BoardRepositoryPort;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CardService implements CardFacade {

  private final BoardRepositoryPort boardRepositoryPort;

  public CardService(BoardRepositoryPort boardRepositoryPort) {
    this.boardRepositoryPort = boardRepositoryPort;
  }

  @Override
  public Card addCardToBoard(UUID boardId, Card card) {
    return boardRepositoryPort.updateBoardWithNewCardForBoardId(boardId, card);
  }
}
