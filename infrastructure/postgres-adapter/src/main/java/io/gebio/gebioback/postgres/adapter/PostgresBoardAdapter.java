package io.gebio.gebioback.postgres.adapter;

import io.gebio.gebioback.domain.port.out.BoardRepositoryPort;
import io.gebio.gebioback.domain.service.Board;
import io.gebio.gebioback.postgres.mapper.BoardMapper;
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
}
