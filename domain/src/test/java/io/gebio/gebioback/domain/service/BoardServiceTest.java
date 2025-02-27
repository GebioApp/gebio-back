package io.gebio.gebioback.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.gebio.gebioback.core.exception.BoardNotFound;
import io.gebio.gebioback.domain.model.Card;
import io.gebio.gebioback.domain.model.User;
import io.gebio.gebioback.domain.port.out.BoardRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

  @InjectMocks
  BoardService boardService;

  @Test
  void should_create_board_given_board_name_and_board_template() {
    //given
    ArgumentCaptor<Board> argumentCaptor = ArgumentCaptor.captor();

    UUID userId = UUID.fromString("35920a0f-7c3f-484d-96a3-7efa789c6079");
    String userEmail = "dorianf@gebio.com";
    User currentUser = new User(userId, userEmail, null);

    String boardName = "Retrospective du 25 février";
    UUID templateId = UUID.fromString("e637621b-4451-4b38-b33f-f07dbd8aeceb");

    //when
    boardService.createWithOwner(templateId, boardName, currentUser);

    //then
    verify(boardRepositoryPort).createBoard(argumentCaptor.capture());
    Board savedBoard = argumentCaptor.getValue();
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
      "https://logo.com"
    );
    User cardOwner = new User(
      UUID.randomUUID(),
      "another.user@gebio.com",
      "https://another-logo.com"
    );
    List<Card> cards = List.of(
      new Card(
        UUID.randomUUID(),
        "I'm the content of the card",
        "#000000",
        new Card.Position(100, 100),
        cardOwner
      )
    );
    Board expectedBoard = new Board(
      boardId,
      "My board",
      UUID.randomUUID(),
      boardOwner,
      cards
    );
    when(boardRepositoryPort.findById(boardId)).thenReturn(
      Optional.of(expectedBoard)
    );

    Board result = boardService.findById(boardId);

    assertThat(result).isEqualTo(expectedBoard);
  }
}
