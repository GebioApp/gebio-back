package service;

import model.User;
import org.springframework.stereotype.Service;
import port.in.UserFacade;
import port.out.UserRepositoryPort;

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
