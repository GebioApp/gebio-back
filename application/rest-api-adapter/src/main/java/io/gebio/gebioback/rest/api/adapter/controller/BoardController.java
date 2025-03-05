package io.gebio.gebioback.rest.api.adapter.controller;

import static io.gebio.gebioback.rest.api.adapter.service.RestResourceURIBuilder.getCreatedResourceURI;

import io.gebio.gebioback.contract.api.BoardApi;
import io.gebio.gebioback.contract.model.*;
import io.gebio.gebioback.domain.model.User;
import io.gebio.gebioback.domain.port.in.BoardFacade;
import io.gebio.gebioback.domain.service.Board;
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
  private final AuthenticationService authenticationService;
  private final SimpMessagingTemplate simpMessagingTemplate;

  public BoardController(
    BoardFacade boardFacade,
    AuthenticationService authenticationService,
    SimpMessagingTemplate simpMessagingTemplate
  ) {
    this.boardFacade = boardFacade;
    this.authenticationService = authenticationService;
    this.simpMessagingTemplate = simpMessagingTemplate;
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
  public ResponseEntity<FindBoardResponseContract> findById(UUID boardId) {
    return ResponseEntity.ok(
      BoardMapper.findBoardFromDomainToContract(boardFacade.findById(boardId))
    );
  }

  @MessageMapping("/board/add-card")
  public void addCard(@Payload AddCardRequestContract addCardRequestContract) {
    Board updatedBoard = boardFacade.addCardToBoard(
      addCardRequestContract.getBoardId(),
      CardMapper.fromContractToDomain(addCardRequestContract.getCard())
    );
    simpMessagingTemplate.convertAndSend(
      "/topic/board/" + updatedBoard.id(),
      BoardMapper.findBoardFromDomainToContract(updatedBoard)
    );
  }

  @MessageMapping("/board/update-card")
  public void updateCard(
    @Payload UpdateCardRequestContract updateCardRequestContract
  ) {
    Board updatedBoard = boardFacade.updateCardOnBoard(
      updateCardRequestContract.getBoardId(),
      CardMapper.fromContractToDomain(updateCardRequestContract.getCard())
    );
    simpMessagingTemplate.convertAndSend(
      "/topic/board/" + updatedBoard.id(),
      BoardMapper.findBoardFromDomainToContract(updatedBoard)
    );
  }
}
