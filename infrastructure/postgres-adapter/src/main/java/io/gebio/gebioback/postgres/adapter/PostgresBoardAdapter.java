package io.gebio.gebioback.postgres.adapter;

import io.gebio.gebioback.core.exception.BoardNotFound;
import io.gebio.gebioback.domain.model.Card;
import io.gebio.gebioback.domain.port.out.BoardRepositoryPort;
import io.gebio.gebioback.domain.service.Board;
import io.gebio.gebioback.postgres.entity.BoardEntity;
import io.gebio.gebioback.postgres.mapper.BoardMapper;
import io.gebio.gebioback.postgres.mapper.CardMapper;
import io.gebio.gebioback.postgres.repository.BoardRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class PostgresBoardAdapter implements BoardRepositoryPort {

  private final BoardRepository boardRepository;

  public PostgresBoardAdapter(BoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  @Override
  public Board save(Board board) {
    return BoardMapper.entityToDomain(
      boardRepository.save(BoardMapper.domainToEntity(board))
    );
  }

  @Override
  public Optional<Board> findById(UUID boardId) {
    return boardRepository.findById(boardId).map(BoardMapper::entityToDomain);
  }

  @Override
  public Board updateBoardWithNewCardForBoardId(UUID boardId, Card card) {
    BoardEntity boardEntity = boardRepository
      .findById(boardId)
      .orElseThrow(() -> new BoardNotFound(boardId));
    boardEntity.addCard(CardMapper.fromDomainToEntity(card, boardEntity));
    return BoardMapper.entityToDomain(boardRepository.save(boardEntity));
  }

  @Override
  public Board updateBoardWithUpdatedCardForBoardId(UUID boardId, Card card) {
    BoardEntity boardEntity = boardRepository
      .findById(boardId)
      .orElseThrow(() -> new BoardNotFound(boardId));
    boardEntity.updateCard(CardMapper.fromDomainToEntity(card, boardEntity));
    return BoardMapper.entityToDomain(boardRepository.save(boardEntity));
  }
}
