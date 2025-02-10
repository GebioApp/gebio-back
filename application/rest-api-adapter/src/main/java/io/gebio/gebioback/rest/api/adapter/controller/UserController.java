package io.gebio.gebioback.rest.api.adapter.controller;

import io.gebio.gebioback.contract.api.UserApi;
import io.gebio.gebioback.contract.model.CurrentUserResponseContract;
import io.gebio.gebioback.domain.model.User;
import io.gebio.gebioback.rest.api.adapter.mapper.UserContractMapper;
import io.gebio.gebioback.rest.api.adapter.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

  private final AuthenticationService authenticationService;

  public UserController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  public ResponseEntity<CurrentUserResponseContract> getCurrentUser() {
    final User authenticatedUser = authenticationService.getAuthenticatedUser();
    return ResponseEntity.ok(
      UserContractMapper.domainToContract(authenticatedUser)
    );
  }
}
