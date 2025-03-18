package io.gebio.gebioback.rest.api.adapter.mapper;

import io.gebio.gebioback.contract.model.AddUserToBoardResponseContract;
import io.gebio.gebioback.contract.model.BoardContract;
import io.gebio.gebioback.contract.model.CreateBoardResponseContract;
import io.gebio.gebioback.contract.model.FindBoardResponseContract;
import io.gebio.gebioback.domain.model.Board;

public interface BoardMapper {
  static CreateBoardResponseContract createBoardFromDomainToContract(
    Board createdBoard
  ) {
    CreateBoardResponseContract createBoardResponseContract =
      new CreateBoardResponseContract();
    BoardContract boardContract = fromDomainToContract(createdBoard);
    createBoardResponseContract.setBoard(boardContract);
    return createBoardResponseContract;
  }

  static FindBoardResponseContract findBoardFromDomainToContract(Board board) {
    FindBoardResponseContract findBoardResponseContract =
      new FindBoardResponseContract();
    BoardContract boardContract = fromDomainToContract(board);
    findBoardResponseContract.setBoard(boardContract);
    return findBoardResponseContract;
  }

  static AddUserToBoardResponseContract addUserFromDomainToContract(
    Board board
  ) {
    AddUserToBoardResponseContract addUserToBoardResponseContract =
      new AddUserToBoardResponseContract();
    BoardContract boardContract = fromDomainToContract(board);
    addUserToBoardResponseContract.setBoard(boardContract);
    return addUserToBoardResponseContract;
  }

  static BoardContract fromDomainToContract(Board board) {
    BoardContract boardContract = new BoardContract();
    boardContract.setId(board.id());
    boardContract.setTitle(board.name());
    boardContract.setTemplateId(board.templateId());
    boardContract.setOwnerId(board.owner().id());
    boardContract.setCards(
      board.cards().stream().map(CardMapper::fromDomainToContract).toList()
    );
    boardContract.setMembers(
      board.members().stream().map(UserMapper::fromDomainToContract).toList()
    );
    return boardContract;
  }
}
