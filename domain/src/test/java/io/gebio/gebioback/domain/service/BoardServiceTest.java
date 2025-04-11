package io.gebio.gebioback.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.gebio.gebioback.core.exception.BoardNotFound;
import io.gebio.gebioback.core.exception.UserNotFound;
import io.gebio.gebioback.domain.model.*;
import io.gebio.gebioback.domain.port.out.BoardRepositoryPort;
import io.gebio.gebioback.domain.port.out.UserRepositoryPort;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

  @Mock
  BoardRepositoryPort boardRepositoryPort;

  @Mock
  UserRepositoryPort userRepositoryPort;

  @InjectMocks
  BoardService boardService;

  @Nested
  class CreateBoardTest {

    @Test
    void should_create_board_given_board_name_and_board_template() {
      //given
      ArgumentCaptor<BoardCreationCommand> argumentCaptor =
        ArgumentCaptor.captor();

      UUID userId = UUID.fromString("35920a0f-7c3f-484d-96a3-7efa789c6079");
      String userEmail = "dorianf@gebio.com";
      User currentUser = new User(userId, userEmail, null, null, UserRole.USER);

      String boardName = "Retrospective du 25 février";
      UUID templateId = UUID.fromString("e637621b-4451-4b38-b33f-f07dbd8aeceb");

      //when
      boardService.createWithOwner(templateId, boardName, currentUser);

      //then
      verify(boardRepositoryPort).create(argumentCaptor.capture());
      BoardCreationCommand savedBoard = argumentCaptor.getValue();
      assertThat(savedBoard.id()).isNotNull();
      assertThat(savedBoard.name()).isEqualTo(boardName);
      assertThat(savedBoard.templateId()).isEqualTo(templateId);
      assertThat(savedBoard.owner()).isEqualTo(currentUser);
      assertThat(savedBoard.cards()).isEmpty();
    }

    @Test
    void should_throw_when_trying_to_find_board_by_id() {
      UUID boardId = UUID.fromString("184628cc-1493-414d-a9b5-ede2247d88ee");

      when(boardRepositoryPort.findById(boardId)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> boardService.findById(boardId))
        .isExactlyInstanceOf(BoardNotFound.class)
        .hasMessage(
          "Board with id 184628cc-1493-414d-a9b5-ede2247d88ee was not found"
        );
    }

    @Test
    void should_successfully_return_board() {
      UUID boardId = UUID.randomUUID();
      User boardOwner = new User(
        UUID.randomUUID(),
        "dorianf@gebio.com",
        "https://logo.com",
        null,
        UserRole.USER
      );
      User cardOwner = new User(
        UUID.randomUUID(),
        "another.user@gebio.com",
        "https://another-logo.com",
        null,
        UserRole.USER
      );
      List<Card> cards = List.of(
        new Card(
          UUID.randomUUID(),
          "I'm the content of the card",
          "#000000",
          new Card.Position(100, 100),
          cardOwner,
          boardId,
          0
        )
      );
      Board expectedBoard = new Board(
        boardId,
        "My board",
        UUID.randomUUID(),
        boardOwner,
        cards,
        List.of(boardOwner),
        OffsetDateTime.now(),
        OffsetDateTime.now()
      );
      when(boardRepositoryPort.findById(boardId)).thenReturn(
        Optional.of(expectedBoard)
      );

      Board result = boardService.findById(boardId);

      assertThat(result).isEqualTo(expectedBoard);
    }
  }

  @Nested
  class AddUserToBoardTest {

    @Test
    void should_throw_when_board_is_not_found() {
      UUID boardId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();

      when(boardRepositoryPort.findById(boardId)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> boardService.addUserToBoard(boardId, userId)
      ).isExactlyInstanceOf(BoardNotFound.class);
    }

    @Test
    void should_throw_when_user_is_not_found() {
      UUID boardId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();

      User boardOwner = new User(
        UUID.randomUUID(),
        "dorianf@gebio.com",
        "https://logo.com",
        null,
        UserRole.USER
      );
      User cardOwner = new User(
        UUID.randomUUID(),
        "another.user@gebio.com",
        "https://another-logo.com",
        null,
        UserRole.USER
      );
      List<Card> cards = List.of(
        new Card(
          UUID.randomUUID(),
          "I'm the content of the card",
          "#000000",
          new Card.Position(100, 100),
          cardOwner,
          boardId,
          0
        )
      );
      Board expectedBoard = new Board(
        boardId,
        "My board",
        UUID.randomUUID(),
        boardOwner,
        cards,
        List.of(boardOwner),
        OffsetDateTime.now(),
        OffsetDateTime.now()
      );
      when(boardRepositoryPort.findById(boardId)).thenReturn(
        Optional.of(expectedBoard)
      );
      when(userRepositoryPort.findById(userId)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> boardService.addUserToBoard(boardId, userId)
      ).isExactlyInstanceOf(UserNotFound.class);
    }

    @Test
    void should_save_board_with_new_user() {
      UUID boardId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();

      User boardOwner = new User(
        UUID.randomUUID(),
        "dorianf@gebio.com",
        "https://logo.com",
        null,
        UserRole.USER
      );
      User cardOwner = new User(
        UUID.randomUUID(),
        "another.user@gebio.com",
        "https://another-logo.com",
        null,
        UserRole.USER
      );
      List<Card> cards = List.of(
        new Card(
          UUID.randomUUID(),
          "I'm the content of the card",
          "#000000",
          new Card.Position(100, 100),
          cardOwner,
          boardId,
          0
        )
      );

      OffsetDateTime createdAt = OffsetDateTime.now();
      OffsetDateTime updatedAt = OffsetDateTime.now();
      Board expectedBoard = new Board(
        boardId,
        "My board",
        UUID.randomUUID(),
        boardOwner,
        cards,
        List.of(boardOwner),
        createdAt,
        updatedAt
      );
      when(boardRepositoryPort.findById(boardId)).thenReturn(
        Optional.of(expectedBoard)
      );
      User expectedUser = new User(
        userId,
        "john.doe@gmail.com",
        "https://logo.com",
        "John Doe",
        UserRole.USER
      );
      when(userRepositoryPort.findById(userId)).thenReturn(
        Optional.of(expectedUser)
      );

      Board updatedBoard = new Board(
        expectedBoard.id(),
        expectedBoard.name(),
        expectedBoard.templateId(),
        boardOwner,
        cards,
        List.of(boardOwner, expectedUser),
        createdAt,
        updatedAt
      );

      boardService.addUserToBoard(boardId, userId);

      verify(boardRepositoryPort).update(updatedBoard);
    }
  }

  @Nested
  class FindAllUserJoinedBoardsTest {

    @Test
    void should_user_joined_boards() {
      UUID userId = UUID.randomUUID();
      when(boardRepositoryPort.findAllForIdInMemberIds(userId)).thenReturn(
        List.of()
      );

      List<Board> boards = boardService.findAllUserJoinedBoards(userId);

      assertThat(boards).isEmpty();
    }
  }
}
