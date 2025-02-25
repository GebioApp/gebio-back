package io.gebio.gebioback.postgres.mapper;

import io.gebio.gebioback.domain.service.Board;
import io.gebio.gebioback.postgres.entity.BoardEntity;

public interface BoardMapper {
  static Board entityToDomain(BoardEntity boardEntity) {
    return new Board(
      boardEntity.getId(),
      boardEntity.getName(),
      boardEntity.getTemplateId(),
      UserMapper.entityToDomain(boardEntity.getOwner())
    );
  }

  static BoardEntity domainToEntity(Board board) {
    return new BoardEntity(
      board.id(),
      board.name(),
      board.templateId(),
      UserMapper.domainToEntity(board.owner())
    );
  }
}
