package io.gebio.gebiback.domain.port.in;

import io.gebio.gebiback.domain.model.User;

public interface UserFacade {
  User getOrCreateUserFromEmail(String email);
}
