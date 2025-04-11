package io.gebio.gebioback.domain.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public record Board(
  UUID id,
  String name,
  UUID templateId,
  User owner,
  List<Card> cards,
  List<User> members,
  OffsetDateTime createdAt,
  OffsetDateTime updatedAt
) {
  public Board addUser(User user) {
    List<User> updatedMembers = Stream.concat(
      members.stream(),
      Stream.of(user)
    ).toList();
    return new Board(
      id,
      name,
      templateId,
      owner,
      cards,
      updatedMembers,
      createdAt,
      updatedAt
    );
  }
}
