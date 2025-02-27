package io.gebio.gebioback.postgres.mapper;

import io.gebio.gebioback.domain.model.Card;
import io.gebio.gebioback.postgres.entity.BoardEntity;
import io.gebio.gebioback.postgres.entity.CardEntity;

public interface CardMapper {
  static CardEntity fromDomainToEntity(Card card, BoardEntity boardEntity) {
    return new CardEntity(
      card.id(),
      card.content(),
      card.color(),
      card.position().posX(),
      card.position().posY(),
      UserMapper.domainToEntity(card.owner()),
      boardEntity
    );
  }

  static Card fromEntityToDomain(CardEntity cardEntity) {
    return new Card(
      cardEntity.getId(),
      cardEntity.getContent(),
      cardEntity.getColor(),
      new Card.Position(cardEntity.getPosX(), cardEntity.getPosY()),
      UserMapper.entityToDomain(cardEntity.getOwner())
    );
  }
}
