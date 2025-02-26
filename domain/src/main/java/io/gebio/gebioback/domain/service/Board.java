package io.gebio.gebioback.domain.service;

import io.gebio.gebioback.domain.model.Card;
import io.gebio.gebioback.domain.model.User;
import java.util.List;
import java.util.UUID;

public record Board(
  UUID id,
  String name,
  UUID templateId,
  User owner,
  List<Card> cards
) {}
