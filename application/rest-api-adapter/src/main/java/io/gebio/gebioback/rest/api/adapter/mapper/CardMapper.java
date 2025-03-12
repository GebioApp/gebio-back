package io.gebio.gebioback.rest.api.adapter.mapper;

import io.gebio.gebioback.contract.model.*;
import io.gebio.gebioback.domain.model.Card;
import io.gebio.gebioback.domain.model.User;

public interface CardMapper {
  static AddCardResponseContract addCardFromDomainToContract(Card card) {
    AddCardResponseContract addCardResponseContract =
      new AddCardResponseContract();
    CardContract cardContract = fromDomainToContract(card);
    addCardResponseContract.setOperationType(OperationTypeContract.ADD);
    addCardResponseContract.setCard(cardContract);
    return addCardResponseContract;
  }

  static UpdateCardResponseContract updateCardFromDomainToContract(Card card) {
    UpdateCardResponseContract updateCardResponseContract =
      new UpdateCardResponseContract();
    CardContract cardContract = fromDomainToContract(card);
    updateCardResponseContract.setOperationType(OperationTypeContract.UPDATE);
    updateCardResponseContract.setCard(cardContract);
    return updateCardResponseContract;
  }

  static DeleteCardResponseContract deleteCardFromDomainToContract(
    Card deletedCard
  ) {
    DeleteCardResponseContract deleteCardResponseContract =
      new DeleteCardResponseContract();
    CardContract cardContract = fromDomainToContract(deletedCard);
    deleteCardResponseContract.setOperationType(OperationTypeContract.DELETE);
    deleteCardResponseContract.setCard(cardContract);
    return deleteCardResponseContract;
  }

  static CardContract fromDomainToContract(Card card) {
    CardContract cardContract = new CardContract();
    cardContract.setId(card.id());
    cardContract.setContent(card.content());
    cardContract.setColor(card.color());
    cardContract.setPosition(fromDomainToContract(card.position()));
    cardContract.setOwnerInfo(
      UserContractMapper.domainToCardOwnerInfoContract(card.owner())
    );
    cardContract.setBoardId(card.boardId());
    return cardContract;
  }

  static Card fromContractToDomain(CardContract cardContract) {
    return new Card(
      cardContract.getId(),
      cardContract.getContent(),
      cardContract.getColor(),
      new Card.Position(
        cardContract.getPosition().getPosX(),
        cardContract.getPosition().getPosY()
      ),
      new User(
        cardContract.getOwnerInfo().getId(),
        cardContract.getOwnerInfo().getEmail(),
        cardContract.getOwnerInfo().getLogo(),
        cardContract.getOwnerInfo().getUsername(),
        null
      ),
      null
    );
  }

  static CardPositionContract fromDomainToContract(Card.Position position) {
    CardPositionContract cardPositionContract = new CardPositionContract();
    cardPositionContract.setPosX(position.posX());
    cardPositionContract.setPosY(position.posY());
    return cardPositionContract;
  }
}
