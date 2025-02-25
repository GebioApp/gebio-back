package io.gebio.gebioback.domain.service;

import io.gebio.gebioback.domain.model.User;
import io.gebio.gebioback.domain.port.in.BoardFacade;
import io.gebio.gebioback.domain.port.out.BoardRepositoryPort;
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
    return boardRepositoryPort.createBoard(
      new Board(UUID.randomUUID(), boardName, templateId, owner)
    );
  }
}
