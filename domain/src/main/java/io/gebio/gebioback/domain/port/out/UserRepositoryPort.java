package io.gebio.gebioback.domain.port.out;

import io.gebio.gebioback.domain.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
  Optional<User> findUserByEmail(String email);

  User createUserFromMail(String email, String logo);

  User create(User user);

  Optional<User> findById(UUID userId);
}
