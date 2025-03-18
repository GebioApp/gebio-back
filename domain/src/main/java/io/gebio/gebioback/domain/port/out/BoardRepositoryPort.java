package io.gebio.gebioback.domain.port.out;

import io.gebio.gebioback.domain.model.Board;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoardRepositoryPort {
  Board save(Board board);
  Optional<Board> findById(UUID boardId);
  List<Board> findAllForIdInMemberIds(UUID userId);
}
