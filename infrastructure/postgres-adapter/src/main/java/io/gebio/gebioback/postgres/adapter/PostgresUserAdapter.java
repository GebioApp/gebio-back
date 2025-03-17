package io.gebio.gebioback.postgres.adapter;

import io.gebio.gebioback.domain.model.User;
import io.gebio.gebioback.domain.port.out.UserRepositoryPort;
import io.gebio.gebioback.postgres.mapper.UserMapper;
import io.gebio.gebioback.postgres.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
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
  public User create(User user) {
    return UserMapper.entityToDomain(
      userRepository.save(UserMapper.domainToEntity(user))
    );
  }

  @Override
  public Optional<User> findById(UUID userId) {
    return userRepository.findById(userId).map(UserMapper::entityToDomain);
  }
}
