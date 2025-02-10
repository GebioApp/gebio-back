package io.gebio.gebioback.rest.api.adapter.mapper;

import io.gebio.gebioback.contract.model.CurrentUserResponseContract;
import io.gebio.gebioback.contract.model.UserContract;
import io.gebio.gebioback.domain.model.User;

public interface UserContractMapper {
  static CurrentUserResponseContract domainToContract(User user) {
    CurrentUserResponseContract currentUserResponseContract =
      new CurrentUserResponseContract();
    UserContract userContract = new UserContract();
    userContract.setId(user.id());
    userContract.setEmail(user.email());
    currentUserResponseContract.setUser(userContract);
    return currentUserResponseContract;
  }
}
