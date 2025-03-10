package io.gebio.gebioback.postgres.adapter;

import io.gebio.gebioback.core.exception.BoardNotFound;
import io.gebio.gebioback.domain.model.Board;
import io.gebio.gebioback.domain.model.Card;
import io.gebio.gebioback.domain.port.out.BoardRepositoryPort;
import io.gebio.gebioback.postgres.entity.BoardEntity;
import io.gebio.gebioback.postgres.entity.CardEntity;
import io.gebio.gebioback.postgres.mapper.BoardMapper;
import io.gebio.gebioback.postgres.mapper.CardMapper;
import io.gebio.gebioback.postgres.repository.BoardRepository;
import io.gebio.gebioback.postgres.repository.CardRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class PostgresBoardAdapter implements BoardRepositoryPort {

  private final BoardRepository boardRepository;
  private final CardRepository cardRepository;

  public PostgresBoardAdapter(
    BoardRepository boardRepository,
    CardRepository cardRepository
  ) {
    this.boardRepository = boardRepository;
    this.cardRepository = cardRepository;
  }

  @Override
  public Board save(Board board) {
    return BoardMapper.entityToDomain(
      boardRepository.save(BoardMapper.domainToEntity(board))
    );
  }

  @Override
  public Optional<Board> findById(UUID boardId) {
    return boardRepository.findById(boardId).map(BoardMapper::entityToDomain);
  }

  @Override
  public Card updateBoardWithNewCardForBoardId(UUID boardId, Card card) {
    BoardEntity boardEntity = boardRepository
      .findById(boardId)
      .orElseThrow(() -> new BoardNotFound(boardId));
    return CardMapper.fromEntityToDomain(
      cardRepository.save(CardMapper.fromDomainToEntity(card, boardEntity))
    );
  }

  @Override
  public Board updateBoardWithUpdatedCardForBoardId(UUID boardId, Card card) {
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
