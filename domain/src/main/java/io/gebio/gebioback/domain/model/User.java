package io.gebio.gebioback.domain.model;

import java.util.UUID;
import org.springframework.lang.Nullable;

public record User(
  UUID id,
  String email,
  @Nullable String profileLogo,
  @Nullable String username,
  UserRole role
) {
  private static final String DEFAULT_LOGO =
    "https://www.svgrepo.com/show/13656/user.svg";

  public static User buildAuthenticatedUser(
    String email,
    @Nullable String logo
  ) {
    return new User(
      UUID.randomUUID(),
      email,
      logo != null ? logo : DEFAULT_LOGO,
      null,
      UserRole.USER
    );
  }

  public static User buildGuestUser(String username) {
    return new User(
      UUID.randomUUID(),
      null,
      DEFAULT_LOGO,
      username,
      UserRole.GUEST
    );
  }
}
