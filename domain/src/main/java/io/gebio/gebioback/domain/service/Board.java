package io.gebio.gebioback.domain.service;

import io.gebio.gebioback.domain.model.Card;
import io.gebio.gebioback.domain.model.User;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public record Board(
  UUID id,
  String name,
  UUID templateId,
  User owner,
  List<Card> cards
) {
  public static Board addCardToBoard(Board board, Card card) {
    return new Board(
      board.id(),
      board.name(),
      board.templateId(),
      board.owner(),
      Stream.concat(board.cards.stream(), Stream.of(card)).toList()
    );
  }

  Board updateCard(Card card) {
    List<Card> updatedCards = cards
      .stream()
      .map(existingCard -> existingCard.equals(card) ? card : existingCard)
      .toList();
    return new Board(id, name, templateId, owner, updatedCards);
  }
}
