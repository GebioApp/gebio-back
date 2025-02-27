package io.gebio.gebioback.rest.api.adapter.mapper;

import io.gebio.gebioback.contract.model.CreateBoardResponseContract;
import io.gebio.gebioback.domain.service.Board;

public interface BoardMapper {
  static CreateBoardResponseContract createBoardFromDomainToContract(
    Board createdBoard
  ) {
    CreateBoardResponseContract createBoardResponseContract =
      new CreateBoardResponseContract();
    createBoardResponseContract.setId(createdBoard.id());
    createBoardResponseContract.setTitle(createdBoard.name());
    createBoardResponseContract.setTemplateId(createdBoard.templateId());
    createBoardResponseContract.setOwnerId(createdBoard.owner().id());
    createBoardResponseContract.setCards(
      createdBoard
        .cards()
        .stream()
        .map(CardMapper::fromDomainToContract)
        .toList()
    );
    return createBoardResponseContract;
  }
}
