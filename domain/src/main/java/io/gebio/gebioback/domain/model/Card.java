package io.gebio.gebioback.domain.model;

import java.util.UUID;

public record Card(
  UUID id,
  String content,
  String color,
  Position position,
  User owner
) {
  public record Position(int posX, int posY) {}
}
