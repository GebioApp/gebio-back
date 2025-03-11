package io.gebio.gebioback.domain.service;

import io.gebio.gebioback.domain.model.User;
import io.gebio.gebioback.domain.port.in.UserFacade;
import io.gebio.gebioback.domain.port.out.UserRepositoryPort;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserFacade {

  private final UserRepositoryPort userRepositoryPort;

  public UserService(UserRepositoryPort userRepositoryPort) {
    this.userRepositoryPort = userRepositoryPort;
  }

  @Override
  public User getOrCreateUserFromEmail(String email, String logo) {
    return userRepositoryPort
      .findUserByEmail(email)
      .orElseGet(() -> userRepositoryPort.createUserFromMail(email, logo));
  }

  @Override
  public User createGuest(String username) {
    return userRepositoryPort.create(
      new User(UUID.randomUUID(), null, null, username)
    );
  }
}
