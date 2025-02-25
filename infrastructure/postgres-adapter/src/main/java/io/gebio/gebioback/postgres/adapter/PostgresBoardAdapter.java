package io.gebio.gebioback.postgres.adapter;

import io.gebio.gebioback.domain.port.out.BoardRepositoryPort;
import io.gebio.gebioback.domain.service.Board;
import io.gebio.gebioback.postgres.mapper.BoardMapper;
import io.gebio.gebioback.postgres.repository.BoardRepository;
import org.springframework.stereotype.Component;

@Component
public class PostgresBoardAdapter implements BoardRepositoryPort {

  private final BoardRepository boardRepository;

  public PostgresBoardAdapter(BoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  @Override
  public Board createBoard(Board board) {
    return BoardMapper.entityToDomain(
      boardRepository.save(BoardMapper.domainToEntity(board))
    );
  }
}
