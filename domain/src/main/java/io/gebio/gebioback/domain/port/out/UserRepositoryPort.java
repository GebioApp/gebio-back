package io.gebio.gebioback.domain.port.out;

import io.gebio.gebioback.domain.model.User;
import java.util.Optional;

public interface UserRepositoryPort {
  Optional<User> findUserByEmail(String email);

  User createUserFromMail(String email);
}
