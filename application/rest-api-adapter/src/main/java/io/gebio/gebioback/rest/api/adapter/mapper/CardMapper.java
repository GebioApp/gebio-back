package io.gebio.gebioback.rest.api.adapter.mapper;

import io.gebio.gebioback.contract.model.AddCardResponseContract;
import io.gebio.gebioback.contract.model.CardContract;
import io.gebio.gebioback.contract.model.CardOwnerInfoContract;
import io.gebio.gebioback.contract.model.CardPositionContract;
import io.gebio.gebioback.domain.model.Card;
import io.gebio.gebioback.domain.model.User;

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
        cardContract.getOwnerInfo().getLogo()
      )
    );
  }

  static CardPositionContract fromDomainToContract(Card.Position position) {
    CardPositionContract cardPositionContract = new CardPositionContract();
    cardPositionContract.setPosX(position.posX());
    cardPositionContract.setPosY(position.posY());
    return cardPositionContract;
  }

  static AddCardResponseContract addCardFromDomainToContract(Card card) {
    AddCardResponseContract addCardResponseContract =
      new AddCardResponseContract();
    CardContract cardContract = new CardContract();
    cardContract.setId(card.id());
    cardContract.setContent(card.content());
    cardContract.setColor(card.color());

    CardPositionContract cardPositionContract = new CardPositionContract();
    cardPositionContract.setPosX(card.position().posX());
    cardPositionContract.setPosY(card.position().posY());
    cardContract.setPosition(cardPositionContract);

    CardOwnerInfoContract cardOwnerInfoContract = new CardOwnerInfoContract();
    cardOwnerInfoContract.setId(card.owner().id());
    cardOwnerInfoContract.setEmail(card.owner().email());
    cardOwnerInfoContract.setLogo(card.owner().profileLogo());
    cardContract.setOwnerInfo(cardOwnerInfoContract);

    addCardResponseContract.setCard(cardContract);
    return addCardResponseContract;
  }
}
