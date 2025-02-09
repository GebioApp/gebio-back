package io.gebio.gebiback.domain.port.out;

import io.gebio.gebiback.domain.model.User;
import java.util.Optional;

public interface UserRepositoryPort {
  Optional<User> findUserByEmail(String email);

  User createUserFromMail(String email);
}
