package io.gebio.gebioback.postgres.adapter;

import io.gebio.gebiback.domain.model.User;
import io.gebio.gebiback.domain.port.out.UserRepositoryPort;
import io.gebio.gebioback.postgres.mapper.UserMapper;
import io.gebio.gebioback.postgres.repository.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresUserAdapter implements UserRepositoryPort {

  private final UserRepository userRepository;

  public PostgresUserAdapter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Optional<User> findUserByEmail(String email) {
    return userRepository.findByEmail(email).map(UserMapper::entityToDomain);
  }

  @Override
  public User createUserFromMail(String email) {
    return null;
  }
}
