package io.gebio.gebioback.postgres.adapter;

import io.gebio.gebioback.core.exception.BoardNotFound;
import io.gebio.gebioback.domain.model.Board;
import io.gebio.gebioback.domain.model.Card;
import io.gebio.gebioback.domain.port.out.CardRepositoryPort;
import io.gebio.gebioback.postgres.entity.BoardEntity;
import io.gebio.gebioback.postgres.entity.CardEntity;
import io.gebio.gebioback.postgres.mapper.BoardMapper;
import io.gebio.gebioback.postgres.mapper.CardMapper;
import io.gebio.gebioback.postgres.repository.BoardRepository;
import io.gebio.gebioback.postgres.repository.CardRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class PostgresCardAdapter implements CardRepositoryPort {

  private final BoardRepository boardRepository;
  private final CardRepository cardRepository;

  public PostgresCardAdapter(
    BoardRepository boardRepository,
    CardRepository cardRepository
  ) {
    this.boardRepository = boardRepository;
    this.cardRepository = cardRepository;
  }

  @Override
  public Card addCardToBoard(UUID boardId, Card card) {
    BoardEntity boardEntity = boardRepository
      .findById(boardId)
      .orElseThrow(() -> new BoardNotFound(boardId));
    return CardMapper.fromEntityToDomain(
      cardRepository.save(CardMapper.fromDomainToEntity(card, boardEntity))
    );
  }

  @Override
  public Board updateCardOnBoard(UUID boardId, Card card) {
    BoardEntity boardEntity = boardRepository
      .findById(boardId)
      .orElseThrow(() -> new BoardNotFound(boardId));
    Optional<CardEntity> cardEntityToUpdate = boardEntity
      .getCards()
      .stream()
      .filter(cardEntity -> cardEntity.getId().equals(card.id()))
      .findFirst();
    if (cardEntityToUpdate.isEmpty()) {
      return BoardMapper.entityToDomain(boardEntity);
    }
    CardEntity updatedCardEntity = cardEntityToUpdate.get();
    updatedCardEntity.setContent(card.content());
    updatedCardEntity.setColor(card.color());
    updatedCardEntity.setPosX(card.position().posX());
    updatedCardEntity.setPosY(card.position().posY());
    return BoardMapper.entityToDomain(boardRepository.save(boardEntity));
  }
}
