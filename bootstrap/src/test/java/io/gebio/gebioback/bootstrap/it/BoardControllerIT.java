package io.gebio.gebioback.bootstrap.it;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.gebio.gebioback.contract.model.*;
import io.gebio.gebioback.postgres.entity.BoardEntity;
import io.gebio.gebioback.postgres.entity.CardEntity;
import io.gebio.gebioback.postgres.entity.UserEntity;
import io.gebio.gebioback.postgres.repository.BoardRepository;
import io.gebio.gebioback.postgres.repository.UserRepository;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
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
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

class BoardControllerIT extends AbstractGebioBackApiIT {

  @Autowired
  UserRepository userRepository;

  @Autowired
  BoardRepository boardRepository;

  private WebSocketStompClient stompClient;
  private StompSession stompSession;

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
        AUTHENTICATED_USER_LOGO
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
        .andExpect(jsonPath("$.board.cards", hasSize(0)));
    }
  }

  @Nested
  class FindBoard {

    UUID boardId = UUID.fromString("99c25084-4df3-42da-bece-4e7e50788abb");

    @Test
    void should_return_401_when_unauthenticated() throws Exception {
      mockMvc
        .perform(
          get(String.format(FIND_BOARD_API_URL, boardId)).contentType(
            MediaType.APPLICATION_JSON
          )
        )
        .andExpect(status().isUnauthorized());
    }

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
        AUTHENTICATED_USER_LOGO
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

      UUID id = UUID.fromString("3338266c-26f2-4c85-8157-91f02b680577");
      UserEntity userEntity = new UserEntity(
        id,
        AUTHENTICATED_USER_EMAIL,
        AUTHENTICATED_USER_LOGO
      );
      userRepository.save(userEntity);

      BoardEntity board = new BoardEntity(
        boardId,
        "Test Board",
        UUID.randomUUID(),
        userEntity
      );

      boardRepository.save(board);

      AddCardRequestContract request = new AddCardRequestContract();
      CardOwnerInfoContract ownerInfo = new CardOwnerInfoContract(
        id,
        AUTHENTICATED_USER_EMAIL,
        AUTHENTICATED_USER_LOGO
      );
      request.setBoardId(boardId);
      request.setCard(
        new CardContract(
          cardId,
          "Test Card",
          "#FF5733",
          new CardPositionContract(1, 2),
          ownerInfo
        )
      );

      CountDownLatch latch = new CountDownLatch(1);
      final FindBoardResponseContract[] responseHolder =
        new FindBoardResponseContract[1];

      stompSession.subscribe(
        "/topic/board/" + boardId,
        new StompFrameHandler() {
          @Override
          public Type getPayloadType(StompHeaders headers) {
            return FindBoardResponseContract.class;
          }

          @Override
          public void handleFrame(StompHeaders headers, Object payload) {
            responseHolder[0] = (FindBoardResponseContract) payload;
            latch.countDown();
          }
        }
      );

      stompSession.send("/app/board/add-card", request);

      assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
      assertThat(responseHolder[0]).isNotNull();

      FindBoardResponseContract response = responseHolder[0];
      assertThat(response.getBoard()).isNotNull();
      assertThat(response.getBoard().getCards()).isNotNull();

      CardContract addedCard = response.getBoard().getCards().getFirst();
      assertThat(addedCard.getId()).isEqualTo(cardId);
      assertThat(addedCard.getContent()).isEqualTo("Test Card");
      assertThat(addedCard.getColor()).isEqualTo("#FF5733");
      assertThat(addedCard.getPosition().getPosX()).isEqualTo(1);
      assertThat(addedCard.getPosition().getPosY()).isEqualTo(2);
      assertThat(addedCard.getOwnerInfo().getId()).isEqualTo(id);
      assertThat(addedCard.getOwnerInfo().getLogo()).isEqualTo(
        AUTHENTICATED_USER_LOGO
      );
      assertThat(addedCard.getOwnerInfo().getEmail()).isEqualTo(
        AUTHENTICATED_USER_EMAIL
      );
    }
  }

  @Nested
  class UpdateCard {

    @Test
    void should_update_card_on_board() throws Exception {
      UUID boardId = UUID.randomUUID();
      UUID cardId = UUID.randomUUID();

      UUID id = UUID.fromString("3338266c-26f2-4c85-8157-91f02b680577");
      UserEntity userEntity = new UserEntity(
        id,
        AUTHENTICATED_USER_EMAIL,
        AUTHENTICATED_USER_LOGO
      );
      userRepository.save(userEntity);

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
        userEntity,
        boardEntity
      );
      boardEntity.setCards(List.of(cardEntity));

      boardRepository.save(boardEntity);

      UpdateCardRequestContract request = new UpdateCardRequestContract();
      CardOwnerInfoContract ownerInfo = new CardOwnerInfoContract(
        id,
        AUTHENTICATED_USER_EMAIL,
        AUTHENTICATED_USER_LOGO
      );
      request.setBoardId(boardId);
      request.setCard(
        new CardContract(
          cardId,
          "Card 1 Content modified",
          "#FF5735",
          new CardPositionContract(20, 30),
          ownerInfo
        )
      );

      CountDownLatch latch = new CountDownLatch(1);
      final FindBoardResponseContract[] responseHolder =
        new FindBoardResponseContract[1];

      stompSession.subscribe(
        "/topic/board/" + boardId,
        new StompFrameHandler() {
          @Override
          public Type getPayloadType(StompHeaders headers) {
            return FindBoardResponseContract.class;
          }

          @Override
          public void handleFrame(StompHeaders headers, Object payload) {
            responseHolder[0] = (FindBoardResponseContract) payload;
            latch.countDown();
          }
        }
      );

      stompSession.send("/app/board/update-card", request);

      assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
      assertThat(responseHolder[0]).isNotNull();

      FindBoardResponseContract response = responseHolder[0];
      assertThat(response.getBoard()).isNotNull();
      assertThat(response.getBoard().getCards()).isNotNull();

      CardContract addedCard = response.getBoard().getCards().getFirst();
      assertThat(addedCard.getId()).isEqualTo(cardId);
      assertThat(addedCard.getContent()).isEqualTo("Card 1 Content modified");
      assertThat(addedCard.getColor()).isEqualTo("#FF5735");
      assertThat(addedCard.getPosition().getPosX()).isEqualTo(20);
      assertThat(addedCard.getPosition().getPosY()).isEqualTo(30);
      assertThat(addedCard.getOwnerInfo().getId()).isEqualTo(id);
      assertThat(addedCard.getOwnerInfo().getLogo()).isEqualTo(
        AUTHENTICATED_USER_LOGO
      );
      assertThat(addedCard.getOwnerInfo().getEmail()).isEqualTo(
        AUTHENTICATED_USER_EMAIL
      );
    }
  }

  @Nested
  class DeleteCard {

    @Test
    void should_delete_card_from_board() throws InterruptedException {
      UUID boardId = UUID.randomUUID();
      UUID cardId = UUID.randomUUID();

      UUID id = UUID.fromString("3338266c-26f2-4c85-8157-91f02b680577");
      UserEntity userEntity = new UserEntity(
        id,
        AUTHENTICATED_USER_EMAIL,
        AUTHENTICATED_USER_LOGO
      );
      userRepository.save(userEntity);

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
        userEntity,
        boardEntity
      );
      boardEntity.setCards(List.of(cardEntity));

      boardRepository.save(boardEntity);

      DeleteCardRequestContract request = new DeleteCardRequestContract();
      CardOwnerInfoContract ownerInfo = new CardOwnerInfoContract(
        id,
        AUTHENTICATED_USER_EMAIL,
        AUTHENTICATED_USER_LOGO
      );
      request.setBoardId(boardId);
      request.setCardId(cardId);

      CountDownLatch latch = new CountDownLatch(1);
      final FindBoardResponseContract[] responseHolder =
        new FindBoardResponseContract[1];

      stompSession.subscribe(
        "/topic/board/" + boardId,
        new StompFrameHandler() {
          @Override
          public Type getPayloadType(StompHeaders headers) {
            return FindBoardResponseContract.class;
          }

          @Override
          public void handleFrame(StompHeaders headers, Object payload) {
            responseHolder[0] = (FindBoardResponseContract) payload;
            latch.countDown();
          }
        }
      );

      stompSession.send("/app/board/delete-card", request);

      assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
      assertThat(responseHolder[0]).isNotNull();

      FindBoardResponseContract response = responseHolder[0];
      assertThat(response.getBoard()).isNotNull();
      assertThat(response.getBoard().getCards().size()).isEqualTo(0);
    }
  }
}
