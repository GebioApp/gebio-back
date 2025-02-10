package io.gebio.gebioback.bootstrap.it;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.gebio.gebioback.postgres.entity.UserEntity;
import io.gebio.gebioback.postgres.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class UserControllerIntegrationTest extends AbstractGebioBackApiIT {

  @Autowired
  UserRepository userRepository;

  @Test
  void should_return_401_when_unauthenticated() throws Exception {
    mockMvc
      .perform(
        get(GET_CURRENT_USER_API_URL)
          .header("Authorization", "Bearer invalid-token")
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isUnauthorized());
  }

  @Test
  void should_return_200_with_user_if_user_already_exists_in_database()
    throws Exception {
    UUID id = UUID.fromString("3338266c-26f2-4c85-8157-91f02b680577");
    UserEntity userEntity = new UserEntity(id, EMAIL);
    userRepository.save(userEntity);

    mockMvc
      .perform(
        get(GET_CURRENT_USER_API_URL)
          .header("Authorization", "Bearer token")
          .with(jwtToken())
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.user.id", equalTo(id.toString())))
      .andExpect(jsonPath("$.user.email", equalTo(EMAIL)));
  }
}
