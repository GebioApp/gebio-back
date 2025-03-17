package io.gebio.gebioback.postgres.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import io.gebio.gebioback.domain.model.User;
import io.gebio.gebioback.domain.model.UserRole;
import io.gebio.gebioback.postgres.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PostgresUserAdapterIT extends AbstractPostgresIT {

  @Autowired
  private UserRepository userRepository;

  @Test
  void should_create_user() {
    PostgresUserAdapter postgresUserAdapter = new PostgresUserAdapter(
      userRepository
    );
    String email = "john.doe@gmail.com";
    String logo = "my-logo-url";

    String username = "john.doe";
    User user = new User(
      UUID.randomUUID(),
      email,
      logo,
      username,
      UserRole.USER
    );
    User savedUser = postgresUserAdapter.create(user);

    assertThat(savedUser).isNotNull();
    assertThat(savedUser.id()).isEqualTo(user.id());
    assertThat(savedUser.email()).hasToString(user.email());
    assertThat(savedUser.profileLogo()).hasToString(user.profileLogo());
    assertThat(savedUser.username()).hasToString(user.username());
    assertThat(savedUser.role()).isEqualTo(user.role());
  }

  @Test
  void should_read_user() {
    PostgresUserAdapter postgresUserAdapter = new PostgresUserAdapter(
      userRepository
    );
    String email = "john.doe@gmail.com";
    String logo = "my-logo-url";

    String username = "john.doe";
    User savedUser = new User(
      UUID.randomUUID(),
      email,
      logo,
      username,
      UserRole.USER
    );
    postgresUserAdapter.create(savedUser);

    Optional<User> optionalUser = postgresUserAdapter.findUserByEmail(email);

    assertThat(optionalUser).isNotEmpty();
    User user = optionalUser.get();
    assertThat(user.id()).isNotNull();
    assertThat(user.email()).hasToString(email);
    assertThat(user.username()).hasToString(username);
    assertThat(user.profileLogo()).hasToString(logo);
    assertThat(user.role()).isEqualTo(UserRole.USER);
  }
}
