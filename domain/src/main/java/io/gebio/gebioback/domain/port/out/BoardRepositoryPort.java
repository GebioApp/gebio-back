package io.gebio.gebioback.domain.port.out;

import io.gebio.gebioback.domain.model.Board;
import io.gebio.gebioback.domain.model.BoardCreationCommand;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoardRepositoryPort {
  Board create(BoardCreationCommand board);
  Board update(Board board);
  Optional<Board> findById(UUID boardId);
  List<Board> findAllForIdInMemberIds(UUID userId);
  void deleteById(UUID boardId);
}
