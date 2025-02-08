package port.in;

import model.User;

public interface UserFacade {
  User getOrCreateUserFromEmail(String email);
}
