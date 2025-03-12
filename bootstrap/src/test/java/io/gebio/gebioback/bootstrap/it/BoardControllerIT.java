package io.gebio.gebioback.bootstrap.it;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.gebio.gebioback.contract.model.*;
import io.gebio.gebioback.domain.model.UserRole;
import io.gebio.gebioback.postgres.entity.BoardEntity;
import io.gebio.gebioback.postgres.entity.CardEntity;
import io.gebio.gebioback.postgres.entity.UserEntity;
import io.gebio.gebioback.postgres.repository.BoardRepository;
import io.gebio.gebioback.postgres.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

class BoardControllerIT extends AbstractGebioBackApiIT {

  @Autowired
  UserRepository userRepository;

  @Autowired
  BoardRepository boardRepository;

  @LocalServerPort
  private int port;

  @BeforeEach
  void setUp()
    throws ExecutionException, InterruptedException, TimeoutException {
    userRepository.deleteAll();
    boardRepository.deleteAll();

    stompClient = new WebSocketStompClient(new StandardWebSocketClient());
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    // Connexion au WebSocket
    stompSession = stompClient
      .connectAsync(
        "ws://localhost:" + port + "/ws",
        new StompSessionHandlerAdapter() {}
      )
      .get(1, TimeUnit.SECONDS);
  }

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
      UserEntity userEntity = new UserEntity(
        id,
        AUTHENTICATED_USER_EMAIL,
        AUTHENTICATED_USER_LOGO,
        AUTHENTICATED_USER_USERNAME,
        UserRole.USER.name()
      );
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
        .andExpect(jsonPath("$.board.id").exists())
        .andExpect(
          jsonPath("$.board.title", equalTo("Retrospective du 25 février"))
        )
        .andExpect(
          jsonPath(
            "$.board.templateId",
            equalTo("b65687d3-4edc-4492-857d-3f22705ca7fd")
          )
        )
        .andExpect(
          jsonPath(
            "$.board.ownerId",
            equalTo("3338266c-26f2-4c85-8157-91f02b680577")
          )
        )
        .andExpect(jsonPath("$.board.cards", hasSize(0)))
        .andExpect(jsonPath("$.board.members", hasSize(1)))
        .andExpect(jsonPath("$.board.members[0].id", equalTo(id.toString())))
        .andExpect(
          jsonPath("$.board.members[0].logo", equalTo(AUTHENTICATED_USER_LOGO))
        )
        .andExpect(
          jsonPath(
            "$.board.members[0].email",
            equalTo(AUTHENTICATED_USER_EMAIL)
          )
        );
    }
  }

  @Nested
  class FindBoard {

    UUID boardId = UUID.fromString("99c25084-4df3-42da-bece-4e7e50788abb");

    @Test
    void should_return_404_when_board_was_not_found() throws Exception {
      mockMvc
        .perform(
          get(String.format(FIND_BOARD_API_URL, boardId))
            .with(jwtToken())
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound());
    }

    @Test
    void should_return_200_and_retrieve_existing_board() throws Exception {
      UUID id = UUID.fromString("3338266c-26f2-4c85-8157-91f02b680577");
      UserEntity userEntity = new UserEntity(
        id,
        AUTHENTICATED_USER_EMAIL,
        AUTHENTICATED_USER_LOGO,
        AUTHENTICATED_USER_USERNAME,
        UserRole.USER.name()
      );
      userRepository.save(userEntity);

      BoardEntity board = new BoardEntity(
        boardId,
        "Test Board",
        UUID.randomUUID(),
        userEntity
      );

      CardEntity card1 = new CardEntity(
        UUID.randomUUID(),
        "Card 1 Content",
        "#FF5733",
        10,
        20,
        userEntity,
        board
      );
      CardEntity card2 = new CardEntity(
        UUID.randomUUID(),
        "Card 2 Content",
        "#33FF57",
        30,
        40,
        userEntity,
        board
      );

      board.setCards(List.of(card1, card2));

      boardRepository.save(board);

      mockMvc
        .perform(
          get(String.format(FIND_BOARD_API_URL, boardId))
            .with(jwtToken())
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.board.id", equalTo(boardId.toString())))
        .andExpect(jsonPath("$.board.title", equalTo(board.getName())))
        .andExpect(
          jsonPath(
            "$.board.templateId",
            equalTo(board.getTemplateId().toString())
          )
        )
        .andExpect(
          jsonPath(
            "$.board.ownerId",
            equalTo(board.getOwner().getId().toString())
          )
        )
        .andExpect(jsonPath("$.board.cards", hasSize(2)))
        .andExpect(
          jsonPath("$.board.cards[0].id", equalTo(card1.getId().toString()))
        )
        .andExpect(
          jsonPath("$.board.cards[0].content", equalTo(card1.getContent()))
        )
        .andExpect(
          jsonPath("$.board.cards[0].color", equalTo(card1.getColor()))
        )
        .andExpect(
          jsonPath("$.board.cards[0].position.posX", equalTo(card1.getPosX()))
        )
        .andExpect(
          jsonPath("$.board.cards[0].position.posY", equalTo(card1.getPosY()))
        )
        .andExpect(
          jsonPath(
            "$.board.cards[0].ownerInfo.id",
            equalTo(card1.getOwner().getId().toString())
          )
        )
        .andExpect(
          jsonPath(
            "$.board.cards[0].ownerInfo.logo",
            equalTo(card1.getOwner().getProfileLogo())
          )
        );
    }
  }

  @Nested
  class AddCard {

    @Test
    void should_add_card_to_board() throws Exception {
      UUID boardId = UUID.randomUUID();
      UUID cardId = UUID.randomUUID();

      UserEntity userEntity = createAndSaveAuthenticatedUser();
      UserEntity guestEntity = createAndSaveGuestUser();
      BoardEntity board = new BoardEntity(
        boardId,
        "Test Board",
        UUID.randomUUID(),
        userEntity
      );
      boardRepository.save(board);

      AddCardRequestContract request = new AddCardRequestContract();
      CardOwnerInfoContract ownerInfoContract = new CardOwnerInfoContract();
      ownerInfoContract.setId(guestEntity.getId());
      ownerInfoContract.setEmail(guestEntity.getEmail());
      ownerInfoContract.setLogo(guestEntity.getProfileLogo());
      ownerInfoContract.setUsername(guestEntity.getUsername());
      ownerInfoContract.setRole(
        UserRoleContract.valueOf(guestEntity.getRole())
      );
      request.setBoardId(boardId);
      request.setCard(
        new CardContract(
          cardId,
          "Test Card",
          "#FF5733",
          new CardPositionContract(1, 2),
          ownerInfoContract
        )
      );

      AddCardResponseContract response = waitForMessage(
        AddCardResponseContract.class,
        "/topic/board/" + boardId,
        () -> stompSession.send(ADD_CARD_API_URL, request)
      );
      assertThat(response.getOperationType()).isEqualTo(
        OperationTypeContract.ADD
      );
      assertThat(response.getCard())
        .isNotNull()
        .satisfies(addedCard -> {
          assertThat(addedCard.getId()).isEqualTo(cardId);
          assertThat(addedCard.getContent()).isEqualTo("Test Card");
          assertThat(addedCard.getColor()).isEqualTo("#FF5733");
          assertThat(addedCard.getPosition().getPosX()).isEqualTo(1);
          assertThat(addedCard.getPosition().getPosY()).isEqualTo(2);
          assertThat(addedCard.getOwnerInfo().getId()).isEqualTo(
            ownerInfoContract.getId()
          );
          assertThat(addedCard.getOwnerInfo().getEmail()).isEqualTo(
            ownerInfoContract.getEmail()
          );
          assertThat(addedCard.getOwnerInfo().getLogo()).isEqualTo(
            ownerInfoContract.getLogo()
          );
          assertThat(addedCard.getOwnerInfo().getUsername()).isEqualTo(
            ownerInfoContract.getUsername()
          );
          assertThat(addedCard.getOwnerInfo().getRole()).isEqualTo(
            ownerInfoContract.getRole()
          );
          assertThat(addedCard.getBoardId()).isEqualTo(boardId);
        });
    }
  }

  @Nested
  class UpdateCard {

    @Test
    void should_update_card_on_board() throws Exception {
      UUID boardId = UUID.randomUUID();
      UUID cardId = UUID.randomUUID();

      UserEntity userEntity = createAndSaveAuthenticatedUser();
      UserEntity guestEntity = createAndSaveGuestUser();
      BoardEntity boardEntity = new BoardEntity(
        boardId,
        "Test Board",
        UUID.randomUUID(),
        userEntity
      );

      CardEntity cardEntity = new CardEntity(
        cardId,
        "Card 1 Content",
        "#FF5733",
        10,
        20,
        guestEntity,
        boardEntity
      );
      boardEntity.setCards(List.of(cardEntity));
      boardRepository.save(boardEntity);

      UpdateCardRequestContract request = new UpdateCardRequestContract();
      CardOwnerInfoContract ownerInfoContract = new CardOwnerInfoContract();
      ownerInfoContract.setId(guestEntity.getId());
      ownerInfoContract.setEmail(guestEntity.getEmail());
      ownerInfoContract.setLogo(guestEntity.getProfileLogo());
      ownerInfoContract.setUsername(guestEntity.getUsername());
      ownerInfoContract.setRole(
        UserRoleContract.valueOf(guestEntity.getRole())
      );
      request.setCard(
        new CardContract(
          cardId,
          "Card 1 Content modified",
          "#FF5735",
          new CardPositionContract(20, 30),
          ownerInfoContract
        )
      );

      UpdateCardResponseContract response = waitForMessage(
        UpdateCardResponseContract.class,
        TOPIC_BOARD_API_URL.formatted(boardId),
        () -> stompSession.send(UPDATED_CARD_API_URL, request)
      );

      assertThat(response.getOperationType()).isEqualTo(
        OperationTypeContract.UPDATE
      );
      assertThat(response.getCard())
        .isNotNull()
        .satisfies(updatedCard -> {
          assertThat(updatedCard.getId()).isEqualTo(cardId);
          assertThat(updatedCard.getContent()).isEqualTo(
            "Card 1 Content modified"
          );
          assertThat(updatedCard.getColor()).isEqualTo("#FF5735");
          assertThat(updatedCard.getPosition().getPosX()).isEqualTo(20);
          assertThat(updatedCard.getPosition().getPosY()).isEqualTo(30);
          assertThat(updatedCard.getOwnerInfo().getId()).isEqualTo(
            guestEntity.getId()
          );
          assertThat(updatedCard.getOwnerInfo().getEmail()).isEqualTo(
            guestEntity.getEmail()
          );
          assertThat(updatedCard.getOwnerInfo().getLogo()).isEqualTo(
            guestEntity.getProfileLogo()
          );
          assertThat(updatedCard.getOwnerInfo().getUsername()).isEqualTo(
            guestEntity.getUsername()
          );
          assertThat(updatedCard.getOwnerInfo().getRole()).isEqualTo(
            UserRoleContract.valueOf(guestEntity.getRole())
          );
          assertThat(updatedCard.getBoardId()).isEqualTo(boardId);
        });
    }

    @Nested
    class DeleteCard {

      @Test
      void should_delete_card_from_board() throws InterruptedException {
        UUID boardId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();

        UserEntity userEntity = createAndSaveAuthenticatedUser();
        UserEntity guestEntity = createAndSaveGuestUser();
        BoardEntity boardEntity = new BoardEntity(
          boardId,
          "Test Board",
          UUID.randomUUID(),
          userEntity
        );

        CardEntity cardEntity = new CardEntity(
          cardId,
          "Card 1 Content",
          "#FF5733",
          10,
          20,
          guestEntity,
          boardEntity
        );
        boardEntity.setCards(List.of(cardEntity));
        boardRepository.save(boardEntity);

        DeleteCardRequestContract request = new DeleteCardRequestContract();
        request.setCardId(cardId);

        DeleteCardResponseContract response = waitForMessage(
          DeleteCardResponseContract.class,
          TOPIC_BOARD_API_URL.formatted(boardId),
          () -> stompSession.send(DELETE_CARD_API_URL, request)
        );

        assertThat(response.getOperationType()).isEqualTo(
          OperationTypeContract.DELETE
        );
        assertThat(response.getCard())
          .isNotNull()
          .satisfies(deletedCard -> {
            assertThat(deletedCard.getId()).isEqualTo(cardId);
            assertThat(deletedCard.getContent()).isEqualTo("Card 1 Content");
            assertThat(deletedCard.getColor()).isEqualTo("#FF5733");
            assertThat(deletedCard.getPosition().getPosX()).isEqualTo(10);
            assertThat(deletedCard.getPosition().getPosY()).isEqualTo(20);
            assertThat(deletedCard.getOwnerInfo().getId()).isEqualTo(
              guestEntity.getId()
            );
            assertThat(deletedCard.getOwnerInfo().getEmail()).isEqualTo(
              guestEntity.getEmail()
            );
            assertThat(deletedCard.getOwnerInfo().getLogo()).isEqualTo(
              guestEntity.getProfileLogo()
            );
            assertThat(deletedCard.getOwnerInfo().getUsername()).isEqualTo(
              guestEntity.getUsername()
            );
            assertThat(deletedCard.getOwnerInfo().getRole()).isEqualTo(
              UserRoleContract.valueOf(guestEntity.getRole())
            );
            assertThat(deletedCard.getBoardId()).isEqualTo(boardId);
          });
      }
    }
  }
}
