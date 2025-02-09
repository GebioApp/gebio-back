package io.gebio.gebiback.domain.service;

import io.gebio.gebiback.domain.model.User;
import io.gebio.gebiback.domain.port.in.UserFacade;
import io.gebio.gebiback.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserFacade {

  private final UserRepositoryPort userRepositoryPort;

  public UserService(UserRepositoryPort userRepositoryPort) {
    this.userRepositoryPort = userRepositoryPort;
  }

  @Override
  public User getOrCreateUserFromEmail(String email) {
    return userRepositoryPort
      .findUserByEmail(email)
      .orElseGet(() -> userRepositoryPort.createUserFromMail(email));
  }
}
