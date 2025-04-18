package io.gebio.gebioback.rest.api.adapter.controller;

import static io.gebio.gebioback.rest.api.adapter.service.RestResourceURIBuilder.getCreatedResourceURI;

import io.gebio.gebioback.contract.api.BoardApi;
import io.gebio.gebioback.contract.model.*;
import io.gebio.gebioback.domain.model.*;
import io.gebio.gebioback.domain.port.in.BoardFacade;
import io.gebio.gebioback.domain.port.in.CardFacade;
import io.gebio.gebioback.rest.api.adapter.mapper.BoardMapper;
import io.gebio.gebioback.rest.api.adapter.mapper.CardMapper;
import io.gebio.gebioback.rest.api.adapter.service.AuthenticationService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController implements BoardApi {

  private final BoardFacade boardFacade;
  private final CardFacade cardFacade;
  private final AuthenticationService authenticationService;
  private final SimpMessagingTemplate simpMessagingTemplate;

  private static final String TOPIC_BOARD = "/topic/boards/";

  public BoardController(
    BoardFacade boardFacade,
    CardFacade cardFacade,
    AuthenticationService authenticationService,
    SimpMessagingTemplate simpMessagingTemplate
  ) {
    this.boardFacade = boardFacade;
    this.cardFacade = cardFacade;
    this.authenticationService = authenticationService;
    this.simpMessagingTemplate = simpMessagingTemplate;
  }

  @Override
  public ResponseEntity<AddUserToBoardResponseContract> addUserToBoard(
    UUID boardId,
    AddUserToBoardRequestContract addUserToBoardRequestContract
  ) {
    Board updatedBoard = boardFacade.addUserToBoard(
      boardId,
      addUserToBoardRequestContract.getUserId()
    );
    return ResponseEntity.ok(
      BoardMapper.addUserFromDomainToContract(updatedBoard)
    );
  }

  @Override
  public ResponseEntity<CreateBoardResponseContract> createBoard(
    @Valid CreateBoardRequestContract createBoardRequestContract
  ) {
    User currentUser = authenticationService.getAuthenticatedUser();

    Board createdBoard = boardFacade.createWithOwner(
      createBoardRequestContract.getTemplateId(),
      createBoardRequestContract.getTitle(),
      currentUser
    );

    URI location = getCreatedResourceURI(createdBoard.id());

    return ResponseEntity.created(location).body(
      BoardMapper.createBoardFromDomainToContract(createdBoard)
    );
  }

  @Override
  public ResponseEntity<Void> deleteBoard(UUID boardId) {
    User currentUser = authenticationService.getAuthenticatedUser();
    boardFacade.deleteBoard(new BoardDeleteCommand(boardId, currentUser.id()));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<FindBoardResponseContract> findById(UUID boardId) {
    return ResponseEntity.ok(
      BoardMapper.findBoardFromDomainToContract(boardFacade.findById(boardId))
    );
  }

  @Override
  public ResponseEntity<UpdateBoardResponseContract> updateBoard(
    UUID boardId,
    @Valid UpdateBoardRequestContract renameBoardRequestContract
  ) {
    User currentUser = authenticationService.getAuthenticatedUser();
    Board updatedBoard = boardFacade.updateBoard(
      new BoardUpdateCommand(
        boardId,
        currentUser.id(),
        renameBoardRequestContract.getBoardName()
      )
    );
    return ResponseEntity.ok(
      BoardMapper.updateBoardFromDomainToContract(updatedBoard)
    );
  }

  @MessageMapping("/boards/add-card")
  public void addCard(@Payload AddCardRequestContract addCardRequestContract) {
    Card addedCard = cardFacade.addCardToBoard(
      addCardRequestContract.getBoardId(),
      CardMapper.fromContractToDomain(addCardRequestContract.getCard())
    );
    simpMessagingTemplate.convertAndSend(
      TOPIC_BOARD + addedCard.boardId(),
      CardMapper.addCardFromDomainToContract(addedCard)
    );
  }

  @MessageMapping("/boards/update-card")
  public void updateCard(
    @Payload UpdateCardRequestContract updateCardRequestContract
  ) {
    Card updatedCard = cardFacade.updateCardOnBoard(
      CardMapper.fromContractToDomain(updateCardRequestContract.getCard())
    );
    simpMessagingTemplate.convertAndSend(
      TOPIC_BOARD + updatedCard.boardId(),
      CardMapper.updateCardFromDomainToContract(updatedCard)
    );
  }

  @MessageMapping("/boards/delete-card")
  public void updateCard(
    @Payload DeleteCardRequestContract deleteCardRequestContract
  ) {
    Card deletedCard = cardFacade.deleteCardFromBoard(
      deleteCardRequestContract.getCardId()
    );
    simpMessagingTemplate.convertAndSend(
      TOPIC_BOARD + deletedCard.boardId(),
      CardMapper.deleteCardFromDomainToContract(deletedCard)
    );
  }
}
