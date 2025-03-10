package io.gebio.gebioback.rest.api.adapter.mapper;

import io.gebio.gebioback.contract.model.AddCardResponseContract;
import io.gebio.gebioback.contract.model.CardContract;
import io.gebio.gebioback.contract.model.CardPositionContract;
import io.gebio.gebioback.contract.model.UpdateCardResponseContract;
import io.gebio.gebioback.domain.model.Card;
import io.gebio.gebioback.domain.model.User;
import java.util.UUID;

public interface CardMapper {
  static AddCardResponseContract addCardFromDomainToContract(Card card) {
    AddCardResponseContract addCardResponseContract =
      new AddCardResponseContract();
    CardContract cardContract = fromDomainToContract(card);
    addCardResponseContract.setCard(cardContract);
    return addCardResponseContract;
  }

  static UpdateCardResponseContract updateCardFromDomainToContract(Card card) {
    UpdateCardResponseContract updateCardResponseContract =
      new UpdateCardResponseContract();
    CardContract cardContract = fromDomainToContract(card);
    updateCardResponseContract.setCard(cardContract);
    return updateCardResponseContract;
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

  static Card fromContractToDomain(UUID boardId, CardContract cardContract) {
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
      ),
      boardId
    );
  }

  static CardPositionContract fromDomainToContract(Card.Position position) {
    CardPositionContract cardPositionContract = new CardPositionContract();
    cardPositionContract.setPosX(position.posX());
    cardPositionContract.setPosY(position.posY());
    return cardPositionContract;
  }
}
