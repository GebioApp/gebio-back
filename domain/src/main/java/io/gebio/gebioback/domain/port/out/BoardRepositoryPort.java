package io.gebio.gebioback.domain.port.out;

import io.gebio.gebioback.domain.service.Board;

public interface BoardRepositoryPort {
  Board createBoard(Board board);
}
