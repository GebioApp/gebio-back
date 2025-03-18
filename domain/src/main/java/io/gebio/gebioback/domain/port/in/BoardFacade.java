package io.gebio.gebioback.domain.port.in;

import io.gebio.gebioback.domain.model.Board;
import io.gebio.gebioback.domain.model.User;
import java.util.List;
import java.util.UUID;

public interface BoardFacade {
  Board createWithOwner(UUID templateId, String boardName, User owner);
  Board findById(UUID boardId);
  Board addUserToBoard(UUID boardId, UUID userId);
  List<Board> findAllUserJoinedBoards(UUID userId);
}
