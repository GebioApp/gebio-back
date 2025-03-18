package io.gebio.gebioback.bootstrap.it;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.gebio.gebioback.domain.model.UserRole;
import io.gebio.gebioback.postgres.entity.UserEntity;
import io.gebio.gebioback.postgres.repository.BoardRepository;
import io.gebio.gebioback.postgres.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserControllerIT extends AbstractGebioBackApiIT {

  @Autowired
  UserRepository userRepository;

  @Autowired
  BoardRepository boardRepository;

  @BeforeEach
  void setup() {
    userRepository.deleteAll();
    boardRepository.deleteAll();
  }

  @Test
  void should_return_401_when_unauthenticated() throws Exception {
    doGetWithoutToken(GET_CURRENT_USER_API_URL).andExpect(
      status().isUnauthorized()
    );
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

    doGetWithToken(GET_CURRENT_USER_API_URL)
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
    doGetWithToken(GET_CURRENT_USER_API_URL)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.user.id", notNullValue()))
      .andExpect(jsonPath("$.user.email", equalTo(AUTHENTICATED_USER_EMAIL)))
      .andExpect(jsonPath("$.user.logo", equalTo(AUTHENTICATED_USER_LOGO)))
      .andExpect(jsonPath("$.user.role", equalTo("USER")));
  }

  @Test
  void should_return_201_and_create_guest_from_username() throws Exception {
    doPostWithoutToken(
      CREATE_GUEST_API_URL,
      """
      {
        "username": "iamatest"
      }
      """
    )
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.user.id", notNullValue()))
      .andExpect(jsonPath("$.user.email", nullValue()))
      .andExpect(
        jsonPath(
          "$.user.logo",
          equalTo("https://www.svgrepo.com/show/13656/user.svg")
        )
      )
      .andExpect(jsonPath("$.user.username", equalTo("iamatest")))
      .andExpect(jsonPath("$.user.role", equalTo("GUEST")));
  }

  @Nested
  class FindBoards {

    @Test
    void should_return_401_for_unauthenticated_user() throws Exception {
      UUID userId = UUID.randomUUID();
      doGetWithoutToken(FIND_USER_BOARDS_API_URL.formatted(userId)).andExpect(
        status().isUnauthorized()
      );
    }

    @Test
    void should_return_201_and_return_boards_from_which_user_is_a_member()
      throws Exception {
      UUID userId = UUID.fromString("3338266c-26f2-4c85-8157-91f02b680577");
      UserEntity userEntity = new UserEntity(
        userId,
        AUTHENTICATED_USER_EMAIL,
        AUTHENTICATED_USER_LOGO,
        AUTHENTICATED_USER_USERNAME,
        UserRole.USER.name()
      );
      userRepository.save(userEntity);
      doPostWithToken(
        CREATE_BOARD_API_URL,
        """
        {
                  "title": "Retrospective du 25 février",
                  "templateId": "b65687d3-4edc-4492-857d-3f22705ca7fd"
                }
        """
      ).andExpect(status().isCreated());

      doGetWithToken(FIND_USER_BOARDS_API_URL.formatted(userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.boards", hasSize(1)))
        .andExpect(jsonPath("$.boards[0].id", notNullValue()))
        .andExpect(
          jsonPath("$.boards[0].title", equalTo("Retrospective du 25 février"))
        )
        .andExpect(
          jsonPath(
            "$.boards[0].templateId",
            equalTo("b65687d3-4edc-4492-857d-3f22705ca7fd")
          )
        )
        .andExpect(jsonPath("$.boards[0].ownerId", equalTo(userId.toString())))
        .andExpect(jsonPath("$.boards[0].cards", hasSize(0)))
        .andExpect(jsonPath("$.boards[0].members", hasSize(1)))
        .andExpect(
          jsonPath("$.boards[0].members[0].id", equalTo(userId.toString()))
        );
    }
  }
}
