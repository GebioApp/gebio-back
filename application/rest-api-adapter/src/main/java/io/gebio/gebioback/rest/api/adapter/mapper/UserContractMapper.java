package io.gebio.gebioback.rest.api.adapter.mapper;

import io.gebio.gebioback.contract.model.CardOwnerInfoContract;
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
    userContract.setLogo(user.profileLogo());
    currentUserResponseContract.setUser(userContract);
    return currentUserResponseContract;
  }

  static CardOwnerInfoContract domainToCardOwnerInfoContract(User user) {
    CardOwnerInfoContract cardOwnerInfoContract = new CardOwnerInfoContract();
    cardOwnerInfoContract.setId(user.id());
    cardOwnerInfoContract.setLogo(user.profileLogo());
    cardOwnerInfoContract.setEmail(user.email());
    return cardOwnerInfoContract;
  }

  static UserContract memberFromDomainToContract(User user) {
    UserContract userContract = new UserContract();
    userContract.setId(user.id());
    userContract.setLogo(user.profileLogo());
    userContract.setEmail(user.email());
    return userContract;
  }
}
