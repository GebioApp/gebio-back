package io.gebio.gebioback.domain.service;

import io.gebio.gebioback.core.exception.BoardNotFound;
import io.gebio.gebioback.core.exception.UserNotFound;
import io.gebio.gebioback.domain.model.*;
import io.gebio.gebioback.domain.port.in.BoardFacade;
import io.gebio.gebioback.domain.port.out.BoardRepositoryPort;
import io.gebio.gebioback.domain.port.out.UserRepositoryPort;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BoardService implements BoardFacade {

  private final BoardRepositoryPort boardRepositoryPort;
  private final UserRepositoryPort userRepositoryPort;

  public BoardService(
    BoardRepositoryPort boardRepositoryPort,
    UserRepositoryPort userRepositoryPort
  ) {
    this.boardRepositoryPort = boardRepositoryPort;
    this.userRepositoryPort = userRepositoryPort;
  }

  @Override
  public Board createWithOwner(UUID templateId, String boardName, User owner) {
    return boardRepositoryPort.create(
      new BoardCreationCommand(
        UUID.randomUUID(),
        boardName,
        templateId,
        owner,
        Collections.emptyList(),
        List.of(owner)
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
  public Board addUserToBoard(UUID boardId, UUID userId) {
    Board board = boardRepositoryPort
      .findById(boardId)
      .orElseThrow(() -> new BoardNotFound(boardId));
    User user = userRepositoryPort
      .findById(userId)
      .orElseThrow(() -> new UserNotFound(userId));
    return boardRepositoryPort.update(board.addUser(user));
  }

  @Override
  public List<Board> findAllUserJoinedBoards(UUID userId) {
    return boardRepositoryPort.findAllForIdInMemberIds(userId);
  }

  @Override
  public Board updateBoard(BoardUpdateCommand updateBoardCommand) {
    Board board = boardRepositoryPort
      .findById(updateBoardCommand.boardId())
      .orElseThrow(() -> new BoardNotFound(updateBoardCommand.boardId()));
    board.checkBoardOwner(updateBoardCommand.userId());
    Board updateBoard = board.updateName(updateBoardCommand.name());
    return boardRepositoryPort.update(updateBoard);
  }

  @Override
  public Board deleteBoard(BoardDeleteCommand deleteBoardCommand) {
    Board board = boardRepositoryPort
      .findById(deleteBoardCommand.boardId())
      .orElseThrow(() -> new BoardNotFound(deleteBoardCommand.boardId()));
    board.checkBoardOwner(deleteBoardCommand.userId());
    return null;
  }
}
