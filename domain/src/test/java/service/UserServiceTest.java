package service;

import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import port.out.UserRepositoryPort;

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

    Assertions.assertThat(user).isEqualTo(existingUser);
  }
}
