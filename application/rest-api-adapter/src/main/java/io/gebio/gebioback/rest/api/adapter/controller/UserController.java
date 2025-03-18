package io.gebio.gebioback.rest.api.adapter.controller;

import static io.gebio.gebioback.rest.api.adapter.service.RestResourceURIBuilder.getCreatedResourceURI;

import io.gebio.gebioback.contract.api.UserApi;
import io.gebio.gebioback.contract.model.CreateGuestRequestContract;
import io.gebio.gebioback.contract.model.CreateGuestResponseContract;
import io.gebio.gebioback.contract.model.CurrentUserResponseContract;
import io.gebio.gebioback.contract.model.FindBoardsResponseContract;
import io.gebio.gebioback.domain.model.User;
import io.gebio.gebioback.domain.port.in.BoardFacade;
import io.gebio.gebioback.domain.port.in.UserFacade;
import io.gebio.gebioback.rest.api.adapter.mapper.UserContractMapper;
import io.gebio.gebioback.rest.api.adapter.service.AuthenticationService;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

  private final AuthenticationService authenticationService;
  private final UserFacade userFacade;
  private final BoardFacade boardFacade;

  public UserController(
    AuthenticationService authenticationService,
    UserFacade userFacade,
    BoardFacade boardFacade
  ) {
    this.authenticationService = authenticationService;
    this.userFacade = userFacade;
    this.boardFacade = boardFacade;
  }

  @Override
  public ResponseEntity<CreateGuestResponseContract> createGuest(
    CreateGuestRequestContract createGuestRequestContract
  ) {
    User createdGuest = userFacade.createGuest(
      createGuestRequestContract.getUsername()
    );

    URI location = getCreatedResourceURI(createdGuest.id());

    return ResponseEntity.created(location).body(
      UserContractMapper.createGuestFromDomainToContract(createdGuest)
    );
  }

  @Override
  public ResponseEntity<FindBoardsResponseContract> findBoards(UUID userId) {
    return ResponseEntity.ok(
      UserContractMapper.findBoardsDomainToContract(
        boardFacade.findAllUserJoinedBoards(userId)
      )
    );
  }

  @Override
  public ResponseEntity<CurrentUserResponseContract> getCurrentUser() {
    final User authenticatedUser = authenticationService.getAuthenticatedUser();
    return ResponseEntity.ok(
      UserContractMapper.domainToContract(authenticatedUser)
    );
  }
}
