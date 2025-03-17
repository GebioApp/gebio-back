package io.gebio.gebioback.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.gebio.gebioback.domain.model.User;
import io.gebio.gebioback.domain.model.UserRole;
import io.gebio.gebioback.domain.port.out.UserRepositoryPort;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    String existingUserEmail = "john.doe@gmail.com";
    String existingUserLogo = "my-logo-url";
    UUID existingUserId = UUID.fromString(
      "e575b163-a4ae-41ff-a407-d004042248fb"
    );
    User existingUser = new User(
      existingUserId,
      existingUserEmail,
      null,
      null,
      UserRole.USER
    );
    when(userRepositoryPort.findUserByEmail(existingUserEmail)).thenReturn(
      Optional.of(existingUser)
    );

    User user = userService.getOrCreateUserFromEmail(
      existingUserEmail,
      existingUserLogo
    );

    assertThat(user).isEqualTo(existingUser);
  }

  @Test
  void should_create_user_if_user_does_not_exist() {
    ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
    String email = "john.doe@gmail.com";
    String logo = "my-logo-url";
    User createdUser = new User(
      UUID.randomUUID(),
      email,
      logo,
      null,
      UserRole.USER
    );

    when(userRepositoryPort.findUserByEmail(email)).thenReturn(
      Optional.empty()
    );
    when(userRepositoryPort.create(any(User.class))).thenReturn(createdUser);

    userService.getOrCreateUserFromEmail(email, logo);

    verify(userRepositoryPort).create(argumentCaptor.capture());
    User savedUser = argumentCaptor.getValue();
    assertThat(savedUser.id()).isNotNull();
    assertThat(savedUser.email()).isEqualTo(email);
    assertThat(savedUser.profileLogo()).isEqualTo(logo);
    assertThat(savedUser.username()).isNull();
    assertThat(savedUser.role()).isEqualTo(UserRole.USER);
  }
}
