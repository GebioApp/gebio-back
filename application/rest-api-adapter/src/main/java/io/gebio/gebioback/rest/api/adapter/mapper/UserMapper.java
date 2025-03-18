package io.gebio.gebioback.rest.api.adapter.mapper;

import io.gebio.gebioback.contract.model.*;
import io.gebio.gebioback.domain.model.Board;
import io.gebio.gebioback.domain.model.User;
import java.util.List;

public interface UserMapper {
  static CurrentUserResponseContract domainToContract(User user) {
    CurrentUserResponseContract currentUserResponseContract =
      new CurrentUserResponseContract();
    UserContract userContract = fromDomainToContract(user);
    currentUserResponseContract.setUser(userContract);
    return currentUserResponseContract;
  }

  static CardOwnerInfoContract domainToCardOwnerInfoContract(User user) {
    CardOwnerInfoContract cardOwnerInfoContract = new CardOwnerInfoContract();
    cardOwnerInfoContract.setId(user.id());
    cardOwnerInfoContract.setLogo(user.profileLogo());
    cardOwnerInfoContract.setEmail(user.email());
    cardOwnerInfoContract.setUsername(user.username());
    cardOwnerInfoContract.setRole(UserRoleContract.valueOf(user.role().name()));
    return cardOwnerInfoContract;
  }

  static CreateGuestResponseContract createGuestFromDomainToContract(
    User guest
  ) {
    CreateGuestResponseContract createGuestResponseContract =
      new CreateGuestResponseContract();
    UserContract userContract = fromDomainToContract(guest);
    createGuestResponseContract.setUser(userContract);
    return createGuestResponseContract;
  }

  static FindBoardsResponseContract findBoardsDomainToContract(
    List<Board> boards
  ) {
    FindBoardsResponseContract findBoardsResponseContract =
      new FindBoardsResponseContract();
    List<BoardContract> boardContracts = boards
      .stream()
      .map(BoardMapper::fromDomainToContract)
      .toList();
    findBoardsResponseContract.setBoards(boardContracts);
    return findBoardsResponseContract;
  }

  static UserContract fromDomainToContract(User user) {
    UserContract userContract = new UserContract();
    userContract.setId(user.id());
    userContract.setLogo(user.profileLogo());
    userContract.setEmail(user.email());
    userContract.setUsername(user.username());
    userContract.setRole(UserRoleContract.valueOf(user.role().name()));
    return userContract;
  }
}
