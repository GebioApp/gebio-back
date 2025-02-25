package io.gebio.gebioback.rest.api.adapter.controller;

import static io.gebio.gebioback.rest.api.adapter.service.RestResourceURIBuilder.getCreatedResourceURI;

import io.gebio.gebioback.contract.api.BoardApi;
import io.gebio.gebioback.contract.model.CreateBoardRequestContract;
import io.gebio.gebioback.contract.model.CreateBoardResponseContract;
import io.gebio.gebioback.domain.model.User;
import io.gebio.gebioback.domain.port.in.BoardFacade;
import io.gebio.gebioback.domain.service.Board;
import io.gebio.gebioback.rest.api.adapter.mapper.BoardMapper;
import io.gebio.gebioback.rest.api.adapter.service.AuthenticationService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController implements BoardApi {

  private final BoardFacade boardFacade;
  private final AuthenticationService authenticationService;

  public BoardController(
    BoardFacade boardFacade,
    AuthenticationService authenticationService
  ) {
    this.boardFacade = boardFacade;
    this.authenticationService = authenticationService;
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
}
