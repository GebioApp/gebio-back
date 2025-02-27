package io.gebio.gebioback.rest.api.adapter.mapper;

import io.gebio.gebioback.contract.model.BoardContract;
import io.gebio.gebioback.contract.model.CreateBoardResponseContract;
import io.gebio.gebioback.contract.model.FindBoardResponseContract;
import io.gebio.gebioback.domain.service.Board;

public interface BoardMapper {
  static CreateBoardResponseContract createBoardFromDomainToContract(
    Board createdBoard
  ) {
    CreateBoardResponseContract createBoardResponseContract =
      new CreateBoardResponseContract();
    BoardContract boardContract = new BoardContract();
    boardContract.setId(createdBoard.id());
    boardContract.setTitle(createdBoard.name());
    boardContract.setTemplateId(createdBoard.templateId());
    boardContract.setOwnerId(createdBoard.owner().id());
    boardContract.setCards(
      createdBoard
        .cards()
        .stream()
        .map(CardMapper::fromDomainToContract)
        .toList()
    );
    createBoardResponseContract.setBoard(boardContract);
    return createBoardResponseContract;
  }

  static FindBoardResponseContract findBoardFromDomainToContract(Board board) {
    FindBoardResponseContract findBoardResponseContract =
      new FindBoardResponseContract();
    BoardContract boardContract = new BoardContract();
    boardContract.setId(board.id());
    boardContract.setTitle(board.name());
    boardContract.setTemplateId(board.templateId());
    boardContract.setOwnerId(board.owner().id());
    boardContract.setCards(
      board.cards().stream().map(CardMapper::fromDomainToContract).toList()
    );
    findBoardResponseContract.setBoard(boardContract);
    return findBoardResponseContract;
  }
}
