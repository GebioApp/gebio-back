package io.gebio.gebioback.bootstrap.it;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.gebio.gebioback.postgres.entity.UserEntity;
import io.gebio.gebioback.postgres.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class BoardControllerIT extends AbstractGebioBackApiIT {

  @Autowired
  UserRepository userRepository;

  @Nested
  class CreateBoard {

    @Test
    void should_return_401_when_unauthenticated() throws Exception {
      mockMvc
        .perform(
          post(CREATE_BOARD_API_URL).contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized());
    }

    @Test
    void should_return_400_when_user_try_to_create_board_with_empty_board_name()
      throws Exception {
      String requestBody =
        """
        {
          "templateId": "b65687d3-4edc-4492-857d-3f22705ca7fd"
        }
        """;
      mockMvc
        .perform(
          post(CREATE_BOARD_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .with(jwtToken())
            .content(requestBody)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_user_try_to_create_board_with_empty_template_id()
      throws Exception {
      String requestBody =
        """
        {
          "title": "Retrospective du 25 février"
        }
        """;
      mockMvc
        .perform(
          post(CREATE_BOARD_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .with(jwtToken())
            .content(requestBody)
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_201_and_with_created_board() throws Exception {
      UUID id = UUID.fromString("3338266c-26f2-4c85-8157-91f02b680577");
      UserEntity userEntity = new UserEntity(id, EMAIL);
      userRepository.save(userEntity);

      String requestBody =
        """
        {
          "title": "Retrospective du 25 février",
          "templateId": "b65687d3-4edc-4492-857d-3f22705ca7fd"
        }
        """;
      mockMvc
        .perform(
          post(CREATE_BOARD_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .with(jwtToken())
            .content(requestBody)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.title", equalTo("Retrospective du 25 février")))
        .andExpect(
          jsonPath(
            "$.templateId",
            equalTo("b65687d3-4edc-4492-857d-3f22705ca7fd")
          )
        );
    }
  }
}
