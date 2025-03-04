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
  public Board addCardToBoard(UUID boardId, Card card) {
    return boardRepositoryPort
      .findById(boardId)
      .map(board -> Board.addCardToBoard(board, card))
      .map(boardRepositoryPort::save)
      .orElseThrow(() -> new BoardNotFound(boardId));
  }
}
