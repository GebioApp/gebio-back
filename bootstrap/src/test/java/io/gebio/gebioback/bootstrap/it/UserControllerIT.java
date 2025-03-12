package io.gebio.gebioback.bootstrap.it;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.gebio.gebioback.domain.model.UserRole;
import io.gebio.gebioback.postgres.entity.UserEntity;
import io.gebio.gebioback.postgres.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class UserControllerIT extends AbstractGebioBackApiIT {

  @Autowired
  UserRepository userRepository;

  @BeforeEach
  void setup() {
    userRepository.deleteAll();
  }

  @Test
  void should_return_401_when_unauthenticated() throws Exception {
    mockMvc
      .perform(
        get(GET_CURRENT_USER_API_URL).contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isUnauthorized());
  }

  @Test
  void should_return_200_with_user_if_user_already_exists_in_database()
    throws Exception {
    UUID id = UUID.fromString("3338266c-26f2-4c85-8157-91f02b680577");
    UserEntity userEntity = new UserEntity(
      id,
      AUTHENTICATED_USER_EMAIL,
      AUTHENTICATED_USER_LOGO,
      AUTHENTICATED_USER_USERNAME,
      UserRole.USER.name()
    );
    userRepository.save(userEntity);

    mockMvc
      .perform(
        get(GET_CURRENT_USER_API_URL)
          .with(jwtToken())
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.user.id", equalTo(id.toString())))
      .andExpect(jsonPath("$.user.email", equalTo(AUTHENTICATED_USER_EMAIL)))
      .andExpect(jsonPath("$.user.logo", equalTo(AUTHENTICATED_USER_LOGO)))
      .andExpect(
        jsonPath("$.user.username", equalTo(AUTHENTICATED_USER_USERNAME))
      )
      .andExpect(jsonPath("$.user.role", equalTo("USER")));
  }

  @Test
  void should_return_200_and_create_user_if_user_does_not_exist_in_database()
    throws Exception {
    mockMvc
      .perform(
        get(GET_CURRENT_USER_API_URL)
          .with(jwtToken())
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.user.id", notNullValue()))
      .andExpect(jsonPath("$.user.email", equalTo(AUTHENTICATED_USER_EMAIL)))
      .andExpect(jsonPath("$.user.logo", equalTo(AUTHENTICATED_USER_LOGO)))
      .andExpect(jsonPath("$.user.role", equalTo("USER")));
  }

  @Test
  void should_return_201_and_create_guest_from_username() throws Exception {
    String body =
      """
      {
        "username": "iamatest"
      }
      """;
    mockMvc
      .perform(
        post(CREATE_GUEST_API_URL)
          .content(body)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.user.id", notNullValue()))
      .andExpect(jsonPath("$.user.email", nullValue()))
      .andExpect(jsonPath("$.user.logo", nullValue()))
      .andExpect(jsonPath("$.user.username", equalTo("iamatest")))
      .andExpect(jsonPath("$.user.role", equalTo("GUEST")));
  }
}
