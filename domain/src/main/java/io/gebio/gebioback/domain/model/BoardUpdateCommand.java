package io.gebio.gebioback.domain.model;

import java.util.UUID;

public record BoardUpdateCommand(UUID boardId, UUID userId, String name) {}
