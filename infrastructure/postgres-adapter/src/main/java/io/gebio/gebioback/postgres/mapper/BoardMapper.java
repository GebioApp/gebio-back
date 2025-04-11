package io.gebio.gebioback.postgres.mapper;

import io.gebio.gebioback.domain.model.Board;
import io.gebio.gebioback.domain.model.BoardCreationCommand;
import io.gebio.gebioback.postgres.entity.BoardEntity;
import io.gebio.gebioback.postgres.entity.CardEntity;
import io.gebio.gebioback.postgres.entity.UserEntity;
import java.time.ZoneOffset;
import java.util.List;

public interface BoardMapper {
  static Board entityToDomain(BoardEntity boardEntity) {
    return new Board(
      boardEntity.getId(),
      boardEntity.getName(),
      boardEntity.getTemplateId(),
      UserMapper.entityToDomain(boardEntity.getOwner()),
      boardEntity
        .getCards()
        .stream()
        .map(CardMapper::fromEntityToDomain)
        .toList(),
      boardEntity
        .getMembers()
        .stream()
        .map(UserMapper::entityToDomain)
        .toList(),
      boardEntity.getCreationDate() != null
        ? boardEntity.getCreationDate().atOffset(ZoneOffset.UTC)
        : null,
      boardEntity.getModificationDate() != null
        ? boardEntity.getModificationDate().atOffset(ZoneOffset.UTC)
        : null
    );
  }

  static BoardEntity domainToEntity(BoardCreationCommand board) {
    BoardEntity boardEntity = new BoardEntity(
      board.id(),
      board.name(),
      board.templateId(),
      UserMapper.domainToEntity(board.owner())
    );
    List<CardEntity> cardEntities = board
      .cards()
      .stream()
      .map(card -> CardMapper.fromDomainToEntity(card, boardEntity))
      .toList();
    List<UserEntity> members = board
      .members()
      .stream()
      .map(UserMapper::domainToEntity)
      .toList();
    boardEntity.setCards(cardEntities);
    boardEntity.setMembers(members);
    return boardEntity;
  }

  static BoardEntity domainToEntity(Board board) {
    BoardEntity boardEntity = new BoardEntity(
      board.id(),
      board.name(),
      board.templateId(),
      UserMapper.domainToEntity(board.owner())
    );
    List<CardEntity> cardEntities = board
      .cards()
      .stream()
      .map(card -> CardMapper.fromDomainToEntity(card, boardEntity))
      .toList();
    List<UserEntity> members = board
      .members()
      .stream()
      .map(UserMapper::domainToEntity)
      .toList();
    boardEntity.setCards(cardEntities);
    boardEntity.setMembers(members);
    return boardEntity;
  }
}
