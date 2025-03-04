package io.gebio.gebioback.rest.api.adapter.mapper;

import io.gebio.gebioback.contract.model.AddCardResponseContract;
import io.gebio.gebioback.contract.model.CardContract;
import io.gebio.gebioback.contract.model.CardPositionContract;
import io.gebio.gebioback.domain.model.Card;

public interface CardMapper {
  static CardContract fromDomainToContract(Card card) {
    CardContract cardContract = new CardContract();
    cardContract.setId(card.id());
    cardContract.setContent(card.content());
    cardContract.setColor(card.color());
    cardContract.setPosition(fromDomainToContract(card.position()));
    cardContract.setOwnerInfo(
      UserContractMapper.domainToCardOwnerInfoContract(card.owner())
    );
    return cardContract;
  }

  static CardPositionContract fromDomainToContract(Card.Position position) {
    CardPositionContract cardPositionContract = new CardPositionContract();
    cardPositionContract.setPosX(position.posX());
    cardPositionContract.setPosY(position.posY());
    return cardPositionContract;
  }

  static AddCardResponseContract addCardFromDomainToContract(
    CardContract cardContract
  ) {
    AddCardResponseContract addCardResponseContract =
      new AddCardResponseContract();
    CardContract card = new CardContract();
    card.setId(cardContract.getId());
    card.setContent(cardContract.getContent());
    card.setColor(cardContract.getColor());
    card.setPosition(cardContract.getPosition());
    card.setOwnerInfo(cardContract.getOwnerInfo());
    addCardResponseContract.setCard(card);
    return addCardResponseContract;
  }
}
