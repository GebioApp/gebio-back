package io.gebio.gebioback.domain.port.out;

import io.gebio.gebioback.domain.service.Board;
import java.util.Optional;
import java.util.UUID;

public interface BoardRepositoryPort {
  Board createBoard(Board board);

  Optional<Board> findById(UUID boardId);
}
