package io.gebio.gebioback.postgres.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import io.gebio.gebioback.domain.model.User;
import io.gebio.gebioback.postgres.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PostgresUserAdapterTest extends AbstractPostgresIT {

  @Autowired
  private UserRepository userRepository;

  @Test
  void should_create_user() {
    PostgresUserAdapter postgresUserAdapter = new PostgresUserAdapter(
      userRepository
    );
    String email = "john.doe@gmail.com";

    User user = postgresUserAdapter.createUserFromMail(email);

    assertThat(user).isNotNull();
    assertThat(user.id()).isNotNull();
    assertThat(user.email()).hasToString(email);
  }

  @Test
  void should_read_user() {
    PostgresUserAdapter postgresUserAdapter = new PostgresUserAdapter(
      userRepository
    );
    String email = "john.doe@gmail.com";
    postgresUserAdapter.createUserFromMail(email);

    Optional<User> optionalUser = postgresUserAdapter.findUserByEmail(email);

    assertThat(optionalUser).isNotEmpty();
    User user = optionalUser.get();
    assertThat(user.id()).isNotNull();
    assertThat(user.email()).hasToString(email);
  }
}
