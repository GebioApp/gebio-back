package io.gebio.gebioback.domain.model;

import java.util.List;
import java.util.UUID;

public record Board(
  UUID id,
  String name,
  UUID templateId,
  User owner,
  List<Card> cards,
  List<User> members
) {}
