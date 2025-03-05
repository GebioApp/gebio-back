package io.gebio.gebioback.rest.api.adapter.mapper;

import io.gebio.gebioback.contract.model.CardContract;
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
}
