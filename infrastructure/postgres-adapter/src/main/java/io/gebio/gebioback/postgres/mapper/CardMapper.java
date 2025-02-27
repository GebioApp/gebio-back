package io.gebio.gebioback.postgres.mapper;

import io.gebio.gebioback.domain.model.Card;
import io.gebio.gebioback.postgres.entity.CardEntity;

public interface CardMapper {
  static CardEntity fromDomainToEntity(Card card) {
    return new CardEntity(
      card.id(),
      card.content(),
      card.color(),
      card.position().posX(),
      card.position().posY(),
      UserMapper.domainToEntity(card.owner())
    );
  }
}
