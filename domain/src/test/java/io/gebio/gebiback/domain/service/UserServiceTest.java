package io.gebio.gebiback.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.gebio.gebiback.domain.model.User;
import io.gebio.gebiback.domain.port.out.UserRepositoryPort;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  UserRepositoryPort userRepositoryPort;

  @InjectMocks
  UserService userService;

  @Test
  void should_return_user_for_already_created_user() {
    String email = "john.doe@gmail.com";
    UUID existingUserId = UUID.fromString(
      "e575b163-a4ae-41ff-a407-d004042248fb"
    );
    User existingUser = new User(existingUserId, email);
    when(userRepositoryPort.findUserByEmail(email)).thenReturn(
      Optional.of(existingUser)
    );

    User user = userService.getOrCreateUserFromEmail(email);

    assertThat(user).isEqualTo(existingUser);
  }

  @Test
  void should_create_user_if_user_does_not_exist() {
    String email = "john.doe@gmail.com";
    User createdUser = new User(UUID.randomUUID(), email);

    when(userRepositoryPort.findUserByEmail(email)).thenReturn(
      Optional.empty()
    );
    when(userRepositoryPort.createUserFromMail(email)).thenReturn(createdUser);
    User user = userService.getOrCreateUserFromEmail(email);

    verify(userRepositoryPort).createUserFromMail(email);
    assertThat(user).isEqualTo(createdUser);
  }
}
