package io.gebio.gebioback.postgres.adapter;

import io.gebio.gebioback.core.exception.BoardNotFound;
import io.gebio.gebioback.core.exception.CardNotFound;
import io.gebio.gebioback.domain.model.Board;
import io.gebio.gebioback.domain.model.Card;
import io.gebio.gebioback.domain.port.out.CardRepositoryPort;
import io.gebio.gebioback.postgres.entity.BoardEntity;
import io.gebio.gebioback.postgres.entity.CardEntity;
import io.gebio.gebioback.postgres.mapper.BoardMapper;
import io.gebio.gebioback.postgres.mapper.CardMapper;
import io.gebio.gebioback.postgres.repository.BoardRepository;
import io.gebio.gebioback.postgres.repository.CardRepository;
import java.util.List;
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
  public Card updateCardOnBoard(UUID boardId, Card card) {
    boolean boardExists = boardRepository.existsById(boardId);
    if (!boardExists) {
      throw new BoardNotFound(boardId);
    }
    return cardRepository
      .findById(card.id())
      .map(cardEntity ->
        CardMapper.updateCardFromDomainToEntity(cardEntity, card)
      )
      .map(cardRepository::save)
      .map(CardMapper::fromEntityToDomain)
      .orElseThrow(() -> new CardNotFound(card.id()));
  }

  @Override
  public Board deleteBoardWithDeletedCard(UUID boardId, UUID cardId) {
    BoardEntity boardEntity = boardRepository
      .findById(boardId)
      .orElseThrow(() -> new BoardNotFound(boardId));

    List<CardEntity> newCardList = boardEntity
      .getCards()
      .stream()
      .filter(cardEntity -> !cardEntity.getId().equals(cardId))
      .toList();

    boardEntity.setCards(newCardList);
    return BoardMapper.entityToDomain(boardRepository.save(boardEntity));
  }
}
