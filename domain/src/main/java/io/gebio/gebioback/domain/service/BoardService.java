package io.gebio.gebioback.domain.service;

import io.gebio.gebioback.core.exception.BoardNotFound;
import io.gebio.gebioback.domain.model.Card;
import io.gebio.gebioback.domain.model.User;
import io.gebio.gebioback.domain.port.in.BoardFacade;
import io.gebio.gebioback.domain.port.out.BoardRepositoryPort;
import java.util.Collections;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BoardService implements BoardFacade {

  private final BoardRepositoryPort boardRepositoryPort;

  public BoardService(BoardRepositoryPort boardRepositoryPort) {
    this.boardRepositoryPort = boardRepositoryPort;
  }

  @Override
  public Board createWithOwner(UUID templateId, String boardName, User owner) {
    return boardRepositoryPort.save(
      new Board(
        UUID.randomUUID(),
        boardName,
        templateId,
        owner,
        Collections.emptyList()
      )
    );
  }

  @Override
  public Board findById(UUID boardId) {
    return boardRepositoryPort
      .findById(boardId)
      .orElseThrow(() -> new BoardNotFound(boardId));
  }

  @Override
  public Card addCardToBoard(UUID boardId, Card card) {
    return boardRepositoryPort.updateBoardWithNewCardForBoardId(boardId, card);
  }

  @Override
  public Board updateCardOnBoard(UUID boardId, Card card) {
    return boardRepositoryPort.updateBoardWithUpdatedCardForBoardId(
      boardId,
      card
    );
  }

  @Override
  public Board deleteCardFromBoard(UUID boardId, UUID cardId) {
    return boardRepositoryPort.deleteBoardWithDeletedCard(boardId, cardId);
  }
}
