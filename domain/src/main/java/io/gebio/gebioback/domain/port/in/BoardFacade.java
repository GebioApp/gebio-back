package io.gebio.gebioback.domain.port.in;

import io.gebio.gebioback.domain.model.User;
import io.gebio.gebioback.domain.service.Board;
import java.util.UUID;

public interface BoardFacade {
  Board createWithOwner(UUID templateId, String boardName, User owner);
}
