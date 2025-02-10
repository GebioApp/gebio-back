package io.gebio.gebioback.rest.api.adapter.service;

import io.gebio.gebioback.domain.model.User;
import io.gebio.gebioback.domain.port.in.UserFacade;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

  private static final String GEBIO_APP_EMAIL = "https://gebio.app/email";
  private final UserFacade userFacade;

  public AuthenticationService(UserFacade userFacade) {
    this.userFacade = userFacade;
  }

  public User getAuthenticatedUser() {
    return Optional.ofNullable(
      SecurityContextHolder.getContext().getAuthentication()
    )
      .map(authentication ->
        authentication.getPrincipal() instanceof Jwt jwt ? jwt : null
      )
      .map(Jwt::getClaims)
      .filter(claims -> claims.containsKey(GEBIO_APP_EMAIL))
      .map(claims -> claims.get(GEBIO_APP_EMAIL).toString())
      .map(userFacade::getOrCreateUserFromEmail)
      .orElseThrow(() -> new IllegalStateException("Invalid JWT Token"));
  }
}
