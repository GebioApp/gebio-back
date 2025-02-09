package io.gebio.gebioback.domain.port.in;

import io.gebio.gebioback.domain.model.User;

public interface UserFacade {
  User getOrCreateUserFromEmail(String email);
}
