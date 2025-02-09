package port.out;

import java.util.Optional;
import model.User;

public interface UserRepositoryPort {
  Optional<User> findUserByEmail(String email);

  User createUserFromMail(String email);
}
